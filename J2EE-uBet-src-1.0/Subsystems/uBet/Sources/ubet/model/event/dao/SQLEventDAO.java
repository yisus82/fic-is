package ubet.model.event.dao;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ubet.model.event.to.EventTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLEventDAO {

        /**
         * Creates an event in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param eventTO
         *                a transfer object with all of the event data, but the
         *                ID
         * @return the same transfer object but with all of the event data
         *         (including the ID)
         * @throws DuplicateInstanceException
         *                 if there is another event with the same ID in the
         *                 database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public EventTO create(Connection connection, EventTO eventTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Checks if an event exists in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param eventID
         *                the event identifier
         * @return <code>true</code> if the event exists<br>
         *         <code>false</code> if the event doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, Long eventID)
                        throws InternalErrorException;

        /**
         * Finds an event in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param eventID
         *                the event identifier
         * @return a transfer object with the event data
         * @throws InstanceNotFoundException
         *                 if there is no event with the <code>eventID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public EventTO find(Connection connection, Long eventID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Finds events in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param eventIDs
         *                event identifiers
         * @return a collection of transfer objects with the events data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public Map<Long, EventTO> findByIDs(Connection connection,
                        List<Long> eventIDs) throws InternalErrorException;

        /**
         * Returns a collection of <code>EventTO</code> of a given category.
         * If the category has no events, a collection of zero elements is
         * returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are less
         * than 1 the collection returned has all of the events of the specified
         * category</li>
         * <br>
         * <li> The <code>startDate</code> and the <code>endDate</code> may
         * be <code>null</code> </li>
         * </ul>
         * 
         * 
         * @param connection
         *                the connection to the database
         * @param categoryID
         *                the category identifier
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
         * @return the collection of <code>EventTO</code> with the data of the
         *         events of the specified category
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<EventTO> findByCategory(Connection connection,
                        String categoryID, int startIndex, int count,
                        Calendar startDate, Calendar endDate)
                        throws InternalErrorException;

        /**
         * Finds all the events in the database.
         * 
         * @param connection
         *                the connection to the database
         * @return a collection of transfer objects with the events data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<EventTO> findAll(Connection connection)
                        throws InternalErrorException;

        /**
         * Updates an event in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param eventTO
         *                a transfer object with the new event data
         * @throws InstanceNotFoundException
         *                 if there is no event with the ID included in
         *                 <code>eventTO</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void update(Connection connection, EventTO eventTO)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Removes an event from the database.
         * 
         * @param connection
         *                the connection to the database
         * @param eventID
         *                the event identifier
         * @throws InstanceNotFoundException
         *                 if there is no event with the <code>eventID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void remove(Connection connection, Long eventID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Returns a collection with all the highlighted <code>EventTO</code>s
         * (not only the current three). If there is no highlighted events, a
         * collection of zero elements is returned.
         * <p>
         * 
         * @param connection
         *                the connection to the database
         * @return the collection of <code>EventTO</code> with the data of the
         *         bets of the specified bet type
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<EventTO> findAllHighlighted(Connection connection)
                        throws InternalErrorException;

        /**
         * Returns a collection with the highlighted <code>EventTO</code>s.
         * If there is no highlighted events, a collection of zero elements is
         * returned.
         * <p>
         * 
         * @param connection
         *                the connection to the database
         * @return the collection of <code>EventTO</code> with the data of the
         *         bets of the specified bet type
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<EventTO> findHighlighted(Connection connection)
                        throws InternalErrorException;

        /**
         * Updates highlighted events in the database.
         * 
         * @param connection
         *                the connection to the database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void updateHighlighted(Connection connection)
                        throws InternalErrorException;

        /**
         * Sets or unsets highlighted events in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param eventID
         *                the event identifier
         * @param highlighted
         *                <code>true</code> to set highlighted<br>
         *                <code>false</code> to unset highlighted
         * @throws InstanceNotFoundException
         *                 if there is no event with the <code>eventID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void setHighlighted(Connection connection, Long eventID,
                        boolean highlighted) throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Returns a collection of recent <code>EventTO</code>s of the given
         * categories. If the categories have no unstarted events, a collection
         * of zero elements is returned.
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are equal
         * to -1 the collection returned has all of the events of the specified
         * event</li>
         * </ul>
         * <ul>
         * <li> The <code>startDate</code> and the <code>endDate</code> may
         * be <code>null</code> </li>
         * </ul>
         * 
         * @param connection
         *                the connection to the database
         * @param categoryIDs
         *                a collection of category identifiers
         * @param startIndex
         *                the index (starting from 1) of the first object to
         *                return
         * @param count
         *                the maximum number of objects to return
         * @return the collection of <code>EventTO</code>s with the data of
         *         the events of the specified categories
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<EventTO> findRecent(Connection connection,
                        List<String> categoryIDs, int startIndex, int count)
                        throws InternalErrorException;

}
