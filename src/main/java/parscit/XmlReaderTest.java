package parscit;


import org.xml.sax.helpers.DefaultHandler;

public class XmlReaderTest {	
	public static String xml = "C:/Users/Kartik Asooja/Downloads/Parcit/A/A00/A00-1000-parscit.130908.xml";

	public static void main(String[] args) {	
		ParscitFile file = new ParscitFile();
		DefaultHandler handler = new XmlHandler(file);
		XmlReader lReader = new XmlReader(xml);
		lReader.read(handler);
		System.out.println(file);
	}	

}