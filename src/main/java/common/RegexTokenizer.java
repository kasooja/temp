package common;

public class RegexTokenizer {


	static String REGEX_TOKENIZER_1 = "(\\.\\.\\.+|[\\p{Po}\\p{Ps}\\p{Pe}\\p{Pi}\\p{Pf}\u2013\u2014\u2015&&[^'\\.]]|(?<!(\\.|\\.\\p{L}))\\.(?=[\\p{Z}\\p{Pf}\\p{Pe}]|\\Z)|(?<!\\p{L})'(?!\\p{L}))";
	static String REGEX_TOKENIZER_2 = "\\p{C}|^\\p{Z}+|\\p{Z}+$";
	static String REGEX_SPLITTER = "\\p{Z}+";

	public static void main(String[] args) {
		//String text = "error correction technique.)";
		//String text = "et al. (1982) for a more thorough discussion of the";
		String text = "The " +
 "EM algorithm (Dempster, Laird, and Rubin, 1977) "+
 "used to train such &quot;hidden&quot; models requires us to "+
 "sum an expression over all possible alignments. "+
 "These early models were developed for French to "+
 "English translation. However, in NLU there is a fun- "+
 "damental asymmetry between the natural language ";
		System.out.println("uncleaned: " + text);
		text = clean_text(text);
		System.out.println("cleaned: " + text);
		for(String token : split_text(text)){
			System.out.println(token);
		}
		//System.out.println(split_text(text));		
	}


	public static String clean_text(String text){
		text = text.replaceAll(REGEX_TOKENIZER_1, " $1 ");
		text = text.replaceAll(REGEX_TOKENIZER_2, "");
		return text;	
	}

	public static String[] split_text(String text){
		return text.split(REGEX_SPLITTER);		
	}

}
