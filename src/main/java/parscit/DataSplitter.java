package parscit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataSplitter {

	public static void main(String[] args) {
		String compressedDirPath = "C:/Users/Kartik Asooja/Downloads/Parcit/Compressed/";
		String compressed_separatedDirPath = "C:/Users/Kartik Asooja/Downloads/Parcit/Compressed_Separated/";

		Pattern patt = Pattern.compile("[A-Z](\\d\\d)");
		File dir = new File(compressedDirPath);
		int filesNo = 0;
		int totRawFileNo = 0;

		int minYear = Integer.MAX_VALUE;
		int maxYear = Integer.MIN_VALUE;

		Map<Integer, List<String>> yearFileMap = new HashMap<Integer, List<String>>();

		for(File compressedFile : dir.listFiles()){
			String name = compressedFile.getName();
			Matcher matcher = patt.matcher(name);
			if(matcher.find()){
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
				if(year <= minYear){
					minYear = year;
				} else if(year > maxYear) {
					maxYear = year;
				}
				if(!yearFileMap.containsKey(year)){
					yearFileMap.put(year, new ArrayList<String>());
				} 
				yearFileMap.get(year).add(compressedFile.getAbsolutePath());				
				filesNo++;
			}
			totRawFileNo++;
		}

		System.out.println(totRawFileNo);
		System.out.println("******************************");
		System.out.println(filesNo);
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
			if(span < 5){
				System.out.println(year);			
				files.addAll(fileMap);
				span++;

			} else if(span>=5 || sortedList.size() == (sortedList.indexOf(year)+1)){
				System.out.println("******************************");		
				System.out.println(year);			

				String dest = compressed_separatedDirPath + "/" + String.valueOf(classNameInt++);
				new File(dest).mkdir();
				for(String file : files){
					new File(file).renameTo(new File(dest + "/" +  new File(file).getName()));
				}
				files = new ArrayList<String>();
				files.addAll(fileMap);
				span = 1;
			}
		}

	}

}
