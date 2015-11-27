/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.util;

import GoAberDatabase.ActivityData;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author samuel
 */
public class StatisticsSummary {
    
    private final double total;
    private final double average;
    private final double min;
    private final String minDate;
    private final double max;
    private final String maxDate;
    
    public StatisticsSummary(List<ActivityData> data) {
        total  = computeTotal(data);
        average = computeMean(data);
        
        ActivityData minItem = findMinItem(data);
        min = minItem.getValue();
        minDate = DateUtils.convertDateToString(minItem.getDate());
        
        ActivityData maxItem = findMaxItem(data);
        max = maxItem.getValue();
        maxDate = DateUtils.convertDateToString(maxItem.getDate());
        
    }
    
    /** Compute the total number of units across the subset of activity data
     * 
     * This uses the value field of the actiivty data item.
     * 
     * @param data the data to compute the total for
     * @return the total
     */
    private double computeTotal(List<ActivityData> data) {
        double summation = 0;
        
        for (ActivityData item : data) {
            summation += item.getValue();
        }
        
        return summation;
    }
    
    /** Compute the mean of a subset of activity data.
     * 
     * This requires computeTotal to have been run previously.
     * 
     * @param data the data to compute the mean for
     * @return the mean
     */
    private double computeMean(List<ActivityData> data) {
        return getTotal() / data.size();
    }
    
    /** Find the activity data item with the smallest value
     * 
     * @param data the data to find the min item for.
     * @return the min item
     */
    private ActivityData findMinItem(List<ActivityData> data) {
        return Collections.min(data, new ActivityDataComparator());
    }
    
    /** Find the activity data item with the largest value
     * 
     * @param data the data to find the max item for.
     * @return the max item
     */
    private ActivityData findMaxItem(List<ActivityData> data) {
        return Collections.max(data, new ActivityDataComparator());
    }


    /**
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @return the average
     */
    public double getAverage() {
        return average;
    }

    /**
     * @return the min
     */
    public double getMin() {
        return min;
    }

    /**
     * @return the minDate
     */
    public String getMinDate() {
        return minDate;
    }

    /**
     * @return the max
     */
    public double getMax() {
        return max;
    }

    /**
     * @return the maxDate
     */
    public String getMaxDate() {
        return maxDate;
    }
    
    /** Comparator class for ActivityData
     * 
     * Uses the value field of an activity data object as the parameter
     * to compareTo.
     * 
     */
    private class ActivityDataComparator implements Comparator<ActivityData>{
        
        @Override
        public int compare(ActivityData a, ActivityData b) {
            return a.getValue().compareTo(b.getValue());
        }
    }
}


