package cwant.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TimestampExtractor {

	/** 
	 * 中文
	 * (1)能匹配的年月日类型有： 
	 *    2014年4月19日 
	 *    2014年4月19号 
	 *    2014-4-19 
	 *    2014/4/19 
	 *    2014.4.19 
	 * (2)能匹配的时分秒类型有： 
	 *    15:28:21 
	 *    15:28 
	 *    5:28 pm 
	 *    15点28分21秒 
	 *    15点28分 
	 *    15点 
	 * (3)能匹配的年月日时分秒类型有：(1)和(2)的任意组合，二者中间可有任意多个空格 
	 * 如果dateStr中有多个时间串存在，只会匹配第一个串，其他的串忽略 
	 * @param text 
	 * @return 
	 */  
	
	public static String matchDateString(String html) {
		
		Document doc;
		doc = Jsoup.parse(html);
		String dateStr = doc.text();
		
	       try {  
	           List matches = null;  
	           Pattern p = Pattern.compile("(\\d{1,4}[-|\\/|年|\\.]\\d{1,2}[-|\\/|月|\\.]\\d{1,2}([日|号])?(\\s)*(\\d{1,2}([点|时])?((:)?\\d{1,2}(分)?((:)?\\d{1,2}(秒)?)?)?)?(\\s)*(PM|AM)?)", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);  
	           Matcher matcher = p.matcher(dateStr);  
	           if (matcher.find() && matcher.groupCount() >= 1) {  
	               matches = new ArrayList();  
	               for (int i = 1; i <= matcher.groupCount(); i++) {  
	                   String temp = matcher.group(i);  
	                   matches.add(temp);  
	               }  
	           } else {  
	               matches = Collections.EMPTY_LIST;  
	           }             
	           if (matches.size() > 0) {  
	               return ((String) matches.get(0)).trim();  
	           } else {  
	           }  
	       } catch (Exception e) {  
	           return "";  
	       }  
	         
	    return dateStr;  
	   }  

/*
 * 函数化 可判断几天几天的那种
 */
//String time = doc.select("span.r-10").text();
//
//if(time.contains("天")){
//	 
//	Pattern p = Pattern.compile("(\\d+)");   
//	Matcher m = p.matcher(time);   
//    String dayCount = "0";
//	while(m.find())
//    {               
//		dayCount = m.group(1);
//        System.out.println(dayCount+"day");
//        
//    }  
//	
//	long currentTime = System.currentTimeMillis();
//	long newsTime = currentTime/1000 - Integer.parseInt(dayCount)*24*60*60;
//	time = Long.toString(newsTime);
//}else if (time.contains("小时")){
//	Pattern p = Pattern.compile("(\\d+)");   
//	Matcher m = p.matcher(time);   
//    String hourCount = "0";
//	while(m.find())
//    {               
//		hourCount = m.group(1);
//        System.out.println(hourCount+"hour");
//    }  
//	long currentTime = System.currentTimeMillis();
//	long newsTime = currentTime/1000 - Integer.parseInt(hourCount)*60*60;
//	time = Long.toString(newsTime);
//}else {
//	time = matchDateString(doc.toString());
//	String transPatten = "yyyy/MM/dd";
//	time =transFromNormalDateToUnixDate(time,transPatten);
//	
//}



	/**
	 * English Date to ts
	 * transPatten = "yyyy-MM-dd HH:mm:ss";
	 * @param normalDate
	 * @param transPatten
	 * @return
	 */
	public static String transFromNormalDateToUnixDate(String normalDate,String transPatten){
	
	String unixTimeDate = null;
	 SimpleDateFormat myFmt=new SimpleDateFormat(transPatten);
	try {
		Date unixtime = myFmt.parse(normalDate);
		long unixstamp = unixtime.getTime()/1000;
		unixTimeDate = Long.toString(unixstamp);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return unixTimeDate;
	}
}
