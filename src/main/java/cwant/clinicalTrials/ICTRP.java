package cwant.clinicalTrials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import cwant.db.MongoDB;
import cwant.item.Trial;
import cwant.item.Contact;
import cwant.item.Outcome;
import cwant.tools.Tools;


public class ICTRP {
	final Logger logger = Logger.getLogger(ICTRP.class);
	
	public void saveTrials( String url, String username, String password ) {
		
		String regex = "http://\\w+.who.int/trialsearch/crawl/crawl\\d+.aspx";
		List<String> levOneURL = new Tools().getAllURL( url, username, password, regex );
		
		for( String thisURL : levOneURL ){
			System.out.println( "[.] " + thisURL );
			regex = "http://\\w+.who.int/trialsearch/Trial3.aspx.trialid=.*";
			List<String> levTwoURL = new Tools().getAllURL( thisURL, username, password, regex);
			for(String trialURL : levTwoURL){
				System.out.println( "[...] " + trialURL );
				Trial trial = getTrial( trialURL, username, password );
			}
			
			
		}
	}
	
	public Trial getTrial( String url, String username, String password ) {
		Document doc;
		Trial trial = new Trial();
		String label = "";
		
		int i = 0;
		try {
			doc = Jsoup.connect(url)
					.data("Username", username, "Password",   password)
					.post();
			
			String temp = doc.html().replace("<br>", "###"); //$$$ instead <br>
			doc = Jsoup.parse(temp); //Parse again

			
			trial.setRegister( doc.select("#DataList3_ctl01_DescriptionLabel").text() );
			trial.setId( doc.select("#DataList3_ctl01_TrialIDLabel").text() );
			
			// secondary id
			i=1;
			label = "#DataList16_ctl0" + i++ + "_SecondaryIDLabel";
			while( !doc.select(label).isEmpty() ){
				trial.addRecruitmentCountry( doc.select(label).text() );
				label = "#DataList16_ctl0" + i++ + "_SecondaryIDLabel";
			}
			
			
			trial.setUpdateDate( doc.select("#DataList3_ctl01_Last_updatedLabel").text() );
			trial.setRegistrationDate( doc.select("#DataList3_ctl01_Date_registrationLabel").text() );
			trial.setPrimarySponsor( doc.select("#DataList3_ctl01_Primary_sponsorLabel").text() );
			trial.setSecondarySponsor( doc.select("#DataList20_ctl01_Secondary_SponsorLabel").text() );
			trial.setTitle( doc.select("#DataList3_ctl01_Public_titleLabel").text() );
			trial.setAcronym( doc.select("#DataList3_ctl01_AcronymLabel").text() );
			trial.setScientificTitle( doc.select("#DataList3_ctl01_Scientific_titleLabel").text() );
			trial.setEnrollmentDate( doc.select("#DataList3_ctl01_Date_enrollementLabel").text() );
			trial.setTargetSampleSize( Integer.parseInt(doc.select("#DataList3_ctl01_Target_sizeLabel").text()) );
			trial.setRecruitmentStatus( doc.select("#DataList3_ctl01_Recruitment_statusLabel").text() );
			trial.setUrl( doc.select("#DataList3_ctl01_HyperLink12").text() );
			trial.setStudyType( doc.select("#DataList3_ctl01_Study_typeLabel").text() );
			trial.setStudyDesign( doc.select("#DataList3_ctl01_Study_designLabel").text() );
			trial.setAllocation( doc.select("#DataList3_ctl01_AllocationLabel").text() );
			trial.setAssignement( doc.select("#DataList3_ctl01_AssignementLabel").text() );
			trial.setPhase( doc.select("#DataList3_ctl01_PhaseLabel").text() );
			
			// recruitment countries
			i=1;
			label = "#DataList2_ctl0" + i++ + "_Country_Label";
			while( !doc.select(label).isEmpty() ){
				trial.addRecruitmentCountry( doc.select(label).text() );
				label = "#DataList2_ctl0" + i++ + "_Country_Label";
			}
			
			
			// contacts
			Contact contact = new Contact();
			i=1;
			label = "#DataList4_ctl0" + i++ ;
			while( !doc.select(label + "_LastnameLabel").isEmpty() ){
//				System.out.println( doc.select(label + "_LastnameLabel") );
				contact.setName( doc.select(label + "_LastnameLabel").text() );
				contact.setAddress( doc.select(label + "_AddressLabel").text() );
				contact.setPhone( doc.select(label + "_TelephoneLabel").text() );
				contact.setEmail( doc.select(label + "_EmailLabel").text() );
				contact.setAffiliation( doc.select(label + "_AffiliationLabel").text() );
				trial.addContact( contact );
				contact.clear();
				label = "#DataList4_ctl0" + i++ ;
			}
			
			trial.setHealthCondition( doc.select("#DataList8_ctl01_Condition_FreeTextLabel").text() );
			trial.setIntervention( doc.select("#DataList10_ctl01_Intervention_FreeTextLabel").text() );
			
			// primary outcome
			Outcome outcome = new Outcome();
			i=1;
			label = "#DataList14_ctl0" + i++ ;
			while( !doc.select(label + "_Outcome_NameLabel").isEmpty() ){
				outcome.setText( doc.select("#DataList12_ctl01_Outcome_NameLabel").text() );
				outcome.setTime( doc.select("#DataList14_ctl01_Time_Frame2Label").text() );
				trial.addPrimaryOutcome( outcome );
				outcome.clear();
				label = "#DataList4_ctl0" + i++ ;
			}
			
			
			// secondary outcome
			outcome.clear();
			i=1;
			label = "#DataList14_ctl0" + i++ ;
			while( !doc.select(label + "_Outcome_NameLabel").isEmpty() ){
				outcome.setText( doc.select(label + "_Outcome_NameLabel").text() );
				outcome.setTime( doc.select(label + "_Time_Frame2Label").text() );
				trial.addSecondaryOutcome( outcome );
				outcome.clear();
				label = "#DataList4_ctl0" + i++ ;
			}
			
			// funding
			trial.setFunding( doc.select("#DataList18_ctl01_Source_NameLabel").text() );
			
			updateTrialsDB( trial );
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return trial;
	}
	
	public void updateTrialsDB( Trial trial ){
		MongoDB mongodb;
		String json;
		BasicDBObject dbObject = new BasicDBObject();
		try {
			// connect mongodb
			mongodb = new MongoDB("ictrp");
            dbObject = trial.getDBObject();
            mongodb.updatebyId( trial.getId(), dbObject, true );
            mongodb.close();
            
            // wait for 1 second
            try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        } catch (JSONException je) {
            System.out.println(je.toString());
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
