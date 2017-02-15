package parscit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import common.BasicFileTools;
import common.MapSort;
import common.RegexTokenizer;
import common.SerializationUtils;

public class RegexTextCleaner {

	public static void main(String[] args) {
		String uncleaned_text_dir = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Uncompressed_Separated_Text/";
		String cleaned_text_dir = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Cleaned_Uncompressed_Separated_Text/";
		int fileNo = 0;
		
		Map<String, Integer> wordDict = new HashMap<String, Integer>();
		String saveTo = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/by_products/serialized_dict.dic";
	
		Map<String, Integer> uncleaned_wordDict = new HashMap<String, Integer>();
		String uncleaned_saveTo = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/by_products/serialized_uncleaned_dict.dic";
			
		if(!new File(cleaned_text_dir).exists())
			new File(cleaned_text_dir).mkdir();
		for(File yearDir : new File(uncleaned_text_dir).listFiles()){
			if(yearDir.isHidden())
				continue;
			String cleanedYearDir = cleaned_text_dir + "/" + yearDir.getName();
			if(!new File(cleanedYearDir).exists())
				new File(cleanedYearDir).mkdir();
			for(File textFile : yearDir.listFiles()){
				if(textFile.isHidden())
					continue;				
				fileNo++;
				if(fileNo%500 == 0)
					System.out.println(fileNo);
				String text = BasicFileTools.extractText(textFile, " \n ");
				StringBuilder bld = new StringBuilder();
				//System.out.println("uncleaned: " + text);
				
				String[] uncleanedTokens = text.split("\\s+");
				for(String uncleanedToken : uncleanedTokens){
					if(!uncleaned_wordDict.containsKey(uncleanedToken))
						uncleaned_wordDict.put(uncleanedToken, 0);					
					uncleaned_wordDict.put(uncleanedToken, uncleaned_wordDict.get(uncleanedToken)+1);
				}
				String raw = text;
				text = RegexTokenizer.clean_text(text);
				
			//	System.out.println("cleaned: " + text);
				
				for(String token : RegexTokenizer.split_text(text)){
					if("alignments.".equalsIgnoreCase(token)){
						System.out.println("fo");
					}
				//	System.out.print(token + " | ");
					if(!wordDict.containsKey(token))
						wordDict.put(token, 0);					
					wordDict.put(token, wordDict.get(token)+1);
					bld.append(token.trim() + " ");
				}
				
				String cleanedTextFile = cleanedYearDir + "/" + textFile.getName();		
				BasicFileTools.writeFile(cleanedTextFile, bld.toString().trim());
			}
		}
		
		wordDict = MapSort.sortByValue(wordDict);	
		SerializationUtils.saveObject(wordDict, new File(saveTo));
		
		uncleaned_wordDict = MapSort.sortByValue(uncleaned_wordDict);		
		SerializationUtils.saveObject(uncleaned_wordDict, new File(uncleaned_saveTo));
		
	}

}
