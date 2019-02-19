package ubet.http.view.applicationobjects;

import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class DateRanges {

        public final static int FIRST_DAY = 1;

        public final static int LAST_DAY = 31;

        public final static int FIRST_MONTH = 1;

        public final static int LAST_MONTH = 12;

        public final static int FIRST_YEAR = 1990;

        private List<Integer> days;

        private List<Integer> months;

        public DateRanges() {
                days = getRange(FIRST_DAY, LAST_DAY);
                months = getRange(FIRST_MONTH, LAST_MONTH);
        }

        public List<Integer> getDays() {
                return days;
        }

        public List<Integer> getMonths() {
                return months;
        }

        public List<Integer> getYears() {
                return getRange(FIRST_YEAR, getLastYear());
        }

        public List<Integer> getNextYears() {
                return getRange(getLastYear(), getLastYear() + 10);
        }

        public int getLastYear() {
                return Calendar.getInstance().get(Calendar.YEAR);
        }

        private List<Integer> getRange(int start, int end) {
                List<Integer> range = new ArrayList<Integer>();

                for (int i = start; i <= end; i++) {
                        range.add(new Integer(i));
                }

                return range;
        }

}
