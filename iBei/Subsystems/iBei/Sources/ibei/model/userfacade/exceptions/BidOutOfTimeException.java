package ibei.model.userfacade.exceptions;

import java.util.Calendar;

import es.udc.fbellas.j2ee.util.exceptions.ModelException;

/**
 * An exception thrown when an user tries to make a bid when the bid time is
 * over
 * 
 */
public class BidOutOfTimeException extends ModelException {

        /**
         * The date when the bid time finishes
         */
        private Calendar endDate;

        /**
         * The current date
         */
        private Calendar currentDate;

        /**
         * Constructs a new exception with the message "Bid out of time
         * exception => end date = <code>endDate</code> | current date =
         * <code>currentDate</code>"
         * 
         * @param endDate
         *                the date when the bid time finishes
         * @param currentDate
         *                the current date
         */
        public BidOutOfTimeException(Calendar endDate, Calendar currentDate) {
                super("Bid out of time exception => end date = " + endDate
                                + " | current date = " + currentDate);
                this.endDate = endDate;
                this.currentDate = currentDate;
        }

        /**
         * @return the <code>endDate</code>
         */
        public Calendar getEndDate() {
                return this.endDate;
        }

        /**
         * @return the <code>currentDate</code>
         */
        public Calendar getCurrentDate() {
                return this.currentDate;
        }

}
