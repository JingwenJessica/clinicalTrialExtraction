package cwant.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;


public class Tools {
	public List<String> getAllURL( String url, String username, String password, String regex ){
		List<String> urlList = new ArrayList<String>();
		
		Document doc;
		try {
			doc = Jsoup.connect(url)
					.data("Username", username, "Password",   password)
					.post();
//			Elements elements = doc.select("table").select("a");
			Elements elements = doc.select("a");
			for( Element element : elements ){
				String thisURL = element.absUrl("href");
				
 				if( Pattern.matches(regex, thisURL) ){
					urlList.add( thisURL );
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urlList;
	}
//	public static void main( String [] args){
//				
//		try{   
//			
//	         // To connect to mongodb server
//			Mongo mongo = new Mongo("129.63.16.51", 27017);
//				
//	         // Now connect to your databases
//	         DB db = mongo.getDB( "ClinicalTrials" );
//	         System.out.println("[+] Connect to database successfully");
//				
//	         DBCollection coll = db.getCollection("clinicalTrialsGov");
//	         System.out.println("[+] Collection clinicalTrialsGov selected successfully");
//				
//	         int i = 0;
//	         BasicDBObject exist = new BasicDBObject("$exists", false);
//	         BasicDBObject query = new BasicDBObject("id", exist);
//	         DBCursor cursor = coll.find(query);
//	         while (cursor.hasNext()) {
//	     		DBObject obj = cursor.next();
//	     		
//	     		DBObject idInfo = (BasicDBObject) obj.get("id_info");
//	     	    String id = (String) idInfo.get("nct_id");
//	     	    System.out.println("[" + (i++) +"] " + id);
//	     	    
//	     	   BasicDBObject searchQuery = new BasicDBObject("id_info", idInfo);
//	     	   BasicDBObject newDocument = (BasicDBObject) obj;
//	     	   obj.put("id", id);
//	     	    
//	     	   coll.update(searchQuery, newDocument, true, false);
//	     	   
//	     	}
//				
//	      }catch(Exception e){
//	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//	      }
//	}
}
