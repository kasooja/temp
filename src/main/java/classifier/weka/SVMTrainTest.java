package classifier.weka;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class SVMTrainTest {

	public static void main(String[] args) {

		Instances trainingData = Commons.loadWekaData("C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/Weka/pos_data/data/train_pos_lat_acl_data.arff");

		Instances testData = Commons.loadWekaData("C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/Weka/pos_data/data/test_pos_lat_mts_data.arff");

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
			filtClassifier1.buildClassifier(trainingData);
			System.out.println("Done");

			Evaluation eval = new Evaluation(trainingData);
			eval.evaluateModel(filtClassifier1, testData);

			System.out.println(eval.toSummaryString("\nResults\n======\n", false));


		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}