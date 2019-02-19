package ubet.model.bet.dao;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import ubet.model.bet.to.BetTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLBetDAO {

        /**
         * Creates a bet in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betTO
         *                a transfer object with all of the bet data, but the ID
         * @return the same transfer object but with all of the bet data
         *         (including the ID)
         * @throws DuplicateInstanceException
         *                 if there is another bet with the same ID in the
         *                 database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public BetTO create(Connection connection, BetTO betTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Checks if a bet exists in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betID
         *                the bet identifier
         * @return <code>true</code> if the bet exists<br>
         *         <code>false</code> if the bet doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, Long betID)
                        throws InternalErrorException;

        /**
         * Finds a bet in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betID
         *                the bet identifier
         * @return a transfer object with the bet data
         * @throws InstanceNotFoundException
         *                 if there is no bet with the <code>betID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public BetTO find(Connection connection, Long betID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Returns a collection of <code>BetTO</code> of a given account. If
         * the account has no bets, a collection of zero elements is returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are are
         * less than 1 the collection returned has all of the bets of the
         * specified account</li>
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
         *                the older date of the bets to be returned (including
         *                this date)
         * @param endDate
         *                the newer date of the bets to be returned (including
         *                this date)
         * @return the collection of <code>BetTO</code> with the data of the
         *         bets of the specified account
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetTO> findByAccount(Connection connection, Long accountID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException;

        /**
         * Returns a collection of <code>BetTO</code> of a given event. If the
         * event has no bets, a collection of zero elements is returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are less
         * than 1 the collection returned has all of the bets of the specified
         * event</li>
         * <br>
         * <li> The <code>startDate</code> and the <code>endDate</code> may
         * be <code>null</code> </li>
         * </ul>
         * 
         * @param connection
         *                the connection to the database
         * @param eventID
         *                the event identifier
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
         * @return the collection of <code>BetTO</code> with the data of the
         *         bets of the specified event
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetTO> findByEvent(Connection connection, Long eventID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException;

        /**
         * Returns a collection of <code>BetTO</code> of a given bet type. If
         * the bet type has no bets, a collection of zero elements is returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are less
         * than 1 the collection returned has all of the bets of the specified
         * bet type</li>
         * <br>
         * <li> The <code>startDate</code> and the <code>endDate</code> may
         * be <code>null</code> </li>
         * </ul>
         * 
         * @param connection
         *                the connection to the database
         * @param betTypeID
         *                the bet type identifier
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
         * @return the collection of <code>BetTO</code> with the data of the
         *         bets of the specified bet type
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetTO> findByBetType(Connection connection, Long betTypeID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException;

        /**
         * Returns a collection of <code>BetTO</code> of a given bet option.
         * If the bet option has no bets, a collection of zero elements is
         * returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are less
         * than 1 the collection returned has all of the bets of the specified
         * bet option</li>
         * <br>
         * <li> The <code>startDate</code> and the <code>endDate</code> may
         * be <code>null</code> </li>
         * </ul>
         * 
         * @param connection
         *                the connection to the database
         * @param betOptionID
         *                the bet option identifier
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
         * @return the collection of <code>BetTO</code> with the data of the
         *         bets of the specified account
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetTO> findByBetOption(Connection connection,
                        Long betOptionID, int startIndex, int count,
                        Calendar startDate, Calendar endDate)
                        throws InternalErrorException;

        /**
         * Updates a bet in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betTO
         *                a transfer object with the new bet data
         * @throws InstanceNotFoundException
         *                 if there is no bet with the ID included in
         *                 <code>betTO</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void update(Connection connection, BetTO betTO)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Removes a bet from the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betID
         *                the bet identifier
         * @throws InstanceNotFoundException
         *                 if there is no bet with the <code>betID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void remove(Connection connection, Long betID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
