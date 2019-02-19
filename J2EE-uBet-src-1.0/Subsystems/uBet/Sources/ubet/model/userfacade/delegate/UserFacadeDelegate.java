package ubet.model.userfacade.delegate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import ubet.model.account.to.AccountTO;
import ubet.model.bet.to.BetTO;
import ubet.model.userfacade.exceptions.BetOutOfTimeException;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;
import ubet.model.userfacade.to.AccountChunkTO;
import ubet.model.userfacade.to.AccountOperationChunkTO;
import ubet.model.userfacade.to.BetChunkTO;
import ubet.model.userfacade.to.LoginResultTO;
import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A facade to model the interaction of the user with the portal. There is some
 * logical restrictions with regard to the method calling order. In particular,
 * to call methods the user has to be authenticated. This happens when the user
 * calls <code>login</code> or <code>registerUser</code>.
 */
public interface UserFacadeDelegate extends Serializable {

        /**
         * Registers an user in the database and creates an account associated
         * to this user
         * 
         * @param userTO
         *                a transfer object with all of the user data
         * @param creditCardNumber
         *                the credit card number associated to the account to be
         *                created
         * @param expirationDate
         *                the expiration date of the credit card number
         *                associated to the account to be created
         * @param balance
         *                the balance of the account to be created
         * @return a transfer object with all of the created account data
         *         (including the ID)
         * @throws DuplicateInstanceException
         *                 if there is another user (or another account) with
         *                 the same identifier in the database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public AccountTO registerUser(UserProfileTO userTO,
                        String creditCardNumber, Calendar expirationDate,
                        Double balance) throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Allows an user to be authenticated
         * 
         * @param login
         *                the user login (identifier)
         * @param password
         *                the password of the user
         * @param passwordIsEncrypted
         *                <code>true</code> if the given password is encrypted<br>
         *                <code>false</code> otherwise
         * @return a custom transfer object with the relevant data of the user
         * @throws InstanceNotFoundException
         *                 if there is no user with the <code>login</code>
         * @throws IncorrectPasswordException
         *                 if the password given is not correct
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public LoginResultTO login(String login, String password,
                        boolean passwordIsEncrypted)
                        throws InstanceNotFoundException,
                        IncorrectPasswordException, InternalErrorException;

        /**
         * Updates the user details in the database
         * 
         * @param userTO
         *                a transfer object with the new user data
         * @throws InternalErrorException
         *                 if a severe error occurs
         * @throws DuplicateInstanceException
         *                 if an incorrect login update occurs
         */
        public void updateUserDetails(UserProfileTO userTO)
                        throws InternalErrorException,
                        DuplicateInstanceException;

        /**
         * Changes the password of the user in the database
         * 
         * @param oldClearPassword
         *                the old password (not encrypted)
         * @param newClearPassword
         *                the new password (not encrypted)
         * @throws IncorrectPasswordException
         *                 if the old password given is not correct
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void changePassword(String oldClearPassword,
                        String newClearPassword)
                        throws IncorrectPasswordException,
                        InternalErrorException;

        /**
         * Inserts a bet in the database
         * 
         * @param betTO
         *                a transfer object with all of the bet data, but the ID
         * @return the same transfer object but with all of the bet data
         *         (including the ID)
         * @throws InternalErrorException
         *                 if a severe error occurs
         * @throws BetOutOfTimeException
         *                 if the time to bet for the bet type associated to the
         *                 bet is over
         * @throws InsufficientBalanceException
         *                 if there isn't enough money to make the bet
         */
        public BetTO bet(BetTO betTO) throws InternalErrorException,
                        BetOutOfTimeException, InsufficientBalanceException;

        /**
         * Finds an user in the database
         * 
         * @return a transfer object with the user data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public UserProfileTO findUser() throws InternalErrorException;

        /**
         * Creates an account in the database
         * 
         * @param accountTO
         *                a transfer object with all of the account data, but
         *                the ID
         * @return the same transfer object but with all of the account data
         *         (including the ID)
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public AccountTO createAccount(AccountTO accountTO)
                        throws InternalErrorException;

        /**
         * Updates the details of an account in the database
         * 
         * @param accountTO
         *                a transfer object with the new account data
         * @throws InternalErrorException
         *                 if a severe error occurs
         * @throws InstanceNotFoundException
         *                 if there is no account with the ID included in
         *                 <code>accountTO</code>
         */
        public void updateAccountDetails(AccountTO accountTO)
                        throws InternalErrorException,
                        InstanceNotFoundException;

        /**
         * Finds an account in the database
         * 
         * @param accountID
         *                the account identifier
         * @return a transfer object with the account data
         * @throws InstanceNotFoundException
         *                 if there is no account with the
         *                 <code>accountID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public AccountTO findAccount(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Returns a collection of <code>AccountOperationTO</code> of a given
         * account. If the account has no account operations, a collection of
         * zero elements is returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are equal
         * to -1 the collection returned has all of the accounts of the
         * specified user</li>
         * </ul>
         * 
         * @param accountID
         *                the account identifier
         * @return the collection of <code>AccountTO</code> with the data of
         *         the accounts of the specified user
         * @throws InstanceNotFoundException
         *                 if there is no account operation with the
         *                 <code>accountID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public AccountOperationChunkTO findAccountOperations(Long accountID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Returns a collection of <code>BetStatusTO</code> of a given
         * account. If the account has no bets, a collection of zero elements is
         * returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are equal
         * to -1 the collection returned has all of the bets of the specified
         * account</li>
         * <br>
         * <li> The <code>startDate</code> and the <code>endDate</code> may
         * be <code>null</code> </li>
         * </ul>
         * 
         * @param accountID
         *                the account identifier
         * @param startIndex
         *                the index (starting from 1) of the first object to
         *                return
         * @param count
         *                the maximum number of objects to return
         * @param startDate
         *                the older date of the bets to be returned (including
         *                this date)
         * @param endDate
         *                the newer date of the bets to be returned (including
         *                this date)
         * @return the collection of <code>BetStatusTO</code> with the data of
         *         the bets of the specified account
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public BetChunkTO findBetsByAccount(Long accountID, int startIndex,
                        int count, Calendar startDate, Calendar endDate)
                        throws InternalErrorException;

        /**
         * Returns a collection of <code>AccountTO</code> of a given user. If
         * the user has no accounts, a collection of zero elements is returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are equal
         * to -1 the collection returned has all of the accounts of the
         * specified user</li>
         * </ul>
         * 
         * @param startIndex
         *                the index (starting from 1) of the first object to
         *                return
         * @param count
         *                the maximum number of objects to return
         * @return the collection of <code>AccountTO</code> with the data of
         *         the accounts of the specified user
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public AccountChunkTO findAccountsByUser(int startIndex, int count)
                        throws InternalErrorException;

        /**
         * Adds an <code>amount</code> of money to a given account in the
         * database.
         * 
         * @param accountID
         *                the account identifier
         * @param amount
         *                the amount to be added
         * @throws InstanceNotFoundException
         *                 if there is no account with the
         *                 <code>accountID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void addToAccount(Long accountID, Double amount)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Withdraws an <code>amount</code> of money from a given account in
         * the database.
         * 
         * @param accountID
         *                the account identifier
         * @param amount
         *                the amount to be withdrawn
         * @throws InstanceNotFoundException
         *                 if there is no account with the
         *                 <code>accountID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void withdrawFromAccount(Long accountID, Double amount)
                        throws InstanceNotFoundException,
                        InsufficientBalanceException, InternalErrorException;

        /**
         * Returns a collection of the favorite categories' indentifiers of a
         * given account. If the account has no bets, a collection of zero
         * elements is returned.
         * 
         * @param accountID
         *                the account identifier
         * @return the collection of the favorite categories' indentifiers of
         *         the specified account
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<String> findFavorites(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
