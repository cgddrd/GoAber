package rest;

import GoAberDatabase.ActivityData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

       
public class ActivityDataDTO {
        private final int value;
        private final String activityDate;
        private final String lastUpdated;
        private final String category;
        private final String unit;
        private final String user;

        public ActivityDataDTO(ActivityData data) {
            value = data.getValue();            
            activityDate = convertDateToString(data.getDate());
            lastUpdated = convertDateToString(data.getLastUpdated());
            category = data.getCategoryUnitId().getCategoryId().getName();
            unit = data.getCategoryUnitId().getUnitId().getName();
            user = data.getUserId().getNickname();
        }
        
        /** Converts a Java date object to a string
         * 
         * @param date the date to convert
         * @return string representing the date in ISO format
         */
        private String convertDateToString(Date date) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            return df.format(date);
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * @return the activityDate
         */
        public String getActivityDate() {
            return activityDate;
        }

        /**
         * @return the lastUpdated
         */
        public String getLastUpdated() {
            return lastUpdated;
        }

        /**
         * @return the category
         */
        public String getCategory() {
            return category;
        }

        /**
         * @return the unit
         */
        public String getUnit() {
            return unit;
        }

        /**
         * @return the user
         */
        public String getUser() {
            return user;
        }
    }