package parscit.xml;

public class BodyText {

	public String text = null;
	
	public BodyText(String text){
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}
