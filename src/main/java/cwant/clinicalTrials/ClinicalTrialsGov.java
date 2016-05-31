package cwant.clinicalTrials;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.log4j.Logger;
import org.json.*;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import cwant.network.LoadFile;
import cwant.network.WebContent;
import cwant.clinicalIE.ClinicalTrialsIE;
import cwant.db.MongoDB;
import cwant.item.Config;
//import cwant.tools.Logging;

public class ClinicalTrialsGov {
	
	private  String url = "";
	private WebContent webContent = new WebContent();
	private LoadFile loadFile = new LoadFile();
	
//	private String nctId = null;
	private String trialXML = null;
	private JSONObject xmlJSONObj = null;
	
	final Logger logger = Logger.getLogger(ClinicalTrialsIE.class);

	
	
	public ClinicalTrialsGov() throws IOException{
		// logging
//		Logging.setup();
		
		Properties prop = new Config().getConfig();
		url = prop.getProperty("clinicalTrialsGovURL");
	}
	
	
	
	public void saveTrials( Document content ) throws MalformedURLException{
		Elements listItems = content.select("item");
		int index = 0;
		for(Element item: listItems){
//			String url = item.select("link").text();
			String url = item.select("link").get(0).nextSibling().toString();
			// clinical trial url
			if(url.contains("?")){
				url = url.substring(0, url.lastIndexOf("?") ); 		//remove all parameters
			}
			
			String nctId = url.substring(url.lastIndexOf("/") + 1);	// get NCT_id
			url = url + "?resultsxml=true";
			System.out.println( "[" + index + "] " + url);
			logger.info( "[" + index + "] " + url);
			index++;
			
			// get trial xml
			try {
				String trialXML = webContent.getContent( url ).select("clinical_study").toString();
				JSONObject xmlJSONObj = XML.toJSONObject( trialXML );
				updateTrialsDB( nctId, xmlJSONObj );
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void saveTrials( File[] fileList ) throws IOException, JSONException, ParserConfigurationException{
		int count = 1;
		for (int i = 0; i < fileList.length; i++) {
	      if (fileList[i].isFile() && fileList[i].getName().contains("NCT0272")) {
		        String nctId = fileList[i].getName().replace(".xml", "");
		        trialXML = loadFile.readFile(fileList[i].getPath(), StandardCharsets.UTF_8);
		        xmlJSONObj = XML.toJSONObject( trialXML );
				updateTrialsDB( nctId, xmlJSONObj );
				System.out.println("[" + count++ + "]: " + nctId );
//				try {
//				    TimeUnit.SECONDS.sleep(1);
//				} catch (InterruptedException e) {
//				    //Handle exception
//				}
		      } else if (fileList[i].isDirectory()) {
		        System.out.println("Directory " + fileList[i].getName());
		      }
		    }
	}
	
	
	
	public void updateTrialsDB( String nctId, JSONObject xmlJSONObj ){
		MongoDB mongodb;
		String json;
		BasicDBObject dbObject;
		try {
			// connect mongodb

			mongodb = new MongoDB("clinicalTrialsGov");
            json = xmlJSONObj.get("clinical_study").toString();
            dbObject = (BasicDBObject)JSON.parse(json);
            mongodb.updatebyId( nctId, dbObject, true );
            mongodb.close();
        } catch (JSONException je) {
            System.out.println(je.toString());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
