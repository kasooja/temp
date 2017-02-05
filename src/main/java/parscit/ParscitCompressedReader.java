package parscit;

import java.io.File;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.LogEnablePhase;
import org.xml.sax.helpers.DefaultHandler;

public class ParscitCompressedReader {

	public static String compressedDirPath = "C:/Users/Kartik Asooja/Downloads/Parcit/Compressed/";
	public static String uncompressedDirPath = "C:/Users/Kartik Asooja/Downloads/Parcit/Uncompressed/";

	public static void main(String[] args) {
		UnArchiver unarchiver = new TarGZipUnArchiver() {
			 @Override
		        protected Logger getLogger() {
		            return new ConsoleLogger(Logger.LEVEL_DEBUG, "dependencies-unarchiver");
		        }
		};
		File dir = new File(compressedDirPath);
		for(File file : dir.listFiles()){
			if(!file.isHidden()){						
				unarchiver.setSourceFile(file);				
				unarchiver.setDestDirectory(new File(uncompressedDirPath));
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
