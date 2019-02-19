package ubet.model.util;

import java.util.Calendar;

public final class DateOperations {

        private DateOperations() {
        }

        /**
         * Returns a copy of the date passed as a parameter with the
         * <code>Calendar.MILLISECOND</code> field set to 0.
         */
        public final static Calendar getDateWithoutMilliSeconds(Calendar date) {
                /*
                 * ***WARNING***
                 * 
                 * An alternative implementation of this method could be:
                 * 
                 * Calendar dateWithoutMilliSeconds = date.clone();
                 * dateWithoutMilliSeconds.set(Calendar.MILLISECOND, 0); return
                 * dateWithoutMilliSeconds;
                 * 
                 * However, if the date passed as a parameter comes from a
                 * remote invocation (RMI, for example), the clone gets
                 * corrupted after the invocation of the "set" method.
                 * Incredibly, but true !!!
                 */

                Calendar dateWithoutMilliSeconds = Calendar.getInstance();

                dateWithoutMilliSeconds.setTime(date.getTime());
                dateWithoutMilliSeconds.set(Calendar.MILLISECOND, 0);

                return dateWithoutMilliSeconds;

        }

}
