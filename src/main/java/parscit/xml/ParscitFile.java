package parscit.xml;

import java.util.List;

public class ParscitFile {

	public List<BodyText> texts;
	public String totalText;

	public ParscitFile() {
		
	}
	
	@Override
	public String toString() {
		return totalText;
	}
	
}
