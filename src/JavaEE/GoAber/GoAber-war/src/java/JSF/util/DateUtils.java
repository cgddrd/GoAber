
package JSF.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** Helper class for various date utility functions
 *
 * @author samuel
 */
public class DateUtils {
    /**
     * Get the date one week prior to today
     * @return Date object for one week prior.
     */
    public static Date getDateLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // substract 7 days
        // If we give 7 there it will give 8 days back
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-6);
        return cal.getTime();
    }
    
    /**
     * Get the date one month prior to today
     * @return Date object for one month prior.
     */
    public static Date getDateLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /** Converts a Java date object to a string
     * 
     * @param date the date to convert
     * @return string representing the date in ISO format
     */
    public static String convertDateToString(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return df.format(date);
    }
}
