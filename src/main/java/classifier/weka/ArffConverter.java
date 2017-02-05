package classifier.weka;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import common.BasicFileTools;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class ArffConverter {

	private static String dataDirPath = "src/main/resources/corpusDirPlainTxt/";
	//private static String dataDirPath = "src/main/resources/test/trecData/";
	//private static String dataDirPath = "src/main/resources/test/mts_preprocessed/";
	
	private static String arff = "src/main/resources/weka/train.arff";	

	private static boolean train = true;

	//private static Pattern pattern = Pattern.compile("trec(\\d\\d\\d\\d)-.*.txt");
	private static Pattern pattern = Pattern.compile("MTS(\\d\\d\\d\\d)-.*.txt");


	public static void main(String[] args) throws IOException {	
		ArrayList<Attribute> atts;
		atts = new ArrayList<Attribute>();

		atts.add(new Attribute("PubText", (ArrayList<String>) null));//attribute contains string values

		ArrayList<String> attVals = new ArrayList<String>();

		File dataDir = new File(dataDirPath);


		if(train){
			File[] files = dataDir.listFiles();
			Arrays.sort(files);
			for(File classDir : files){
				if(!classDir.isHidden()){
					attVals.add(classDir.getName());				
				}
			}
		} else {
			for(int i=1; i<=7; i++){
				attVals.add(String.valueOf(i));				
			}			
		}

		atts.add(new Attribute("decade", attVals));

		Instances data = new Instances("corpus", atts, 0);

		
		if(train){
			for(File classDir : dataDir.listFiles()){
				if(!classDir.isHidden()){				
					String className = classDir.getName();				
					for(File dataFile : classDir.listFiles()){
						if(!dataFile.isHidden()){	
							double[] vals = new double[data.numAttributes()];//returns number of attributes						
							vals[0] = data.attribute(0).addStringValue(BasicFileTools.extractText(dataFile).trim());
							vals[1] = attVals.indexOf(className);
							data.add(new DenseInstance(1.0, vals));
						}
					}
				}
			}
		} else {			
			for(File dataFile : dataDir.listFiles()){							
				if(!dataFile.isHidden()){	
					String className = getClassNameFromYear(dataFile.getName());	
					if(className!=null){
						double[] vals = new double[data.numAttributes()];//returns number of attributes						
						vals[0] = data.attribute(0).addStringValue(BasicFileTools.extractText(dataFile).trim());
						vals[1] = attVals.indexOf(className);
						data.add(new DenseInstance(1.0, vals));
					}
				}
			}
		}		

		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		try {
			saver.setFile(new File(arff));
			saver.writeBatch();
		} catch (IOException e) {
			e.printStackTrace();
		}			

		System.out.println(data);

	}

	public static String getClassNameFromYear(String fileName){
		Matcher matcher = pattern.matcher(fileName);
		if(matcher.find()){
			String yearAsString = matcher.group(1).trim();
			int year = Integer.parseInt(yearAsString);
			if(year >= 1965 && year <= 1975){
				return String.valueOf(1);
			} else if(year >= 1978 && year <= 1982){
				return String.valueOf(2);
			} else if(year >= 1983 && year <= 1987){
				return String.valueOf(3);
			} else if(year >= 1988 && year <= 1992){
				return String.valueOf(4);
			} else if(year >= 1993 && year <= 1997){
				return String.valueOf(5);
			} else if(year >= 1998 && year <= 2002){
				return String.valueOf(6);
			} else if(year >= 2003 && year <= 2006){
				return String.valueOf(7);
			} else {
				return null;
			}
		}

		return null;
	}

}
