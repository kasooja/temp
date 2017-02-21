package common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegexTokenizer {


	static String REGEX_TOKENIZER_1 = "(\\.\\.\\.+|[\\p{Po}\\p{Ps}\\p{Pe}\\p{Pi}\\p{Pf}\u2013\u2014\u2015&&[^'\\.]]|(?<!(\\.|\\.\\p{L}))\\.(?=[\\p{Z}\\p{Pf}\\p{Pe}]|\\Z)|(?<!\\p{L})'(?!\\p{L}))";
	static String REGEX_TOKENIZER_2 = "\\p{C}|^\\p{Z}+|\\p{Z}+$";
	static String REGEX_SPLITTER = "\\p{Z}+";

	static String BEG_END_PUNC_REGEX = "(^\\p{Punct}+)|([\\p{Punct}&&[^-]]+$)";
	static Pattern PUNC_REGEX = Pattern.compile(BEG_END_PUNC_REGEX);

	public static void main(String[] args) {

		//String text = "error correction technique.)";
		//String text = "et al. (1982) for a more thorough discussion of the";
		//String te = "_.papers";
		String k = "pro-.";
		//te = k;
		//String te = "proposition.";
		//String te = "'num=sg";
		String te = "_machine-translation";
		te = k;
		String text = "The " +
				"EM algorithm (Dempster, Laird, and Rubin, 1977) "+
				"used to train such &quot;hidden&quot; models requires us to "+
				"sum an expression over all possible alignments. "+
				"These early models were developed for French to "+
				"English translation. However, in NLU there is a fun- "+
				"damental asymmetry between the natural language. perform an inform act without the hearer  adopting the communicated proposition. 'num=sg _.papers 'anaphora   " + 
				"In addition to recognizing discourse ac-"+ 
				"tions, it is also necessary for a cooperative sys-"+ 
				"tem to recognize a user's changing beliefs as the"+
				"dialogue progresses. Allen's representation of an"+
				"Inform speech act (Allen, 1979) assumed that a "+
				"listener adopted the communicated proposition."+
				"Clearly, listeners do not adopt everything they "+
				"are told (e.g., (3) indicates that Si does not im-"+ 
				"mediately accept that Dr. Smith is teaching Ar-"+ 
				"chitecture)." + 
				"This allows us to differen-"+ 
				"tiate between illocutionary and perlocutionary ef-"+
				"fects and to capture the notion that one can, for"+ 
				"example, perform an inform act without the hearer"+ 
				"adopting the communicated proposition.' ";
		text = te;

		System.out.println("Original: " + text);

		//text = remove_punc_beg_end(text);
		//System.out.println(clean_text(text));
		List<String> overallTokens = tokenize_token(text);		
		for(String to : overallTokens){			
				System.out.println(to);
		}
		//		if(isreqdAlphaNumOrPunc(text)){
		//			System.out.println("Selected: " + text);
		//		} else {
		//			System.out.println("Not selected");
		//		}
		//
		//		System.out.println("******************************");		
		//		if(isreqdAlphaNumOrPunc(text)){
		//			System.out.println("Selected: " + text);
		//		} else {
		//			text = remove_punc_beg_end(text);
		//			System.out.println(clean_text(text));
		//
		//			if(isreqdAlphaNumOrPunc(text)){
		//				System.out.println("Selected: " + text);
		//			} else {
		//				System.out.println("Not selected");
		//			}
		//		} 
		//		System.out.println(isreqdAlphaNumOrPunc(text));
		//		text = remove_punc_beg_end(text);
		//		System.out.println(text);
		//		System.out.println(isreqdAlphaNumOrPunc(text));
		//		System.out.println("uncleaned: " + text);
		//		text = clean_text(text);
		//		System.out.println("cleaned: " + text);
		//		for(String token : split_text(text)){
		//			System.out.println(token);
		//			System.out.println("After cleaning punc: ");
		//			System.out.println(token.replaceAll("^\\p{Punct}+|\\p{Punct}+$", ""));
		//		}
		//System.out.println(split_text(text));


	}

	public static List<String> tokenize_token(String token){
		Matcher matcher = PUNC_REGEX.matcher(token);
		List<String> overallTokens = new ArrayList<String>();
	
		if(matcher.find()) {
			String beg = matcher.group(1);
			String end = matcher.group(2);
			if(beg!=null){
				for(char ch : beg.toCharArray()){
					if(isreqdAlphaNumOrPunc(String.valueOf(ch)))
						overallTokens.add(String.valueOf(ch));
				}
			}
			overallTokens.add(remove_punc_beg_end(token));
			if(end!=null){
				for(char ch : end.toCharArray()){				
					if(isreqdAlphaNumOrPunc(String.valueOf(ch)))
						overallTokens.add(String.valueOf(ch));
				}
			}		
		} else {
			overallTokens.add(token);
		}
		return overallTokens;
	}
	
	public static String remove_punc_beg_end(String token){
		return token.replaceAll(BEG_END_PUNC_REGEX, "");
	}

	public static boolean isPunc(String token){
		return token.matches("\\p{Punct}");
	}

	public static boolean isreqdAlphaNumOrPunc(String token){
		return isalphanumeric(token) || isPunc(token);
	}

	public static boolean isalphanumeric(String token){
		return isAlphanumericEmptyUndScoreHyphen(token);
	}

	public static boolean isAlphanumericEmptyUndScoreHyphen(final CharSequence cs) {
		if (isEmpty(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isLetterOrDigit(cs.charAt(i)) == false && cs.charAt(i) != '-' && cs.charAt(i) != '_') {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
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
