package JSF.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/** Test cases for the DateUtils class
 *
 * @author samuel
 */
public class DateUtilsTest {

    /**
     * Test of getDateLastWeek method, of class DateUtils.
     */
    @Test
    public void testGetDateLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-6);
        Date expected = cal.getTime();

        Date result = DateUtils.getDateLastWeek();
        assertTrue(datesHaveSameDay(expected, result));
    }

    /**
     * Test of addDaysToDate method, of class DateUtils.
     */
    @Test
    public void testAddDaysToDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)+10);
        Date expected = cal.getTime();

        Date result = DateUtils.addDaysToDate(new Date(), 10);
        assertTrue(datesHaveSameDay(expected, result));
    }

    /**
     * Test of getDateLastMonth method, of class DateUtils.
     */
    @Test
    public void testGetDateLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date expected = cal.getTime();
        
        Date result = DateUtils.getDateLastMonth();
        assertTrue(datesHaveSameDay(expected, result));
    }

    /**
     * Test of convertDateToString method, of class DateUtils.
     * @throws java.text.ParseException
     */
    @Test
    public void testConvertDateToString() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date date = df.parse("28/11/2015");
        
        String result = DateUtils.convertDateToString(date);
        assertEquals("28/11/2015 12:00:00", result);
    }
    
    /** Check that two dates share the same day.
     * 
     * Code taken from: http://stackoverflow.com/questions/2517709
     * 
     * @param a first date
     * @param b second date
     * @return whether they share the same day
     */
    private boolean datesHaveSameDay(Date a, Date b) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    
}
