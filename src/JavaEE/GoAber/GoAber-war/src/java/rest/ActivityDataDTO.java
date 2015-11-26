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
            activityDate = convertDateToISO(data.getDate());
            lastUpdated = convertDateToISO(data.getLastUpdated());
            category = data.getCategoryUnitId().getCategoryId().getName();
            unit = data.getCategoryUnitId().getUnitId().getName();
            user = data.getUserId().getNickname();
        }
        
        /** Converts a Java date object to an ISO string
         * 
         * @param date the date to convert
         * @return string representing the date in ISO format
         */
        private String convertDateToISO(Date date) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
            df.setTimeZone(tz);
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