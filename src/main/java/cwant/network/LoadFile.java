package cwant.network;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import cwant.item.Config;

public class LoadFile {

	private String path = "";
	private File folder;
	File[] fileList;
	byte[] encoded;
	private String content;
	
	public LoadFile(){
		Properties prop = null;
		try {
			prop = new Config().getConfig();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		path = prop.getProperty("clinicalTrialsGovPath");
	}
	
	public File[] getFileList(String path){
		folder = new File( path );
		fileList = folder.listFiles();
		return fileList;
	}
	
	
	public String readFile(String path, Charset encoding) throws IOException 
	{
		encoded = Files.readAllBytes(Paths.get(path));
		content = new String(encoded, encoding);
		return content;
		
	}
	
}
