package classifier.weka;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.BasicFileTools;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;


public class TopFeatures {

	public static void main(String[] args) {

		String dir = "C:/Users/Kartik Asooja/Dropbox/_anneKartik/data/Experiment/"
				+ "Temporal_Classification/Train/ACL_Corpus/3_year/pos_filt/";

		Instances acl_data = Commons.loadWekaData(dir + "train_acl_data_pos_3.arff");

//		Instances mts_data = Commons.loadWekaData("C:/Users/Kartik Asooja/Dropbox/_anneKartik/data/Experiment/"
	//			+ "Temporal_Classification/Test/MTS/3_year/pos_filt/test_mts_data_pos_3.arff");

		Instances trainingData = acl_data;

		String fileName = dir + "top_1500_feat_uni_acl_data_pos_3.txt";

		StringToWordVector stringToWordVectorFilter = Commons.getStringToWordVectorFilter();		
		AttributeSelection attributeSelectionFilter = Commons.getAttributeSelectionFilter();

		try {			
			stringToWordVectorFilter.setInputFormat(trainingData);

			Instances tokens = Filter.useFilter(trainingData, stringToWordVectorFilter);
			tokens.setClassIndex(0);

			attributeSelectionFilter.setInputFormat(tokens);

			Instances topAttributes = Filter.useFilter(tokens, attributeSelectionFilter);
			StringBuilder bld = new StringBuilder();


			Map<String, Map<String, Double>> attrClassAvgValue = new HashMap<String, Map<String, Double>>();



			for(int i=0; i<topAttributes.numAttributes(); i++){
				Attribute attribute = topAttributes.attribute(i);
				double[] attributeVals = topAttributes.attributeToDoubleArray(i);
				for(int j=0; j<attributeVals.length; j++){
					if(attributeVals[j]!=0.0){
						Instance instance = topAttributes.instance(j);
						double classValue2 = instance.classValue();
						String classValue = instance.classAttribute().value((int)classValue2);
					//	System.out.println(classValue);

						if(!attrClassAvgValue.containsKey(attribute.name())){
							attrClassAvgValue.put(attribute.name(), new HashMap<String, Double>());
						}

						if(!attrClassAvgValue.get(attribute.name()).containsKey(classValue)){
							attrClassAvgValue.get(attribute.name()).put(classValue, 0.0);
						}

						Double prevVal = attrClassAvgValue.get(attribute.name()).get(classValue);
						attrClassAvgValue.get(attribute.name()).put(classValue, prevVal + attributeVals[j]);
					}
				}

			}

			Attribute classAttribute = topAttributes.classAttribute();
			Enumeration<Object> enumeration = classAttribute.enumerateValues();
			List<String> classValues = new ArrayList<String>();
			String sep = ",,,,";
			bld.append(sep);
			while(enumeration.hasMoreElements()){
				Object classVal = enumeration.nextElement();
				bld.append(classVal + sep);
				classValues.add((String)classVal);
			}
			bld.append("\n");

			double numInstances = (double) topAttributes.numInstances();

			for(String attr : attrClassAvgValue.keySet()){
				attr = attr.replaceAll(",", "-com-");
				Map<String, Double> map = attrClassAvgValue.get(attr);
				bld.append(attr + sep);
				for(String classVal : classValues){					
					double avgVal = 0.0;
					if(map.containsKey(classVal)){
						Double double1 = map.get(classVal);
						if(double1 != null){
							avgVal = double1 / numInstances;
						}
					}
					bld.append(avgVal + sep);
				}
				bld.append("\n");
			}
			BasicFileTools.writeFile(fileName, bld.toString().trim());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

}