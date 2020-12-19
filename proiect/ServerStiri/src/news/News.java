package news;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class News {

	public String domeniu;
	public String titlu;
	public String src;
	public String publishData;
	public String lastChangedData;
	public String body;
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
	
	public String toString(){
		return domeniu + "|" + src + "|" + publishData + "|" + lastChangedData + "|" + titlu + "|" + body;
	}
	
	private News() {}
	
	public News(String domeniu, String src, Date data, String titlu, String body) { 
		this.domeniu = domeniu;
		this.src = src;
		this.lastChangedData = this.publishData = dateFormat.format(data);
		this.titlu = titlu;
		this.body = body;
	}
	
	public void changeContent(String newContent, Date newDate) {
		this.body = newContent;
		this.lastChangedData = dateFormat.format(newDate);
	}
	
	public String getHead() {
		return domeniu;
	}
	
	public static News fromString(String src) {
		News retVal = new News();
		//System.out.print(src);
		retVal.domeniu = src.substring(0, src.indexOf('|'));
		src = src.substring(src.indexOf('|')+1);
		
		//System.out.print(src);
		retVal.src = src.substring(0, src.indexOf('|'));
		src = src.substring(src.indexOf('|')+1);
		
		//System.out.print(src);
		retVal.publishData = src.substring(0, src.indexOf('|'));
		src = src.substring(src.indexOf('|')+1);
		
		retVal.lastChangedData = src.substring(0, src.indexOf('|'));
		src = src.substring(src.indexOf('|')+1);
		
		retVal.titlu = src.substring(0, src.indexOf('|'));
		src = src.substring(src.indexOf('|')+1);
		
		//System.out.print(src);
		retVal.body = src;
		
		return retVal;
	}
	
	public boolean equals(Object o) {
		if(o instanceof News) {
			News n = (News)o;
			return  domeniu.equals(n.domeniu) &&
					src.equals(n.src) &&
					publishData.equals(n.publishData) &&
					lastChangedData.equals(n.lastChangedData) &&
					titlu.equals(n.titlu) &&
					body.equals(n.body);
					
		}
		return false;
	}
}
