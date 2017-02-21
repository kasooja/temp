package parscit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.BasicFileTools;
import common.MapSort;
import common.SerializationUtils;

public class HyphenCleaner {

	public static Map<String, Integer> replacedTokens = new HashMap<String, Integer>();

	public static Map<String, Integer> hyphens_not_replacedTokens = new HashMap<String, Integer>();


	public static List<String> bigram_hyphen_cleaning(String text, Map<String, Integer> word_dict){
		String[] tokens = text.split("\\s+");
		List<String> new_tokens = new ArrayList<String>();

		for(int i=0; i<tokens.length; i++){
			String firstToken = tokens[i];
			if(firstToken.endsWith("-")){
				if(firstToken.equalsIgnoreCase("misclas-")){
					System.out.println("break 1");
				}
				if(i+1<tokens.length){
					int firstTokenCount = 0;					
					if(word_dict.containsKey(firstToken))
						firstTokenCount = word_dict.get(firstToken);

					String secondToken = tokens[i+1];
					if(secondToken.equalsIgnoreCase("-sified")){
						System.out.println("break 2");
					}
				
					int secondTokenCount = 0;

					if(word_dict.containsKey(secondToken))
						secondTokenCount = word_dict.get(secondToken);

					String bigramWithHyphenation = firstToken + secondToken;
					int hyBigramCount = 0;
					if(word_dict.containsKey(bigramWithHyphenation))
						hyBigramCount = word_dict.get(bigramWithHyphenation);


					String bigramWoHyphenation = firstToken.replaceAll("-+$","") + secondToken;
					
					int bigramCount = 0;
					if(word_dict.containsKey(bigramWoHyphenation))
						bigramCount = word_dict.get(bigramWoHyphenation);


					if(hyBigramCount > bigramCount){
						new_tokens.add(bigramWithHyphenation);

						String replacedKey = firstToken + secondToken + "->" + bigramWithHyphenation; 
						if(!replacedTokens.containsKey(replacedKey)){
							replacedTokens.put(replacedKey, 0);
						}
						replacedTokens.put(replacedKey, replacedTokens.get(replacedKey) + 1);

						i = i+1;
					} else if(bigramCount  > hyBigramCount){
						new_tokens.add(bigramWoHyphenation);

						String replacedKey = firstToken + secondToken + "->" + bigramWoHyphenation; 
						if(!replacedTokens.containsKey(replacedKey)){
							replacedTokens.put(replacedKey, 0);
						}
						replacedTokens.put(replacedKey, replacedTokens.get(replacedKey) + 1);

						i = i+1;
					} else {
						String hyphens_not_replacedKey = firstToken + secondToken; 
						if(!hyphens_not_replacedTokens.containsKey(hyphens_not_replacedKey)){
							hyphens_not_replacedTokens.put(hyphens_not_replacedKey, 0);
						}
						hyphens_not_replacedTokens.put(hyphens_not_replacedKey, hyphens_not_replacedTokens.get(hyphens_not_replacedKey) + 1);

						new_tokens.add(firstToken);					
					}
				}
			} else {
				new_tokens.add(firstToken);
			}
		}
		return new_tokens;
	}


	public static void main(String[] args) {		
		String uncleaned_saveTo = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/by_products/serialized_uncleaned_dict.dic";
		Map<String, Integer> uncleaned_word_dict = SerializationUtils.readObject(new File(uncleaned_saveTo));
				
		String saveTo = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/by_products/serialized_dict.dic";
		Map<String, Integer> word_dict = SerializationUtils.readObject(new File(saveTo));
		System.out.println("Total Words in the Dictionary:  " + word_dict.size());
		
		
		Map<String, Integer> new_word_dict = new HashMap<String, Integer>();
		String new_saveTo = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/by_products/hyphen_cleaned_serialized_dict.dic";

		Map<String, Integer> lower_new_word_dict = new HashMap<String, Integer>();
		String lower_new_saveTo = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/by_products/lower_hyphen_cleaned_serialized_dict.dic";

		String uncleaned_text_dir = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Cleaned_Uncompressed_Separated_Text/";
		String cleaned_text_dir = "/home/kat/Downloads/Anne/Corpus/span_period_years/5/Hyphen_Cleaned_Uncompressed_Separated_Text/";

		int fileNo = 0;

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
				
				String text = BasicFileTools.extractText(textFile, "\n ");

				List<String> new_tokens = bigram_hyphen_cleaning(text, word_dict);
				StringBuilder bld = new StringBuilder();

				for(String token : new_tokens){	
					token = token.trim();
					if(!new_word_dict.containsKey(token))
						new_word_dict.put(token, 0);					
					new_word_dict.put(token, new_word_dict.get(token)+1);

					if(!lower_new_word_dict.containsKey(token.toLowerCase()))
						lower_new_word_dict.put(token.toLowerCase(), 0);					
					lower_new_word_dict.put(token.toLowerCase(), lower_new_word_dict.get(token.toLowerCase())+1);

					bld.append(token + " ");
				}

				String cleanedTextFile = cleanedYearDir + "/" + textFile.getName();		
				BasicFileTools.writeFile(cleanedTextFile, bld.toString().trim());
			}
		}
		SerializationUtils.saveObject(new_word_dict, new File(new_saveTo));

		SerializationUtils.saveObject(lower_new_word_dict, new File(lower_new_saveTo));

		System.out.println("****************Replaced Token Map*********************************");
		replacedTokens = MapSort.sortByValue(replacedTokens);
		for(String key : replacedTokens.keySet()){
			Integer count = replacedTokens.get(key);
			if(count>20)
				System.out.println(key.trim() + " : " + count);			
		}
		System.out.println("*****************Not Replaced Token Map********************************");
		hyphens_not_replacedTokens = MapSort.sortByValue(hyphens_not_replacedTokens);
		for(String key : hyphens_not_replacedTokens.keySet()){
			Integer count = hyphens_not_replacedTokens.get(key);
			if(count > 20)
				System.out.println(key.trim() + " : " + count);			
		}
		System.out.println("*******************Other Info******************************");

		System.out.println("Total Words in the Raw Text Dictionary:  " + uncleaned_word_dict.size());				
		System.out.println("Total Words in the Regex Cleaned Text Dictionary:  " + word_dict.size());		
		System.out.println("Total Words in the Hyphen Cleaned Text Dictionary:  " + new_word_dict.size());
		System.out.println("Total Words in the Lowercased Hyphen Text Cleaned Dictionary:  " + lower_new_word_dict.size());

	}

	
}
