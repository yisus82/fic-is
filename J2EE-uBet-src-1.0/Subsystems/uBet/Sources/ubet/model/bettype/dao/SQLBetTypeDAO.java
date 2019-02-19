package ubet.model.bettype.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import ubet.model.bettype.to.BetTypeTO;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLBetTypeDAO {

        /**
         * Creates a bet type in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betTypeTO
         *                a transfer object with all of the bet type data, but
         *                the ID
         * @return the same transfer object but with all of the bet type data
         *         (including the ID)
         * @throws DuplicateInstanceException
         *                 if there is another bet with the same ID in the
         *                 database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public BetTypeTO create(Connection connection, BetTypeTO betTypeTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Checks if a bet type exists in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betTypeID
         *                the bet type identifier
         * @return <code>true</code> if the bet type exists<br>
         *         <code>false</code> if the bet type doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, Long betTypeID)
                        throws InternalErrorException;

        /**
         * Finds a bet type in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betTypeID
         *                the bet type identifier
         * @return a transfer object with the bet type data
         * @throws InstanceNotFoundException
         *                 if there is no bet type with the
         *                 <code>betTypeID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public BetTypeTO find(Connection connection, Long betTypeID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Finds bet types in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betTypeIDs
         *                bet type identifiers
         * @return a collecion of transfer objects with the bet types data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public Map<Long, BetTypeTO> findByIDs(Connection connection,
                        List<Long> betTypeIDs) throws InternalErrorException;

        /**
         * Returns a collection of <code>BetTypeTO</code> of a given event. If
         * the event has no bet types, a collection of zero elements is
         * returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are less
         * than 1 the collection returned has all of the bet types of the
         * specified event</li>
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
         * @return the collection of <code>BetTypeTO</code> with the data of
         *         the bet types of the specified event
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetTypeTO> findByEvent(Connection connection, Long eventID,
                        int startIndex, int count)
                        throws InternalErrorException;

        /**
         * Returns a collection of <code>BetTypeTO</code> of a given question.
         * If the question has no bet types, a collection of zero elements is
         * returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are less
         * than 1 the collection returned has all of the bet types of the
         * specified question</li>
         * </ul>
         * 
         * @param connection
         *                the connection to the database
         * @param questionID
         *                the question identifier
         * @param startIndex
         *                the index (starting from 1) of the first object to
         *                return
         * @param count
         *                the maximum number of objects to return
         * @return the collection of <code>BetTypeTO</code> with the data of
         *         the bet types of the specified question
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetTypeTO> findByQuestion(Connection connection,
                        Long questionID, int startIndex, int count)
                        throws InternalErrorException;

        /**
         * Updates a bet type in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betTypeTO
         *                a transfer object with the new bet type data
         * @throws InstanceNotFoundException
         *                 if there is no bet type with the ID included in
         *                 <code>betTypeTO</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void update(Connection connection, BetTypeTO betTypeTO)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Removes a bet type from the database.
         * 
         * @param connection
         *                the connection to the database
         * @param betTypeID
         *                the bet type identifier
         * @throws InstanceNotFoundException
         *                 if there is no bet type with the
         *                 <code>betTypeID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void remove(Connection connection, Long betTypeID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
