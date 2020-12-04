package news;

import java.text.ParseException;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class NewsTestCase {
	
	private News testObj;
	private Date crtDate;
	
	@BeforeEach
	public void before() {
		crtDate = new Date();
		testObj = new News(
				"Test Domeniu",
				"Test Source",
				crtDate,
				"Test Body"
				);
	}
	
	@Test
	public void testToString() {
		String actual = "Test Domeniu" + "|" +
						"Test Source" + "|" + 
						News.dateFormat.format(crtDate)+ "|" + "Test Body";
		Assertions.assertEquals(testObj.toString(), actual);
	}
	
	@Test
	public void testFromString() throws ParseException {
		String strFormat = testObj.toString();
		Assertions.assertEquals(News.fromString(strFormat), testObj);

	}

}
