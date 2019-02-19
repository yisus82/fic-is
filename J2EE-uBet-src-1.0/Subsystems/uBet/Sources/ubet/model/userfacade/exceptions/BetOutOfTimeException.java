package ubet.model.userfacade.exceptions;

import java.util.Calendar;

import es.udc.fbellas.j2ee.util.exceptions.ModelException;

/**
 * An exception thrown when an user tries to make a bet when the bet time is
 * over
 * 
 */
public class BetOutOfTimeException extends ModelException {

        /**
         * The date when the bet time finishes
         */
        private Calendar endDate;

        /**
         * The current date
         */
        private Calendar currentDate;

        /**
         * Constructs a new exception with the message "Bet out of time
         * exception => end date = <code>endDate</code> | current date =
         * <code>currentDate</code>"
         * 
         * @param endDate
         *                the date when the bet time finishes
         * @param currentDate
         *                the current date
         */
        public BetOutOfTimeException(Calendar endDate, Calendar currentDate) {
                super("Bet out of time exception => end date = " + endDate
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
