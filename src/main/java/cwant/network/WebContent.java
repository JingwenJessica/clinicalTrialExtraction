package cwant.network;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;

public class WebContent {
	public void webContent(){
		
	}
	
	public Document getContent(String url) throws IOException{
		System.setProperty("jsse.enableSNIExtension", "false");
		
		Document doc = Jsoup.connect(url).timeout(0).ignoreHttpErrors(true).get();
		
		return doc;
	}
}
