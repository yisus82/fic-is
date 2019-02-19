package ubet.model.userfacade.exceptions;

import es.udc.fbellas.j2ee.util.exceptions.ModelException;

/**
 * An exception thrown when there isn't enough money in the account to do an
 * operation
 * 
 */
public class InsufficientBalanceException extends ModelException {

        /**
         * The account identifier
         */
        private Long accountID;

        /**
         * The current balance in the account
         */
        private double currentBalance;

        /**
         * The minimum amount of money necessary to do the operation
         */
        private double amountToWithdraw;

        /**
         * Creates a new exception with the message "Insufficient balance
         * exception => account ID = <code>accountID</code> | current balance =
         * <code>currentBalance</code> | amount to withdraw =
         * <code>amountToWithdraw</code>"
         * 
         * @param accountID
         *                the account identifier
         * @param currentBalance
         *                the current balance in the account
         * @param amountToWithdraw
         *                the minimum amount of money necessary to do the
         *                operation
         */
        public InsufficientBalanceException(Long accountID,
                        double currentBalance, double amountToWithdraw) {
                super("Insufficient balance exception => account ID = "
                                + accountID + " | current balance = "
                                + currentBalance + " | amount to withdraw = "
                                + amountToWithdraw);
                this.accountID = accountID;
                this.currentBalance = currentBalance;
                this.amountToWithdraw = amountToWithdraw;
        }

        /**
         * @return the <code>accountID</code>
         */
        public Long getAccountID() {
                return this.accountID;
        }

        /**
         * @return the <code>currentBalance</code>
         */
        public double getCurrentBalance() {
                return this.currentBalance;
        }

        /**
         * @return the <code>amountToWithdraw</code>
         */
        public double getAmountToWithdraw() {
                return this.amountToWithdraw;
        }

}
