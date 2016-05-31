package cwant.item;

import com.mongodb.BasicDBObject;

public class Contact {
	private String name = "";
	private String title = "";
	private String address = "";
	private String country = "";
	private String phone = "";
	private String fax = "";
	private String email = "";
	private String affiliation = "";
	private String type = "";
	
	
	public Contact(){
		
	}
	
	public void clear(){
		name = "";
		title = "";
		address = "";
		country = "";
		phone = "";
		fax = "";
		email = "";
		affiliation = "";
		type = "";
	}
	
	public void setName (String s){ name = s; }
	public void setTitle(String s){ title = s; }
	public void setAddress(String s){ address = s; }
	public void setCountry(String s){ country = s; }
	public void setPhone( String s) { phone = s; }
	public void setFax (String s) { fax = s; }
	public void setEmail( String s){ email = s; }
	public void setAffiliation( String s){ affiliation = s; }
	public void setType( String s ){ type = s; }
	
	
	// get
	public BasicDBObject getDBObject(){
		BasicDBObject dbObject = new BasicDBObject();
		
		dbObject.put("name", name);
		dbObject.put("title", title);
		dbObject.put("address", address);
		dbObject.put("country", country);
		dbObject.put("phone", phone);
		dbObject.put("fax", fax);
		dbObject.put("email", email);
		dbObject.put("affiliation", affiliation);
		dbObject.put("type", type);
		
		return dbObject;
	}
	
}
