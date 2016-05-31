package cwant.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import cwant.item.Config;

public class PostgreSQL {
	private Connection conn = null;;
	
	public PostgreSQL() throws IOException{
		Properties prop = new Config().getConfig();
		
		String 	ip 	 = prop.getProperty("postgres_ip");
		int 	port = Integer.parseInt(prop.getProperty("postgres_port"));
		String  db 	 = prop.getProperty("postgres_db");
		String 	coll = "postgres_coll";
		
		try {
			String connConf = "jdbc:postgresql://"+ ip +":"+ port +"/"+ db +"";
			
	         Class.forName("org.postgresql.Driver");
	         conn = DriverManager
	            .getConnection(connConf, "postgres", "123");
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
	}
	

	public void update(String query){
		Connection c = null;
	       Statement stmt = null;
	       try {
	         stmt = conn.createStatement();
	         String sql = query;
	         stmt.executeUpdate(sql);
	         stmt.close();
	       } catch ( Exception e ) {
	         System.err.println( e.getClass().getName()+": "+ e.getMessage() );
	         System.exit(0);
	       }
	}
	
	

	
}
