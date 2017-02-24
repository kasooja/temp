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

	//private static String dataDirPath = "C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/Parscit/Uncompressed_Separated_Text/";
	//private static String dataDirPath = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_cleaned_pos/";
	//private static String dataDirPath = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Test/mts/mts_small/";
	private static String dataDirPath = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Test/txt_trec/trec_small/";
	//private static String dataDirPath = "C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/nlp4nlp/trec/trec_small/";
	//private static String dataDirPath = "C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/nlp4nlp/mts/mts_small/";
	//private static String dataDirPath = "C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/nlp4nlp/trec/trec_extracted/";
	//private static String dataDirPath = "C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/nlp4nlp/mts/mts_extracted/";
	
	private static String arff = "src/main/resources/weka/test_pos_lat_trec_data.arff";	

	private static boolean train = false;

	private static Pattern pattern = Pattern.compile("trec(\\d\\d\\d\\d)-.*.txt");
	//private static Pattern pattern = Pattern.compile("MTS(\\d\\d\\d\\d)-.*.txt");

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
			for(int i=1979; i<=2014; i=i+5){
				attVals.add(String.valueOf(i));				
			}			
		}

		atts.add(new Attribute("time_period", attVals));

		Instances data = new Instances("corpus", atts, 0);

		
		if(train){
			for(File classDir : dataDir.listFiles()){
				if(!classDir.isHidden()){				
					String className = classDir.getName();				
					for(File dataFile : classDir.listFiles()){
						if(!dataFile.isHidden()){	
							double[] vals = new double[data.numAttributes()];//returns number of attributes
							String fileText = BasicFileTools.extractText(dataFile, " \n ").trim();
							
							vals[0] = data.attribute(0).addStringValue(fileText);
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
						String fileText = BasicFileTools.extractText(dataFile, " \n ").trim();
						vals[0] = data.attribute(0).addStringValue(fileText);
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

		//System.out.println(data);

	}

	public static String getClassNameFromYear(String fileName){
		Matcher matcher = pattern.matcher(fileName);
		if(matcher.find()){
			String yearAsString = matcher.group(1).trim();
			int year = Integer.parseInt(yearAsString);
			if(year >= 1979 && year <= 1983){
				return String.valueOf(1979);
			} else if(year >= 1984 && year <= 1988){
				return String.valueOf(1984);
			} else if(year >= 1989 && year <= 1993){
				return String.valueOf(1989);
			} else if(year >= 1994 && year <= 1998){
				return String.valueOf(1994);
			} else if(year >= 1999 && year <= 2003){
				return String.valueOf(1999);
			} else if(year >= 2004 && year <= 2008){
				return String.valueOf(2004);
			} else if(year >= 2009 && year <= 2013){
				return String.valueOf(2009);
			} else if(year >= 2014 && year <= 2015){
				return String.valueOf(2014);
			} else {
				return null;
			}
		}

		return null;
	}

}

//if(matcher.find()){
//	String yearAsString = matcher.group(1).trim();
//	int year = Integer.parseInt(yearAsString);
//	if(year >= 1965 && year <= 1975){
//		return String.valueOf(1);
//	} else if(year >= 1978 && year <= 1982){
//		return String.valueOf(2);
//	} else if(year >= 1983 && year <= 1987){
//		return String.valueOf(3);
//	} else if(year >= 1988 && year <= 1992){
//		return String.valueOf(4);
//	} else if(year >= 1993 && year <= 1997){
//		return String.valueOf(5);
//	} else if(year >= 1998 && year <= 2002){
//		return String.valueOf(6);
//	} else if(year >= 2003 && year <= 2006){
//		return String.valueOf(7);
//	} else {
//		return null;
//	}
//}
