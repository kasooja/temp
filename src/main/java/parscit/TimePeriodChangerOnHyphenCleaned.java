package parscit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimePeriodChangerOnHyphenCleaned {


	private static Pattern yearPatt = Pattern.compile("\\w(\\d+)-.*");

	public static void main(String[] args) throws IOException {
		String cleaned_text_dir = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/";
		String move_to_text_dir = "/home/kat/Downloads/Anne/Corpus/span_period_years/3/Hyphen_Cleaned_Uncompressed_Separated_Text/";

		if(!new File(move_to_text_dir).exists())
			new File(move_to_text_dir).mkdir();

		int fileNo = 0;
		int totRawFileNo = 0;

		int minYear = Integer.MAX_VALUE;
		int maxYear = Integer.MIN_VALUE;

		Map<Integer, List<String>> yearFileMap = new HashMap<Integer, List<String>>();

		for(File yearDir : new File(cleaned_text_dir).listFiles()){
			if(yearDir.isHidden())
				continue;
//			String move_to_text_dir_class_dir = move_to_text_dir + "/" + yearDir.getName();
//			if(!new File(move_to_text_dir_class_dir).exists())
//				new File(move_to_text_dir_class_dir).mkdir();

			for(File textFile : yearDir.listFiles()){
				totRawFileNo++;
				if(textFile.isHidden())
					continue;
				
				if(fileNo%500 == 0)
					System.out.println(fileNo);	

				String fileName = textFile.getName();
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
				System.out.println(year);
				if(year <= minYear){
					minYear = year;
				} else if(year > maxYear) {
					maxYear = year;
				}
				if(!yearFileMap.containsKey(year)){
					yearFileMap.put(year, new ArrayList<String>());
				}
				yearFileMap.get(year).add(textFile.getAbsolutePath());
				fileNo++;
			}
		}
		
		System.out.println(totRawFileNo);
		System.out.println("******************************");
		System.out.println(fileNo);
		System.out.println("******************************");		
		System.out.println(minYear);
		System.out.println("******************************");		
		System.out.println(maxYear);
		System.out.println("******************************");		

		Set<Integer> keySet = yearFileMap.keySet();
		List<Integer> sortedList = new ArrayList<Integer>(keySet);
		Collections.sort(sortedList);
		int span = 0;
		
		List<String> files = new ArrayList<String>();
		
		int classNameInt = 1;
		for(Integer year : sortedList){
			List<String> fileMap = yearFileMap.get(year);
			if(span < 3){
				System.out.println(year);			
				files.addAll(fileMap);
				span++;

			} else if(span>=3 || sortedList.size() == (sortedList.indexOf(year)+1)){
				System.out.println("******************************");		
				System.out.println(year);			

				String dest = move_to_text_dir + "/" + String.valueOf(classNameInt++);
				new File(dest).mkdir();
				for(String file : files){
					Files.copy(new File(file).toPath(), new File(dest + "/" +  new File(file).getName()).toPath());					
				}
				files = new ArrayList<String>();
				files.addAll(fileMap);
				span = 1;
			}
		}

		
	}
}
