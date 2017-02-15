package parscit.xml;


import org.xml.sax.helpers.DefaultHandler;

public class XmlReaderTest {	
	public static String xml = "C:/Users/Kartik Asooja/Downloads/Parscit/Uncompressed_Separated/3/P/P92/P92-1047-parscit.130908.xml";

	public static void main(String[] args) {	
		ParscitFile file = new ParscitFile();
		DefaultHandler handler = new XmlHandler(file);
		XmlReader lReader = new XmlReader(xml);
		lReader.read(handler);
		System.out.println(file);
	}	

}