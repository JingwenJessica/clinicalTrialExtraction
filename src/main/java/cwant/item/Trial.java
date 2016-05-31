package cwant.item;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;

import cwant.tools.TimestampExtractor;

/**
 * 
 * Recruitment Status: 
 * 		Recruiting
 * 		Closed: follow-up complete 
 * 		Completed
 * Study Type:
 *  	Interventional
 *  
 * **/
public class Trial {
	private String register = "";
	private String id = "";
	private List<String> secondaryId = new ArrayList<String>();
	private String url = "";
	private String title = "";
	private String acronym = "";
	private String scientificTitle = "";
	
	private String enrollmentDate = "";
	private long enrollmentTs;
	private String updateDate = "";
	private long updateTs;
//	private String cwantUpdateDate = "";
//	private long cwantUpdateTs;
	private String registrationDate = "";
	private long registrationTs;
	
	private String primarySponsor = "";
	private String secondarySponsor = "";
	
	private int targetSampleSize = 0;	// Target sample size:
	private String recruitmentStatus = "";
	private List<String> recruitmentCountries = new ArrayList<String>();
	
	private String studyType = "";
	private String studyDesign = "";
	private String allocation = "";
	private String assignement = "";
	private String phase = "";
	
	private List<Contact> contacts = new ArrayList<Contact>();
	
	private String healthCondition = "";
	private String intervention = "";
	
	private List<Outcome> primaryOutcome = new ArrayList<Outcome>();
	private List<Outcome> secondaryOutcome = new ArrayList<Outcome>();
	
	private String funding = "";
	
	
	public Trial(){
		
	}
	
	// set
	public void setRegister(String s) { register = s;}
	public void setId ( String s ){ id = s; }
	public void addSecondaryId ( String s){ secondaryId.add(s); }
	public void setUrl( String s){ url = s; }
	public void setTitle( String s ){ title = s; }
	public void setAcronym( String s){ acronym = s; }
	public void setScientificTitle( String s){ scientificTitle = s; }
	
	public void setEnrollmentDate( String s ){
		enrollmentDate = s;
//		enrollmentTs = TimestampExtractor.transFromNormalDateToUnixDate(s, transPatten);
	}
	public void setUpdateDate( String s ){
		updateDate = s;
	}
//	public void setCwantUpdateDate( String s ){
//		cwantUpdateDate = s;
//	}
	public void setRegistrationDate( String s ){
		registrationDate = s;
	}
	
	public void setPrimarySponsor( String s ){ primarySponsor = s; }
	public void setSecondarySponsor( String s ){ secondarySponsor = s; }
	
	public void setTargetSampleSize( int i ){ targetSampleSize = i; }
	
	public void setRecruitmentStatus( String s ){ recruitmentStatus = s; }
	public void addRecruitmentCountry( String s ){ recruitmentCountries.add( s ); }
	
	public void setStudyType( String s){ studyType = s; }
	public void setStudyDesign( String s){ studyDesign = s; }
	public void setAllocation( String s){ allocation = s; }
	public void setAssignement( String s){ assignement = s; }
	public void setPhase( String s ){ phase = s; }
	
	public void addContact(Contact s){ 
		contacts.add( s );
	}
	
	public void setHealthCondition( String s ){ healthCondition = s; }
	public void setIntervention( String s ){ intervention = s; }
	public void addPrimaryOutcome( Outcome s ){ primaryOutcome.add(s); }
	public void addSecondaryOutcome( Outcome s){ secondaryOutcome.add(s); }
	
	public void setFunding( String s ){ funding = s; }
	
	
	
	// get
	public String getId(){ return id; }
	
	
	
	// 
	public BasicDBObject getDBObject(){
		BasicDBObject dbObject = new BasicDBObject();
		
		dbObject.put("id", id);
		dbObject.put("register", register);
		dbObject.put("secondaryId", secondaryId);
		dbObject.put("url", url);
		dbObject.put("title", title);
		dbObject.put("acronym", acronym);
		dbObject.put("scientificTitle", scientificTitle);
		dbObject.put("enrollmentDate", enrollmentDate);
		dbObject.put("enrollmentTs", enrollmentTs);
		dbObject.put("updateDate", updateDate);
		dbObject.put("updateTs", updateTs);
//		dbObject.put("cwantUpdateDate", cwantUpdateDate);
//		dbObject.put("cwantUpdateTs", cwantUpdateTs);
		dbObject.put("registrationDate", registrationDate);
		dbObject.put("registrationTs", registrationTs);
		dbObject.put("primarySponsor", primarySponsor);
		dbObject.put("secondarySponsor", secondarySponsor);
		dbObject.put("targetSampleSize", targetSampleSize);
		dbObject.put("recruitmentStatus", recruitmentStatus);
		dbObject.put("recruitmentCountries", recruitmentCountries);
		dbObject.put("studyType", studyType);
		dbObject.put("studyDesign", studyDesign);
		dbObject.put("allocation", allocation);
		dbObject.put("assignement", assignement);
		dbObject.put("phase", phase);
		dbObject.put("healthCondition", healthCondition);
		dbObject.put("intervention", intervention);
		
		List<BasicDBObject>  temp = new ArrayList<BasicDBObject>(); 
		for(Contact contact : contacts){
			temp.add( contact.getDBObject() );
			
		} 
		dbObject.put("contacts",  temp);
		
		temp = new ArrayList<BasicDBObject>(); 
		for(Outcome outcome : primaryOutcome){
			temp.add( outcome.getDBObject() );
			
		} 
		dbObject.put("primaryOutcome",  temp);
		
		temp = new ArrayList<BasicDBObject>(); 
		for(Outcome outcome : secondaryOutcome){
			temp.add( outcome.getDBObject() );
			
		} 
		dbObject.put("secondaryOutcome", temp );
		
		dbObject.put("funding", funding);
		
		return dbObject;
	}
	
	
}
