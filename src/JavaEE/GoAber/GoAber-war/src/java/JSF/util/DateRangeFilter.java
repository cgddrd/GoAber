package JSF.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/** DateRangeFilter is a class providing a custom filter function for 
 * Primefaces.
 * 
 * This can be used to filter data in a data table between two date ranges. This
 * implementation is based on a refactored version of the solution presented in 
 * StackOverflow question: 
 * 
 * http://stackoverflow.com/questions/23791039/primefaces-datatable-date-range-filter-with-filterfunction
 *
 * @author Samuel Jackson
 */
@ManagedBean(name="dateRangeFilter")
@ApplicationScoped
public class DateRangeFilter implements Serializable {
    
    /** This method create
     *
     * @param value value of the item in the data table
     * @param filter date range represented as a string (dd/MM/yyyy-dd/MM/yyy)
     * @param locale the current locale
     * @return true if value is within the date range.
     */
    public boolean filterByDate(Object value, Object filter, Locale locale) {

        String filterText = parseFilter(filter);
        
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }
        if (value == null) {
            return false;
        }
        
        Date filterDate = (Date) value;
        
        DateRange range;
        try {
            range = parseDateRange(filterText);
        } catch (ParseException pe) {
            return false;
        }

        return range.dateWithinRange(filterDate);
    }
    
    private String parseFilter(Object filter) {
        return (filter == null) ? null : filter.toString().trim();
    }
    
    private DateRange parseDateRange(String filterText) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        String fromPart = filterText.substring(0, filterText.indexOf("-"));
        String toPart = filterText.substring(filterText.indexOf("-") + 1);
        Date dateFrom = fromPart.isEmpty() ? null : df.parse(fromPart);
        Date dateTo = toPart.isEmpty() ? null : df.parse(toPart);

        return new DateRange(dateFrom, dateTo);
    }

    private class DateRange {
        private final Date fromDate;
        private final Date toDate;

        public DateRange(Date fromDate, Date toDate) {
            this.fromDate = fromDate;
            this.toDate = toDate;
        }
        
        public boolean dateWithinRange(Date date) {
            return (fromDate == null || date.after(fromDate)) && 
                   (toDate == null || date.before(toDate));
        }
    }
}

