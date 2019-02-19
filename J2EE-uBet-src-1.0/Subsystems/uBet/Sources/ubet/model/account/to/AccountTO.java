package ubet.model.account.to;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

import ubet.model.util.DateOperations;

/**
 * A transfer object representing the information of an account.
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
 * </ul>
 */
public class AccountTO implements Serializable {

        /**
         * The account identifier
         */
        private Long accountID;

        /**
         * The identifier of the account owner
         */
        private String userID;

        /**
         * The credit card number associated with the account
         */
        private String creditCardNumber;

        /**
         * The expiration date of the credit card
         */
        private Calendar expirationDate;

        /**
         * The balance of the account
         */
        private Double balance;

        /**
         * A constructor to create <code>AccountTO</code> objects
         * 
         * @param accountID
         *                the account identifier
         * @param userID
         *                the identifier of the account owner
         * @param creditCardNumber
         *                the credit card number associated with the account
         * @param expirationDate
         *                the expiration date of the credit card
         * @param balance
         *                the balance of the account
         */
        public AccountTO(Long accountID, String userID,
                        String creditCardNumber, Calendar expirationDate,
                        Double balance) {
                this.accountID = accountID;
                this.userID = userID;
                this.creditCardNumber = creditCardNumber;
                Calendar dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(expirationDate);
                this.expirationDate = dateWithoutMilliSeconds;
                this.balance = balance;
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
         * @return the <code>userID</code>
         */
        public String getUserID() {
                return this.userID;
        }

        /**
         * @param userID
         *                the userID to set
         */
        public void setUserID(String userID) {
                this.userID = userID;
        }

        /**
         * @return the <code>creditCardNumber</code>
         */
        public String getCreditCardNumber() {
                return this.creditCardNumber;
        }

        /**
         * @param creditCardNumber
         *                the creditCardNumber to set
         */
        public void setCreditCardNumber(String creditCardNumber) {
                this.creditCardNumber = creditCardNumber;
        }

        /**
         * @return the <code>expirationDate</code>
         */
        public Calendar getExpirationDate() {
                return this.expirationDate;
        }

        /**
         * @param expirationDate
         *                the expirationDate to set
         */
        public void setExpirationDate(Calendar expirationDate) {
                this.expirationDate = expirationDate;
        }

        /**
         * @return the <code>balance</code>
         */
        public Double getBalance() {
                return this.balance;
        }

        /**
         * @param balance
         *                the balance to set
         */
        public void setBalance(Double balance) {
                this.balance = balance;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof AccountTO) {
                        AccountTO account = (AccountTO) object;
                        Calendar date = account.getExpirationDate();
                        return (account.getAccountID().equals(this.accountID)
                                        && account.getBalance().equals(
                                                        this.balance)
                                        && account
                                                        .getCreditCardNumber()
                                                        .equals(
                                                                        this.creditCardNumber)
                                        /*
                                         * We only want to compare the date not
                                         * the time.
                                         */
                                        && date.get(Calendar.MONTH) == (this.expirationDate
                                                        .get(Calendar.MONTH))
                                        && date.get(Calendar.YEAR) == (this.expirationDate
                                                        .get(Calendar.YEAR)) && account
                                        .getUserID().equals(this.userID));
                }

                return false;
        }

        @Override
        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(expirationDate
                                .getTime());

                return ("\nAccount ID = " + this.accountID + "\nBalance = "
                                + this.balance + "\nCredit card number = "
                                + this.creditCardNumber
                                + "\nExpiration date = " + dateString
                                + "\nUser ID = " + this.userID + "\n");
        }

}
