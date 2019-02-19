package ubet.model.accountoperation.to;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

import ubet.model.util.DateOperations;

/**
 * A transfer object representing the information of an account operation.
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
 * <li> The <code>type</code> of the operation may be one of the following:
 * <ul>
 * <li><code>ADD_OPERATION</code></li>
 * <li><code>WITHDRAW_OPERATION</code></li>
 * </ul>
 * </li>
 * </ul>
 */
public class AccountOperationTO implements Serializable {

        /**
         * A constant used when the account operation is an add operation
         */
        public static String ADD_OPERATION = "+";

        /**
         * A constant used when the account operation is an withdraw operation
         */
        public static String WITHDRAW_OPERATION = "-";

        /**
         * The account operation identifier
         */
        private Long accountOperationID;

        /**
         * The identifier of the account associated with the account operation
         */
        private Long accountID;

        /**
         * The amount of the account operation
         */
        private Double amount;

        /**
         * The type of the operation. It could be <code>ADD_OPERATION</code>
         * or <code>WITHDRAW_OPERATION</code>
         */
        private String type;

        /**
         * The description of the account operation
         */
        private String description;

        /**
         * The date when the account operation was made
         */
        private Calendar date;

        /**
         * The identifier of the bet associated with the account operation. It
         * may be null
         */
        private Long betID;

        /**
         * A constructor to create <code>AccountOperationTO</code> objects
         * 
         * @param accountOperationID
         *                the account operation identifier
         * @param accountID
         *                the identifier of the account associated with the
         *                account operation
         * @param amount
         *                the amount of the account operation
         * @param type
         *                the type of the operation. It could be
         *                <code>ADD_OPERATION</code> or
         *                <code>WITHDRAW_OPERATION</code>
         * @param description
         *                the description of the account operation
         * @param date
         *                the date when the account operation was made
         * @param betID
         *                the identifier of the bet associated with the account
         *                operation. It may be null
         */
        public AccountOperationTO(Long accountOperationID, Long accountID,
                        Double amount, String type, String description,
                        Calendar date, Long betID) {
                this.accountOperationID = accountOperationID;
                this.accountID = accountID;
                this.amount = amount;
                this.type = type;
                this.description = description;
                Calendar dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(date);
                this.date = dateWithoutMilliSeconds;
                this.betID = betID;
        }

        /**
         * @return the <code>accountOperationID</code>
         */
        public Long getAccountOperationID() {
                return this.accountOperationID;
        }

        /**
         * @param accountOperationID
         *                the accountOperationID to set
         */
        public void setAccountOperationID(Long accountOperationID) {
                this.accountOperationID = accountOperationID;
        }

        /**
         * @return the <code>accountID</code>
         */
        public Long getAccountID() {
                return this.accountID;
        }

        /**
         * @param accountID
         *                the accountID to set
         */
        public void setAccountID(Long accountID) {
                this.accountID = accountID;
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
         * @return the <code>type</code>
         */
        public String getType() {
                return this.type;
        }

        /**
         * @param type
         *                the type to set. It could be
         *                <code>ADD_OPERATION</code> or
         *                <code>WITHDRAW_OPERATION</code>
         */
        public void setType(String type) {
                this.type = type;
        }

        /**
         * @return the <code>description</code>
         */
        public String getDescription() {
                return this.description;
        }

        /**
         * @param description
         *                the description to set
         */
        public void setDescription(String description) {
                this.description = description;
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
         * @return the <code>betID</code>
         */
        public Long getBetID() {
                return betID;
        }

        /**
         * @param betID
         *                the betID to set
         */
        public void setBetID(Long betID) {
                this.betID = betID;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof AccountOperationTO) {
                        AccountOperationTO operation = (AccountOperationTO) object;
                        return (operation.getAccountID().equals(this.accountID)
                                        && operation
                                                        .getAccountOperationID()
                                                        .equals(
                                                                        this.accountOperationID)
                                        && operation.getAmount().equals(
                                                        this.amount)
                                        && operation.getDate()
                                                        .equals(this.date)
                                        && operation.getDescription().equals(
                                                        this.description)
                                        && operation.getType()
                                                        .equals(this.type) && operation
                                        .getBetID().equals(this.betID));
                }

                return false;
        }

        @Override
        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(date.getTime());

                return ("\nAccount ID = " + this.accountID
                                + "\nAccount operation ID = "
                                + this.accountOperationID + "\nAmount = "
                                + this.amount + "\nDate = " + dateString
                                + "\nDescription = " + this.description
                                + "\nType = " + this.type + "\nBet ID = "
                                + this.betID + "\n");
        }

}
