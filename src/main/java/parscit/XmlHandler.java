package parscit;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlHandler extends DefaultHandler {

	private boolean takeText;
	private String tagStringValue = "";
	private List<BodyText> list = new ArrayList<BodyText>(); 
	private StringBuilder bld = new StringBuilder();
	private ParscitFile parscit;

	public XmlHandler(ParscitFile parscit){
		this.parscit = parscit;
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {		
		if (qName.equalsIgnoreCase("bodyText")) {
			takeText = true;
		}		
	}	

	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase("bodyText")){
			list.add(new BodyText(tagStringValue.trim()));
			bld.append(tagStringValue.trim() + "\n");
			takeText = false;
			tagStringValue = "";
		}
		if(qName.equalsIgnoreCase("algorithms")){
			parscit.texts = list;
			parscit.totalText = bld.toString().trim();
		}		
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		if (takeText) {
			tagStringValue = tagStringValue + new String(ch, start, length);			
		} 
	}

}