/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author samuel
 */
public class DateRangeFilterTest {
    
    private DateRangeFilter dateRangeFilter;
    private Locale defaultLocale;
    private SimpleDateFormat formatter;
    
    @Before
    public void setUp() {
        dateRangeFilter = new DateRangeFilter();
        defaultLocale = null;
        formatter = new SimpleDateFormat("dd/MM/yyyy");
    }

    /**
     * Test of filterByDate method, of class DateRangeFilter.
     * 
     * Value should be within the range.
     */
    @Test
    public void testFilterByDate_withinRange() {
        Date value = parseDate("2/11/15");
        String filter = "1/11/15-3/11/15";
        
        boolean expResult = true;
        boolean result = dateRangeFilter.filterByDate(value, filter, defaultLocale);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of filterByDate method, of class DateRangeFilter.
     * 
     * Value should be outside of the range.
     */
    @Test
    public void testFilterByDate_outsideRange() {
        Date value = parseDate("5/11/15");
        String filter = "1/11/15-3/11/15";
        
        boolean expResult = false;
        boolean result = dateRangeFilter.filterByDate(value, filter, defaultLocale);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of filterByDate method, of class DateRangeFilter.
     * 
     * Value should be outside of the range.
     */
    @Test
    public void testFilterByDate_rangeIsADay() {
        Date value = parseDate("1/11/15");
        String filter = "1/11/15-1/11/15";
        
        boolean expResult = false;
        boolean result = dateRangeFilter.filterByDate(value, filter, defaultLocale);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of filterByDate method, of class DateRangeFilter.
     * 
     * Value should be outside of the range.
     */
    @Test
    public void testFilterByDate_edgeOfLowerRange() {
        Date value = parseDate("1/11/15");
        String filter = "1/11/15-5/11/15";
        
        boolean expResult = false;
        boolean result = dateRangeFilter.filterByDate(value, filter, defaultLocale);
        
        assertEquals(expResult, result);
    }
    
    /**
     * Test of filterByDate method, of class DateRangeFilter.
     * 
     * Value should be outside of the range.
     */
    @Test
    public void testFilterByDate_edgeOfUpperRange() {
        Date value = parseDate("5/11/15");
        String filter = "1/11/15-5/11/15";
        
        boolean expResult = false;
        boolean result = dateRangeFilter.filterByDate(value, filter, defaultLocale);
        
        assertEquals(expResult, result);
    }
    
    private Date parseDate(String date) {
        try {
            return formatter.parse(date);
        } catch (ParseException ex) {
            return null;
        }
    }
    
}
