package rest;

import GoAberDatabase.ActivityData;
import JSF.util.DateUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

       
public class ActivityDataDTO {
        private final int value;
        private final String activityDate;
        private final String lastUpdated;
        private final String category;
        private final String unit;
        private final String user;

        public ActivityDataDTO(ActivityData data) {
            value = data.getValue();            
            activityDate = DateUtils.convertDateToString(data.getDate());
            lastUpdated = DateUtils.convertDateToString(data.getLastUpdated());
            category = data.getCategoryUnitId().getCategoryId().getName();
            unit = data.getCategoryUnitId().getUnitId().getName();
            user = data.getUserId().getNickname();
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