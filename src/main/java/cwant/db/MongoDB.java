package cwant.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;
import com.mongodb.*;
import cwant.item.Config;


public class MongoDB {
	private Mongo mongo;
	private DB db;
	private DBCollection collection;
	
	public MongoDB(String collName) throws IOException{
		Properties prop = new Config().getConfig();
		
		String 	mongo_ip 	= prop.getProperty("mongo_ip");
		int 	mongo_port 	= Integer.parseInt(prop.getProperty("mongo_port"));
		String 	mongo_db 	= prop.getProperty("mongo_db");
		String 	mongo_coll 	= collName;
		
		// connect to mongodb
		try {
			mongo = new Mongo(mongo_ip, mongo_port);
			db = mongo.getDB( mongo_db );
			collection = db.getCollection( mongo_coll );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	

	public void find(){
		this.collection.find();
	}
	
//	public List[] find(DBObject query, int limit){
//		List[] res;
//		DBCursor cursor = this.collection.find(query).limit(limit);
//		try {
//		   while(cursor.hasNext()) {
//		       res cursor.next());
//		   }
//		} finally {
//		   cursor.close();
//		}
//		
//	}
	
	public void insert(BasicDBObject query){
		this.collection.insert(query);
	}
	
	public WriteResult updatebyId(String id, BasicDBObject newDocument, boolean upsert){
		final BasicDBObject searchQuery = new BasicDBObject( "id", id.toString() );
//		searchQuery = new BasicDBObject().append("id", id); 
//		searchQuery = new BasicDBObject().append("id_info.nct_id", id); 
		newDocument.append( "id", id.toString() );
		newDocument.append( "cwant_updateTs", System.currentTimeMillis()/1000 );
		
		BasicDBObject updateQuery = new BasicDBObject("$set", newDocument);

		WriteResult res = collection.update(searchQuery, updateQuery, upsert, false); // muti = flase
		
		return res;
	}
	
	public void close(){
		mongo.close();
	}

	
}
