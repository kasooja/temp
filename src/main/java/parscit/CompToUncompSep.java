package parscit;

import java.io.File;

import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;

public class CompToUncompSep {

	public static String compressedDirPath = "C:/Users/Kartik Asooja/Downloads/Parscit/Compressed_Separated/";
	public static String uncompressedDirPath = "C:/Users/Kartik Asooja/Downloads/Parscit/Uncompressed_Separated/";

	public static void main(String[] args) {
		UnArchiver unarchiver = new TarGZipUnArchiver() {
			@Override
			protected Logger getLogger() {
				return new ConsoleLogger(Logger.LEVEL_DEBUG, "dependencies-unarchiver");
			}
		};
		File dir = new File(compressedDirPath);
		for(File classDir : dir.listFiles()){
			String uncompressedClassDirPath = uncompressedDirPath + "/" + classDir.getName();
			new File(uncompressedClassDirPath).mkdir();
			for(File file : classDir.listFiles()){				
				if(!file.isHidden()){						
					unarchiver.setSourceFile(file);							
					unarchiver.setDestDirectory(new File(uncompressedClassDirPath));
					unarchiver.extract();
					System.out.println("********************************************************************");				
				}
				//				ParscitFile pfile = new ParscitFile();
				//				DefaultHandler handler = new XmlHandler(pfile);
				//				XmlReader lReader = new XmlReader(file.getAbsolutePath());
				//				lReader.read(handler);
				//				System.out.println(pfile);
				//				System.out.println("********************************************************************");
			}
		}
	}

}
