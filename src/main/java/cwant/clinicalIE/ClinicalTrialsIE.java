package cwant.clinicalIE;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;

import cwant.clinicalTrials.Anzctr;
import cwant.clinicalTrials.Chictr;
import cwant.clinicalTrials.ClinicalTrialsGov;
import cwant.clinicalTrials.ICTRP;
import cwant.item.Config;

import cwant.network.LoadFile;
import cwant.network.WebContent;

public class ClinicalTrialsIE {
	private static Properties prop;
	private static  String url = "";
	private static WebContent webContent = new WebContent();
	
	private static String path = "";
	private static LoadFile loadData = new LoadFile();
	
	private static ClinicalTrialsGov clinicalTrialsGov;
	private static Anzctr anzctr;
	private static Chictr chictr;
	
//	final Logger logger = LogManager.getLogger(ClinicalTrialsIE.class);
	final static Logger logger = Logger.getLogger(ClinicalTrialsIE.class);
    
	/**
	 * clinical trials initial
	 * **/
	public ClinicalTrialsIE() throws IOException{
		
		prop = new Config().getConfig();
		
	}
	
	/**
	 * update clinicaltrials.gov trials from web
	 * **/
	public void clinicalTrialsGov_Web() throws IOException{
		url = prop.getProperty("clinicalTrialsGovURL");
		System.out.println("Time: " + System.currentTimeMillis() );
		System.out.println("url: " + url );
		// all trials
		for(int i=1; i<= 10651; i++){
			System.out.println("\n i = " + i + "\n");
//			logger.info("\n i = " + i + "\n");
			url = url + "?pg=" + i;
			Document content = webContent.getContent( url );
			if( content.select("item").isEmpty() ) break;
			// get and save
			clinicalTrialsGov = new ClinicalTrialsGov();
			clinicalTrialsGov.saveTrials( content );
		}
	}
	
	
	/**
	 * update clinicaltrials.gov trials from files
	 * 
	 * **/
	public static void clinicalTrialsGov_Folder() throws IOException, JSONException, ParserConfigurationException{
		path = prop.getProperty("clinicalTrialsGovPath");
		// all trials
		File[] fileList = loadData.getFileList(path);
		// get and save
		clinicalTrialsGov = new ClinicalTrialsGov();
		clinicalTrialsGov.saveTrials( fileList );
	}
	
	
	/**
	 * update anzctr.org trials from web
	 * **/
	public static void anzctr_Web() throws IOException{
		url = prop.getProperty("anzctrUrl");
		// all trials
		Document content = webContent.getContent( url );
		// get and save
		anzctr = new Anzctr();
		anzctr.saveTrials( content );
	}
	
	
	/**
	 * update anzctr.org trials from files
	 * 
	 * **/
	public static void anzctr_Folder() throws IOException, JSONException, ParserConfigurationException{
		path = prop.getProperty("anzctrPath");
		// all trials
		File[] fileList = loadData.getFileList(path);
		// get and save
		anzctr = new Anzctr();
		anzctr.saveTrials( fileList );
	}
	
	
	/**
	 * update chictr.org trials from web
	 * **/
	public static void chictr_Web() throws IOException{
		url = prop.getProperty("chictrURL");
		String trialURL = prop.getProperty("chictrXmlURL");
		
		// get and save
		chictr = new Chictr();
		chictr.saveTrials( url, trialURL );
	}
	
	
	/**
	 * update chictr.org trials from web
	 * **/
	private void WHO_Web() {
		url = prop.getProperty("ictrpURL");
		String username = prop.getProperty("ictrpUsername");
		String password = prop.getProperty("ictrpPassword");
		
		// get and save
		ICTRP who = new ICTRP();
		who.saveTrials( url, username, password );
	}
	
	
	public static void main( String [] args) throws IOException, JSONException, ParserConfigurationException{
		ClinicalTrialsIE ct = new ClinicalTrialsIE();
		
		// update clinicaltrials.gov from webpage
//		ct.clinicalTrialsGov_Web();
		
		// update clinicaltrials.gov from files
		//		clinicalTrialsGov_Folder();
		
		// update anzctr.org from files
//		anzctr_Folder();
		
		// update chictr.org from files
		// 不能爬取XML
//		chictr_Web(); 
		
		// update clinicaltrials.gov from webpage
		ct.WHO_Web();
	}

	
}
