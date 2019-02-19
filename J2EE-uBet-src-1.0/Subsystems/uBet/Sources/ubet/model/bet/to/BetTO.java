package ubet.model.bet.to;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

import ubet.model.util.DateOperations;

/**
 * A transfer object representing the information of a bet.
 * <p>
 * NOTES:
 * <ul>
 * <li> <b>WARNING:</b> The constructor creates a date equals to the one passed
 * as a parameter, but with the <code>Calendar.MILLISECOND</code> field set to
 * 0, since some JDBC drivers (example: the one that comes with PostgreSQL)
 * throw an <code>SQLException</code> when invoking
 * <code>ResultSet.getTimestamp(int)</code> over an attribute that has
 * milliseconds (<code>PreparedStatement.setTimestamp(int, Timestamp)</code>
 * may be invoked, but not <code>ResultSet.getTimestamp(int)</code>), which
 * indicates a parsing error in the date (since the implementation of
 * <code>ResultSet.getTimestamp(int)</code> does not expect milliseconds).
 * </li>
 * <br>
 * <li> The <code>status</code> of the bet may be one of the following:
 * <ul>
 * <li><code>GAINED</code></li>
 * <li><code>PENDING</code></li>
 * <li><code>LOST</code></li>
 * </ul>
 * </li>
 * </ul>
 */
public class BetTO implements Serializable {

        /**
         * A constant used when the bet is a gained bet
         */
        public static String GAINED = "G";

        /**
         * A constant used when the bet is a pending bet
         */
        public static String PENDING = "P";

        /**
         * A constant used when the bet is a lost bet
         */
        public static String LOST = "L";

        /**
         * The bet identifier
         */
        private Long betID;

        /**
         * The identifier of the bet type associated with the bet
         */
        private Long betTypeID;

        /**
         * The identifier of the bet option associated with the bet
         */
        private Long betOptionID;

        /**
         * The identifier of the account associated with the bet
         */
        private Long accountID;

        /**
         * The identifier of the event associated with the bet
         */
        private Long eventID;

        /**
         * The amount of the bet
         */
        private Double amount;

        /**
         * The date when the bet was made
         */
        private Calendar date;

        /**
         * The current status of the bet. It could be <code>GAINED</code>,
         * <code>PENDING</code> or <code>LOST</code>
         */
        private String status;

        /**
         * A constructor to create <code>BetTO</code> objects
         * 
         * @param betID
         *                the bet identifier
         * @param betTypeID
         *                the identifier of the bet type associated with the bet
         * @param betOptionID
         *                the identifier of the bet option associated with the
         *                bet
         * @param accountID
         *                the identifier of the account associated with the bet
         * @param eventID
         *                the identifier of the event associated with the bet
         * @param amount
         *                the amount of the bet
         * @param date
         *                the date when the bet was made
         * @param status
         *                the current status of the bet. It could be
         *                <code>GAINED</code>, <code>PENDING</code> or
         *                <code>LOST</code>
         */
        public BetTO(Long betID, Long betTypeID, Long betOptionID,
                        Long accountID, Long eventID, Double amount,
                        Calendar date, String status) {
                this.betID = betID;
                this.betTypeID = betTypeID;
                this.betOptionID = betOptionID;
                this.accountID = accountID;
                this.eventID = eventID;
                this.amount = amount;
                Calendar dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(date);
                this.date = dateWithoutMilliSeconds;
                this.status = status;
        }

        /**
         * @return the <code>betID</code>
         */
        public Long getBetID() {
                return this.betID;
        }

        /**
         * @param betID
         *                the betID to set
         */
        public void setBetID(Long betID) {
                this.betID = betID;
        }

        /**
         * @return the <code>betTypeID</code>
         */
        public Long getBetTypeID() {
                return this.betTypeID;
        }

        /**
         * @param betTypeID
         *                the betTypeID to set
         */
        public void setBetTypeID(Long betTypeID) {
                this.betTypeID = betTypeID;
        }

        /**
         * @return the <code>betID</code>
         */
        public Long getBetOptionID() {
                return this.betOptionID;
        }

        /**
         * @param betOptionID
         *                the betOptionID to set
         */
        public void setBetOptionID(Long betOptionID) {
                this.betOptionID = betOptionID;
        }

        /**
         * @return the <code>accountID</code>
         */
        public Long getAccountID() {
                return accountID;
        }

        /**
         * @param accountID
         *                the accountID to set
         */
        public void setAccountID(Long accountID) {
                this.accountID = accountID;
        }

        /**
         * @return the <code>eventID</code>
         */
        public Long getEventID() {
                return this.eventID;
        }

        /**
         * @param eventID
         *                the eventID to set
         */
        public void setEventID(Long eventID) {
                this.eventID = eventID;
        }

        /**
         * @return the <code>amount</code>
         */
        public Double getAmount() {
                return this.amount;
        }

        /**
         * @param amount
         *                the amount to set
         */
        public void setAmount(Double amount) {
                this.amount = amount;
        }

        /**
         * @return the <code>date</code>
         */
        public Calendar getDate() {
                return this.date;
        }

        /**
         * @param date
         *                the date to set
         */
        public void setDate(Calendar date) {
                this.date = date;
        }

        /**
         * @return the <code>status</code>
         */
        public String getStatus() {
                return this.status;
        }

        /**
         * @param status
         *                the status to set. It could be <code>GAINED</code>,
         *                <code>PENDING</code> or <code>LOST</code>
         */
        public void setStatus(String status) {
                this.status = status;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof BetTO) {
                        BetTO bet = (BetTO) object;
                        return (bet.getAccountID().equals(this.accountID)
                                        && bet.getAmount().equals(this.amount)
                                        && bet.getBetID().equals(this.betID)
                                        && bet.getBetOptionID().equals(
                                                        this.betOptionID)
                                        && bet.getBetTypeID().equals(
                                                        this.betTypeID)
                                        && bet.getDate().equals(this.date)
                                        && bet.getEventID()
                                                        .equals(this.eventID) && bet
                                        .getStatus().equals(this.status));
                }

                return false;
        }

        @Override
        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(date.getTime());

                return ("\nAccount ID = " + this.accountID + "\nAmount = "
                                + this.amount + "\nBet ID = " + this.betID
                                + "\nBet option ID = " + this.betOptionID
                                + "\nBet type ID = " + this.betTypeID
                                + "\nDate = " + dateString + "\nEvent ID = "
                                + this.eventID + "\nStatus = " + this.status + "\n");
        }

}
