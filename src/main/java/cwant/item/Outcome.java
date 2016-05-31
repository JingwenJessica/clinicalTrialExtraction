package cwant.item;

import com.mongodb.BasicDBObject;

public class Outcome {
	private String text = "";
	private String time = "";
	
	
	public Outcome(){
		text = "";
		time = "";
	}
	
	public void clear(){
		text = "";
		time = "";
	}
	
	public void setText (String s){ text = s; }
	public void setTime (String s){ time = s; }
	
	// get
	public BasicDBObject getDBObject(){
		BasicDBObject dbObject = new BasicDBObject();
		
		dbObject.put("text", text);
		dbObject.put("time", time);
		
		return dbObject;
	}
}
