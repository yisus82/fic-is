package ubet.model.account.dao;

import java.sql.Connection;
import java.util.List;

import ubet.model.account.to.AccountTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLAccountDAO {

        /**
         * Creates an account in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param accountTO
         *                a transfer object with all of the account data, but
         *                the ID
         * @return the same transfer object but with all of the account data
         *         (including the ID)
         * @throws DuplicateInstanceException
         *                 if there is another account with the same ID in the
         *                 database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public AccountTO create(Connection connection, AccountTO accountTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Checks if an account in the database exists.
         * 
         * @param connection
         *                the connection to the database
         * @param accountID
         *                the account identifier
         * @return <code>true</code> if the account exists<br>
         *         <code>false</code> if the account doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, Long accountID)
                        throws InternalErrorException;

        /**
         * Finds an account in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param accountID
         *                the account identifier
         * @return a transfer object with the account data
         * @throws InstanceNotFoundException
         *                 if there is no account with the
         *                 <code>accountID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public AccountTO find(Connection connection, Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Returns a collection of <code>AccountTO</code> of a given user. If
         * the user has no acounts, a collection of zero elements is returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are less
         * than 1 the collection returned has all of the accounts of the
         * specified user</li>
         * </ul>
         * 
         * @param connection
         *                the connection to the database
         * @param login
         *                the user login (identifier)
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
        public List<AccountTO> findByUser(Connection connection, String login,
                        int startIndex, int count)
                        throws InternalErrorException;

        /**
         * Updates an account in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param accountTO
         *                a transfer object with the new account data
         * @throws InstanceNotFoundException
         *                 if there is no account with the ID included in
         *                 <code>accountTO</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void update(Connection connection, AccountTO accountTO)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Removes an account from the database.
         * 
         * @param connection
         *                the connection to the database
         * @param accountID
         *                the account identifier
         * @throws InstanceNotFoundException
         *                 if there is no account with the
         *                 <code>accountID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void remove(Connection connection, Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
