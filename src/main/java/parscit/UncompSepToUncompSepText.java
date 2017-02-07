package parscit;

import java.io.File;

import org.xml.sax.helpers.DefaultHandler;

import common.BasicFileTools;
import parscit.xml.ParscitFile;
import parscit.xml.XmlHandler;
import parscit.xml.XmlReader;

public class UncompSepToUncompSepText {
	public static String dirPath = "C:/Users/Kartik Asooja/Downloads/Parscit/Uncompressed_Separated/";
	public static String destDirPath = "C:/Users/Kartik Asooja/Downloads/Parscit/Uncompressed_Separated_Text/";

	public static void main(String[] args) {
		File dir = new File(dirPath);
		for(File classDir : dir.listFiles()){
			if(classDir.isHidden()){
				continue;
			}
			String className = classDir.getName();
			for(File confDir : classDir.listFiles()){
				if(confDir.isHidden()){
					continue;
				}
				String destClassDirPath = destDirPath + "/" + className;
				if(!new File(destClassDirPath).exists()) {
					new File(destClassDirPath).mkdir();
				}
				for(File confYearDir : confDir.listFiles()){
					if(confYearDir.isHidden()){
						continue;
					}				
					for(File file : confYearDir.listFiles()){
						if(file.isHidden()){
							continue;
						}
						System.out.println(file.getAbsolutePath());						
						System.out.println(file.getName());
						String destFilePath = destClassDirPath + "/" + file.getName() + ".txt";
						ParscitFile pfile = new ParscitFile();
						DefaultHandler handler = new XmlHandler(pfile);
						XmlReader lReader = new XmlReader(file.getAbsolutePath());
						lReader.read(handler);
					//	System.out.println(pfile);
					
						BasicFileTools.writeFile(destFilePath, pfile.toString());
						System.out.println("********************************************************************");
					}
				}
			}
		}
	}

}
