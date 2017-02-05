package parscit;

import java.io.File;

import org.xml.sax.helpers.DefaultHandler;

public class ParscitDirReader {
	public static String dirPath = "C:/Users/Kartik Asooja/Downloads/Parcit/A/A00/";

	public static void main(String[] args) {
		File dir = new File(dirPath);
		for(File file : dir.listFiles()){
			if(!file.isHidden()){						
				ParscitFile pfile = new ParscitFile();
				DefaultHandler handler = new XmlHandler(pfile);
				XmlReader lReader = new XmlReader(file.getAbsolutePath());
				lReader.read(handler);
				System.out.println(pfile);
				System.out.println("********************************************************************");
			}
		}
	}
	
}
