package classifier.weka;

import java.util.ArrayList;

import common.BasicFileTools;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class TrainTest {

	private static int time_period_span = 3;
	private static ArrayList<String> attVals = new ArrayList<String>();

	static {
		for(int i=1979; i<=2015; i=i+time_period_span){
			attVals.add(String.valueOf(i));				
		}	
	}

	//textM, textF, textC
	public static String getXML(String text, String classLabel, int id){
		StringBuilder bld = new StringBuilder();
		bld.append("<text id=\"" + id + "\"" + ">\n");

		//List<String> tags = new ArrayList<String>();
		//String span_tag = null;

		//		if(time_period_span == 5){
		//			span_tag = "textM";
		//		} 

		StringBuilder yearVal = new StringBuilder();
		for(String attVal : attVals){
			if(attVal.equalsIgnoreCase(classLabel)){
				int endYear = Integer.valueOf(attVal) + time_period_span - 1;
				yearVal.append("yes=\"" + attVal + "-" + endYear + "\" ");
			} else {
				int endYear = Integer.valueOf(attVal) + time_period_span - 1;
				yearVal.append("no=\"" + attVal + "-" + endYear + "\" ");					
			}
		}
		bld.append("<textF " + yearVal.toString().trim() + "> \n");
		bld.append("<textM " + yearVal.toString().trim() + "> \n");
		bld.append("<textC " + yearVal.toString().trim() + "> \n");
		bld.append(text + "\n");		
		bld.append("</text>");
		return bld.toString().trim();
	}


	public static void main(String[] args) {

		Instances acl_data = Commons.loadWekaData("C:/Users/Kartik Asooja/Dropbox/_anneKartik/data/Experiment/"
				+ "Temporal_Classification/Train/ACL_Corpus/3_year/raw/train_acl_data_raw_3.arff");

		Instances mts_testData = Commons.loadWekaData("C:/Users/Kartik Asooja/Dropbox/_anneKartik/data/"
				+ "Experiment/Temporal_Classification/Test/MTS/3_year/raw/test_mts_data_raw_3.arff");
	
		//Instances trec_testData = Commons.loadWekaData("C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/Weka/pos_data/data/test_pos_lat_trec_data.arff");
		
		String saveModelPath = "C:/Users/Kartik Asooja/Dropbox/_anneKartik/data/Experiment/"
				+ "Temporal_Classification/Models/acl_data_svm_lin_uni_raw_1500_3.model";
		
		Instances trainingData = acl_data;
		
		Instances testData = mts_testData;

		String fileName = "svm_lin_uni_raw_3_Acl_on_Mts";


		StringToWordVector stringToWordVectorFilter = Commons.getStringToWordVectorFilter();		
		AttributeSelection attributeSelectionFilter = Commons.getAttributeSelectionFilter();
		
		
		Classifier svm = Commons.getFirstBinClassifierFromJson();
		
		try {			
			stringToWordVectorFilter.setInputFormat(trainingData);

			FilteredClassifier filtClassifier2 = new FilteredClassifier();			
			filtClassifier2.setClassifier(svm);
			filtClassifier2.setFilter(attributeSelectionFilter);

			FilteredClassifier filtClassifier1 = new FilteredClassifier();			
			filtClassifier1.setClassifier(filtClassifier2);
			filtClassifier1.setFilter(stringToWordVectorFilter);
			
			System.out.println("Training started");
			long startTime = System.currentTimeMillis();
			filtClassifier1.buildClassifier(trainingData);
			long endTime = System.currentTimeMillis();
			System.out.println("Done");
			System.out.println("Time take for " + trainingData.numInstances() + " instances: " + ((endTime - startTime)/1000) + " seconds.");

			int id = 1;

			StringBuilder xmlPredict = new StringBuilder();
			StringBuilder goldPredict = new StringBuilder();

			for(Instance instance : testData){
				double classVal = filtClassifier1.classifyInstance(instance);

				String classLabel = attVals.get((int) classVal);
				//System.out.println(classLabel);
				String text = instance.stringValue(instance.attribute(0)).replaceAll("\n", "  ");

				String xmlTextPredictTag = getXML(text, classLabel, id);
				xmlPredict.append(xmlTextPredictTag + "\n\n");

				String actualClassLabel =  attVals.get((int)instance.value(instance.classIndex()));
				String xmlTextGoldTag = getXML(text, actualClassLabel, id);				
				goldPredict.append(xmlTextGoldTag + "\n\n");

				id++;
			}
			
			
			BasicFileTools.writeFile("src/main/resources/predict_" + fileName + ".txt", xmlPredict.toString().trim());
			BasicFileTools.writeFile("src/main/resources/gold_" + fileName + ".txt" , goldPredict.toString().trim());

			Evaluation eval = new Evaluation(trainingData);
			eval.evaluateModel(filtClassifier1, testData);
			System.out.println("Weka results");
			System.out.println(eval.toSummaryString("\nResults\n======\n", false));
			
			weka.core.SerializationHelper.write(saveModelPath, filtClassifier1);

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

}