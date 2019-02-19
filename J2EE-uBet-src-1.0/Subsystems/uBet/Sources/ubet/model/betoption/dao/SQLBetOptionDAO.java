package ubet.model.betoption.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import ubet.model.betoption.to.BetOptionTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLBetOptionDAO {

        /**
         * Creates a bet option in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betOptionTO
         *                a transfer object with all of the bet option data, but
         *                the ID
         * @return the same transfer object but with all of the bet option data
         *         (including the ID)
         * @throws DuplicateInstanceException
         *                 if there is another bet option with the same ID in
         *                 the database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public BetOptionTO create(Connection connection, BetOptionTO betOptionTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Checks if a bet option exists in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betOptionID
         *                the bet option identifier
         * @return <code>true</code> if the bet option exists<br>
         *         <code>false</code> if the bet option doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, Long betOptionID)
                        throws InternalErrorException;

        /**
         * Finds a bet option in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betOptionID
         *                the bet option identifier
         * @return a transfer object with the bet option data
         * @throws InstanceNotFoundException
         *                 if there is no bet option with the
         *                 <code>betOptionID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public BetOptionTO find(Connection connection, Long betOptionID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Finds bet options in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betOptionIDs
         *                bet option identifiers
         * @return a collection of transfer objects with the bet options data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public Map<Long, BetOptionTO> findByIDs(Connection connection,
                        List<Long> betOptionIDs) throws InternalErrorException;

        /**
         * Returns a collection of <code>BetOptionTO</code> of a given bet
         * type. If the bet type has no bet options, a collection of zero
         * elements is returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are less
         * than 1 the collection returned has all of the bet options of the
         * specified bet type</li>
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
         * @return the collection of <code>BetOptionTO</code> with the data of
         *         the bet options of the specified bet type
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetOptionTO> findByBetType(Connection connection,
                        Long betTypeID, int startIndex, int count)
                        throws InternalErrorException;

        /**
         * Returns a collection of winner <code>BetOptionTO</code> of a given
         * bet type. If the bet type has no bet options, a collection of zero
         * elements is returned.
         * 
         * @param connection
         *                the connection to the database
         * @param betTypeID
         *                the bet type identifier
         * @return the collection of winner <code>BetOptionTO</code> with the
         *         data of the bet options of the specified bet type
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetOptionTO> findWinners(Connection connection,
                        Long betTypeID) throws InternalErrorException;

        /**
         * Returns a collection of collections of winner
         * <code>BetOptionTO</code> of given bet types. If all the bet types
         * have no bet options, a collection of zero elements is returned.
         * 
         * @param connection
         *                the connection to the database
         * @param betTypeIDs
         *                bet type identifiers
         * @return the collection of collections of winner
         *         <code>BetOptionTO</code> with the data of the bet options
         *         of the specified bet types
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public Map<Long, List<BetOptionTO>> findWinnersByIDs(
                        Connection connection, List<Long> betTypeIDs)
                        throws InternalErrorException;

        /**
         * Updates a bet option in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betOptionTO
         *                a transfer object with the new bet option data
         * @throws InstanceNotFoundException
         *                 if there is no bet option with the ID included in
         *                 <code>betOptionTO</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void update(Connection connection, BetOptionTO betOptionTO)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Removes a bet option from the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betOptionID
         *                the bet option identifier
         * @throws InstanceNotFoundException
         *                 if there is no bet option with the <code>betID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void remove(Connection connection, Long betOptionID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
