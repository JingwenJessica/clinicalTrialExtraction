package cwant.clinicalTrials;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cwant.network.WebContent;

public class Chictr {
	
	private WebContent webContent = new WebContent();

	public void saveTrials(String url, String trialURL) throws IOException {
		// all trials
		int i = 1;
		String tempUrl = url + "?page=" + i;
		Document content = webContent.getContent( tempUrl );
		while( content.select(".table_list") != null ){
			Elements elements = content.select(".table_list").select("a");
			for( Element e : elements ){
				String id = e.attr("href").replace("showprojen.aspx?proj=", "");
				String triURL = trialURL + id;
				System.out.print(triURL);
				Document doc = webContent.getContent(triURL);
				System.out.println( doc );
			}
		}
	}

}
