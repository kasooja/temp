package classifier.weka;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestData_Counter {
	
	private static Pattern yearPatt = Pattern.compile("trec(\\d\\d\\d\\d)-.*.txt");

	public static void main(String[] args) {
		String dir = "C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/nlp4nlp/trec/trec_preprocessed/";
		
		int minYear = Integer.MAX_VALUE;
		int maxYear = Integer.MIN_VALUE;
		Map<Integer, List<String>> yearFileMap = new TreeMap<Integer, List<String>>();

		int fileNo = 0;
		for(File file : new File(dir).listFiles()){
			if(file.isHidden())
				continue;
			String fileName = file.getName();
			Matcher matcher = yearPatt.matcher(fileName);
			matcher.find();

			int year = Integer.parseInt(matcher.group(1).trim());

		
			//System.out.println(year);
			if(year <= minYear){
				minYear = year;
			} else if(year > maxYear) {
				maxYear = year;
			}
			if(!yearFileMap.containsKey(year)){
				yearFileMap.put(year, new ArrayList<String>());
			}
			yearFileMap.get(year).add(file.getAbsolutePath());
			fileNo++;

		}
		int valid = 0;
		for(int year = 1979; year<=2015; year++){
			int fileCo = 0;
			if(yearFileMap.containsKey(year)){
				fileCo = yearFileMap.get(year).size();
			}
			System.out.println("(" + year + "," + fileCo + ")");
			valid = valid + fileCo;
		}
	
		System.out.println("Validity: ");
		System.out.println(fileNo);
		System.out.println(valid);
	}

}
