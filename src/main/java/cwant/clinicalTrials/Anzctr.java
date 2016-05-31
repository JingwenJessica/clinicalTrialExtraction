package cwant.clinicalTrials;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import cwant.network.LoadFile;
import cwant.network.WebContent;
import cwant.db.MongoDB;
import cwant.item.Config;


public class Anzctr {
	
	private  String url = "";
	private WebContent webContent = new WebContent();
	private LoadFile loadFile = new LoadFile();
	
	private String id = null;
	private String trialXML = null;
	private JSONObject xmlJSONObj = null;
	
	
	
	
	
	
	public Anzctr() throws IOException{
		Properties prop = new Config().getConfig();
		url = prop.getProperty("clinicalTrialsGovURL");
	}
	
	
	
	public void saveTrials( Document content ) throws MalformedURLException{
		Elements listItems = content.select("item");
		int index = 0;
		for(Element item: listItems){
			String url = item.text();
			// clinical trial url
			String newurl = url.substring(0, url.lastIndexOf("?") ); 		//remove all parameters
			id = newurl.substring(newurl.lastIndexOf("/") + 1);	// get NCT_id
			newurl = newurl + "?resultsxml=true";
			System.out.println( "[" + index++ + "] " + newurl + "\n");
			
			// get trial xml
			try {
				String trialXML = webContent.getContent( newurl ).toString();
				JSONObject xmlJSONObj = XML.toJSONObject( trialXML );
				updateTrialsDB( id, xmlJSONObj );
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void saveTrials( File[] fileList ) throws IOException, JSONException, ParserConfigurationException{
		int count = 1;
		for (int i = 0; i < fileList.length; i++) {
	      if (fileList[i].isFile() && 
	    		  fileList[i].getName().contains(".xml") && 
	    		  fileList[i].getName().contains("ACTRN")) {
	    	  
		        id = fileList[i].getName().replace(".xml", "");
		        trialXML = loadFile.readFile(fileList[i].getPath(), StandardCharsets.UTF_8);
		        xmlJSONObj = XML.toJSONObject( trialXML );
				updateTrialsDB( id, xmlJSONObj );
				System.out.println("[" + count++ + "]: " + id );
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
	
	
	
	public void updateTrialsDB( String id, JSONObject xmlJSONObj ){
		MongoDB mongodb;
		String json;
		BasicDBObject dbObject;
		try {
			// connect mongodb
			mongodb = new MongoDB("anzctr");
            json = xmlJSONObj.get("ANZCTR_Trial").toString();
            dbObject = (BasicDBObject)JSON.parse(json);
            mongodb.updatebyId( id, dbObject, true );
            mongodb.close();
        } catch (JSONException je) {
            System.out.println(je.toString());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
