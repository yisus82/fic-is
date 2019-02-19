package ubet.model.accountoperation.dao;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import ubet.model.accountoperation.to.AccountOperationTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLAccountOperationDAO {

        /**
         * Creates an account operation in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param accountOperationTO
         *                a transfer object with all of the account operation
         *                data, but the ID
         * @return the same transfer object but with all of the account
         *         operation data (including the ID)
         * @throws DuplicateInstanceException
         *                 if there is another account operation with the same
         *                 ID in the database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public AccountOperationTO create(Connection connection,
                        AccountOperationTO accountOperationTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Checks if an account operation exists in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param accountOperationID
         *                the account operation identifier
         * @return <code>true</code> if the account operation exists<br>
         *         <code>false</code> if the account operation doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, Long accountOperationID)
                        throws InternalErrorException;

        /**
         * Finds an account operation in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param accountOperationID
         *                the account operation identifier
         * @return a transfer object with the account operation data
         * @throws InstanceNotFoundException
         *                 if there is no account operation with the
         *                 <code>accountOperationID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public AccountOperationTO find(Connection connection,
                        Long accountOperationID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Returns a collection of <code>AccountOperationTO</code> of a given
         * account made between two dates. If the account has no account
         * operations, a collection of zero elements is returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are less
         * than 1 the collection returned has all of the account operations of
         * the specified account</li>
         * <br>
         * <li> The <code>startDate</code> and the <code>endDate</code> may
         * be <code>null</code> </li>
         * </ul>
         * 
         * @param connection
         *                the connection to the database
         * @param accountID
         *                the account identifier
         * @param startIndex
         *                the index (starting from 1) of the first object to
         *                return
         * @param count
         *                the maximum number of objects to return
         * @param startDate
         *                the older date of the account operations to be
         *                returned (including this date)
         * @param endDate
         *                the newer date of the account operations to be
         *                returned (including this date)
         * @return the collection of <code>AccountOperationTO</code> with the
         *         data of the account operations of the specified account
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<AccountOperationTO> findByAccount(Connection connection,
                        Long accountID, int startIndex, int count,
                        Calendar startDate, Calendar endDate)
                        throws InternalErrorException;

        /**
         * Updates an account operation in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param accountOperationTO
         *                a transfer object with the new account operation data
         * @throws InstanceNotFoundException
         *                 if there is no account operation with the ID included
         *                 in <code>accountOperationTO</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void update(Connection connection,
                        AccountOperationTO accountOperationTO)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Removes an account operation from the database.
         * 
         * @param connection
         *                the connection to the database
         * @param accountOperationID
         *                the account operation identifier
         * @throws InstanceNotFoundException
         *                 if there is no account operation with the
         *                 <code>accountOperationID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void remove(Connection connection, Long accountOperationID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
