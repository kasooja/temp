package classifier.weka;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class CrossFold {

	public static void main(String[] args) {

		Instances trainingData = Commons.loadWekaData("C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/Weka/pos_data/data/train_pos_lat_acl_data.arff");

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

			Evaluation eval = new Evaluation(trainingData);
			
			Random rand = new Random(49); 
			int folds = 10;
			System.out.println("Cross validation started");
			eval.crossValidateModel(filtClassifier1, trainingData, folds, rand);
			System.out.println("done");
			System.out.println(eval.toSummaryString());

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}