package classifier.weka;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Counter {
	private static Pattern yearPatt = Pattern.compile("\\w(\\d+)-.*");

	public static void main(String[] args) {
		String dir = "C:/Users/Kartik Asooja/Downloads/Anne/CurrentData/"
				+ "Parscit/Uncompressed_Separated_Text/All/";
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

			if(year < 20){
				if(year<=9){
					year = Integer.parseInt("200" + String.valueOf(year));					
				} else {
					year = Integer.parseInt("20" + String.valueOf(year));
				}
			} else {
				year = Integer.parseInt("19" + String.valueOf(year));
			}
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
		for(Integer year : yearFileMap.keySet()){
			List<String> list = yearFileMap.get(year);
			System.out.println("(" + year + "," + list.size() + ")");
			valid = valid + list.size();
		}
	
		System.out.println("Validity: ");
		System.out.println(fileNo);
		System.out.println(valid);
	}

}
