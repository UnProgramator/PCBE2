package news;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class News {

	public String domeniu;
	public String src;
	public String data;
	public String body;
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
	
	public String toString(){
		return domeniu + "|" + src + "|" + data + "|" + body;
	}
	
	private News() {}
	
	public News(String domeniu, String src, Date data, String body) {
		this.domeniu = domeniu;
		this.src = src;
		this.data = dateFormat.format(data);
		this.body = body;
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
		retVal.data = src.substring(0, src.indexOf('|'));
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
					data.equals(n.data) &&
					body.equals(n.body);
					
		}
		return false;
	}
}
