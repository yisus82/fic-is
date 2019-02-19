package ubet.model.event.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ubet.model.event.to.EventTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A partial implementation of <code>SQLEventDAO</code> that leaves
 * <code>create(Connection, EventTO)</code> as abstract.
 */
public abstract class AbstractSQLEventDAO implements SQLEventDAO {

        /**
         * Sole constructor. (For invocation by subclass constructors, typically
         * implicit).
         */
        protected AbstractSQLEventDAO() {
        }

        public abstract EventTO create(Connection connection, EventTO eventTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long eventID)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT eventID FROM Event WHERE eventID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, eventID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        return resultSet.next();

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public EventTO find(Connection connection, Long eventID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT description, date, categoryID, betTypeID, "
                                        + "highlighted, insertionDate FROM Event WHERE eventID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, eventID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(eventID,
                                                EventTO.class.getName());
                        }

                        /* Get first result. */
                        i = 1;
                        String description = resultSet.getString(i++);
                        Calendar date = Calendar.getInstance();
                        date.setTime(resultSet.getTimestamp(i++));
                        String categoryID = resultSet.getString(i++);
                        Long betTypeID = resultSet.getLong(i++);
                        String highlighted = resultSet.getString(i++);
                        Calendar insertionDate = Calendar.getInstance();
                        insertionDate.setTime(resultSet.getDate(i++));

                        return new EventTO(eventID, description, date,
                                        categoryID, betTypeID, highlighted,
                                        insertionDate);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public Map<Long, EventTO> findByIDs(Connection connection,
                        List<Long> eventIDs) throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Map<Long, EventTO> results = new HashMap<Long, EventTO>();
                if (eventIDs.isEmpty())
                        return results;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT eventID, description, date, categoryID, betTypeID, "
                                        + "highlighted, insertionDate FROM Event WHERE eventID IN (";
                        int i;
                        for (i = 0; i < eventIDs.size(); i++)
                                queryString += "?, ";
                        queryString = queryString.substring(0, queryString
                                        .length() - 2);
                        queryString += ") ORDER BY eventID";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        i = 1;
                        for (Long eventID : eventIDs)
                                preparedStatement.setLong(i++, eventID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if (resultSet.first()) {
                                do {

                                        i = 1;
                                        Long eventID = resultSet.getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String categoryID = resultSet
                                                        .getString(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        String highlighted = resultSet
                                                        .getString(i++);
                                        Calendar insertionDate = Calendar
                                                        .getInstance();
                                        insertionDate.setTime(resultSet
                                                        .getDate(i++));

                                        results.put(eventID, new EventTO(
                                                        eventID, description,
                                                        date, categoryID,
                                                        betTypeID, highlighted,
                                                        insertionDate));

                                } while (resultSet.next());
                        }
                        return results;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public List<EventTO> findByCategory(Connection connection,
                        String categoryID, int startIndex, int count,
                        Calendar startDate, Calendar endDate)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<EventTO> results = new ArrayList<EventTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT eventID, description, date, betTypeID, "
                                        + "highlighted, insertionDate FROM Event WHERE categoryID = ?";
                        if (startDate != null)
                                queryString += " AND date >= ?";
                        if (endDate != null)
                                queryString += " AND date <= ?";
                        queryString += " ORDER BY date";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, categoryID);
                        if (startDate != null) {
                                Calendar dateWithoutMilliSeconds = DateOperations
                                                .getDateWithoutMilliSeconds(startDate);
                                preparedStatement
                                                .setTimestamp(
                                                                i++,
                                                                new Timestamp(
                                                                                dateWithoutMilliSeconds
                                                                                                .getTime()
                                                                                                .getTime()));
                        }
                        if (endDate != null) {
                                Calendar dateWithoutMilliSeconds = DateOperations
                                                .getDateWithoutMilliSeconds(endDate);
                                preparedStatement
                                                .setTimestamp(
                                                                i++,
                                                                new Timestamp(
                                                                                dateWithoutMilliSeconds
                                                                                                .getTime()
                                                                                                .getTime()));
                        }

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if ((startIndex < 1) && (count < 1)
                                        && resultSet.first()) {

                                do {

                                        i = 1;
                                        Long eventID = resultSet.getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Long betTypeID = resultSet.getLong(i++);
                                        String highlighted = resultSet
                                                        .getString(i++);
                                        Calendar insertionDate = Calendar
                                                        .getInstance();
                                        insertionDate.setTime(resultSet
                                                        .getDate(i++));

                                        results.add(new EventTO(eventID,
                                                        description, date,
                                                        categoryID, betTypeID,
                                                        highlighted,
                                                        insertionDate));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long eventID = resultSet.getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Long betTypeID = resultSet.getLong(i++);
                                        String highlighted = resultSet
                                                        .getString(i++);
                                        Calendar insertionDate = Calendar
                                                        .getInstance();
                                        insertionDate.setTime(resultSet
                                                        .getDate(i++));

                                        results.add(new EventTO(eventID,
                                                        description, date,
                                                        categoryID, betTypeID,
                                                        highlighted,
                                                        insertionDate));
                                        currentCount++;

                                } while (resultSet.next()
                                                && (currentCount < count));
                        }

                        return results;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public List<EventTO> findAll(Connection connection)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<EventTO> results = new ArrayList<EventTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT eventID, description, date, categoryID, betTypeID, "
                                        + "highlighted, insertionDate FROM Event";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if (resultSet.first()) {

                                do {

                                        int i = 1;
                                        Long eventID = resultSet.getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String categoryID = resultSet
                                                        .getString(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        String highlighted = resultSet
                                                        .getString(i++);
                                        Calendar insertionDate = Calendar
                                                        .getInstance();
                                        insertionDate.setTime(resultSet
                                                        .getDate(i++));

                                        results.add(new EventTO(eventID,
                                                        description, date,
                                                        categoryID, betTypeID,
                                                        highlighted,
                                                        insertionDate));

                                } while (resultSet.next());

                        }
                        return results;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void update(Connection connection, EventTO eventTO)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "UPDATE Event SET description = ?, date = ?, "
                                        + "categoryID = ?, betTypeID = ?, highlighted = ?, "
                                        + "insertionDate = ? WHERE eventID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, eventTO
                                        .getDescription());
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(eventTO
                                                        .getDate());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        preparedStatement.setString(i++, eventTO
                                        .getCategoryID());
                        preparedStatement.setLong(i++, eventTO.getBetTypeID());
                        preparedStatement.setString(i++, eventTO
                                        .getHighlighted());
                        dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(eventTO
                                                        .getInsertionDate());
                        preparedStatement.setDate(i++, new Date(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        preparedStatement.setLong(i++, eventTO.getEventID());

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(eventTO
                                                .getEventID(), EventTO.class
                                                .getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for eventID = '"
                                                                + eventTO
                                                                                .getEventID()
                                                                + "' in table 'Event'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void remove(Connection connection, Long eventID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM Event WHERE eventID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, eventID);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(eventID,
                                                EventTO.class.getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public List<EventTO> findAllHighlighted(Connection connection)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<EventTO> results = new ArrayList<EventTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT eventID, description, date, categoryID, betTypeID, "
                                        + "highlighted, insertionDate FROM Event WHERE highlighted <> ? "
                                        + "AND date > ?";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, EventTO.NO);
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(Calendar
                                                        .getInstance());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if (!resultSet.next())
                                return results;

                        do {

                                i = 1;
                                Long eventID = resultSet.getLong(i++);
                                String description = resultSet.getString(i++);
                                Calendar date = Calendar.getInstance();
                                date.setTime(resultSet.getTimestamp(i++));
                                String categoryID = resultSet.getString(i++);
                                Long betTypeID = resultSet.getLong(i++);
                                String highlighted = resultSet.getString(i++);
                                Calendar insertionDate = Calendar.getInstance();
                                insertionDate.setTime(resultSet.getDate(i++));

                                results.add(new EventTO(eventID, description,
                                                date, categoryID, betTypeID,
                                                highlighted, insertionDate));

                        } while (resultSet.next());
                        return results;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public List<EventTO> findHighlighted(Connection connection)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<EventTO> results = new ArrayList<EventTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT eventID, description, date, categoryID, betTypeID, "
                                        + "highlighted, insertionDate FROM Event WHERE highlighted = ?"
                                        + " AND date > ?";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, EventTO.YES);
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(Calendar
                                                        .getInstance());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if (!resultSet.next())
                                return results;

                        do {

                                i = 1;
                                Long eventID = resultSet.getLong(i++);
                                String description = resultSet.getString(i++);
                                Calendar date = Calendar.getInstance();
                                date.setTime(resultSet.getTimestamp(i++));
                                String categoryID = resultSet.getString(i++);
                                Long betTypeID = resultSet.getLong(i++);
                                String highlighted = resultSet.getString(i++);
                                Calendar insertionDate = Calendar.getInstance();
                                insertionDate.setTime(resultSet.getDate(i++));

                                results.add(new EventTO(eventID, description,
                                                date, categoryID, betTypeID,
                                                highlighted, insertionDate));

                        } while (resultSet.next());
                        return results;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void updateHighlighted(Connection connection)
                        throws InternalErrorException {
                List<EventTO> events = findAllHighlighted(connection);

                int i = 0;

                for (EventTO event : events) {
                        if (event.getDate().before(Calendar.getInstance()))
                                event.setHighlighted(EventTO.NO);
                        if (event.getHighlighted().equals(EventTO.YES))
                                event.setHighlighted(EventTO.WAITING);
                        if (event.getHighlighted().equals(EventTO.READY)) {
                                if (i < 3) {
                                        event.setHighlighted(EventTO.YES);
                                        i++;
                                }
                        }
                }

                if (i < 3)
                        for (EventTO event : events) {
                                if (event.getHighlighted().equals(
                                                EventTO.WAITING))
                                        if (i < 3) {
                                                event
                                                                .setHighlighted(EventTO.YES);
                                                i++;
                                        } else
                                                event
                                                                .setHighlighted(EventTO.READY);
                        }

                for (EventTO event : events) {
                        try {
                                update(connection, event);
                        } catch (InstanceNotFoundException e) {
                                throw new InternalErrorException(e);
                        }
                }

        }

        public void setHighlighted(Connection connection, Long eventID,
                        boolean highlighted) throws InstanceNotFoundException,
                        InternalErrorException {
                EventTO event = find(connection, eventID);

                if (highlighted)
                        event.setHighlighted(EventTO.READY);
                else
                        event.setHighlighted(EventTO.NO);

                update(connection, event);
        }

        public List<EventTO> findRecent(Connection connection,
                        List<String> categoryIDs, int startIndex, int count)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<EventTO> results = new ArrayList<EventTO>();
                if (categoryIDs.isEmpty())
                        return results;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT eventID, description, date, categoryID, betTypeID, "
                                        + "highlighted, insertionDate FROM Event e1 WHERE insertionDate IN"
                                        + " (SELECT MAX(insertionDate) FROM Event e2 WHERE e1.categoryID = e2.categoryID"
                                        + " GROUP BY e2.categoryID) AND categoryID IN (";
                        int i;
                        for (i = 0; i < categoryIDs.size(); i++)
                                queryString += "?, ";
                        queryString = queryString.substring(0, queryString
                                        .length() - 2);
                        queryString += ") AND date > ?";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        i = 1;
                        for (String categoryID : categoryIDs)
                                preparedStatement.setString(i++, categoryID);
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(Calendar
                                                        .getInstance());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if ((startIndex < 1) && (count < 1)
                                        && resultSet.first()) {

                                do {

                                        i = 1;
                                        Long eventID = resultSet.getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String categoryID = resultSet
                                                        .getString(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        String highlighted = resultSet
                                                        .getString(i++);
                                        Calendar insertionDate = Calendar
                                                        .getInstance();
                                        insertionDate.setTime(resultSet
                                                        .getDate(i++));

                                        results.add(new EventTO(eventID,
                                                        description, date,
                                                        categoryID, betTypeID,
                                                        highlighted,
                                                        insertionDate));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long eventID = resultSet.getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String categoryID = resultSet
                                                        .getString(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        String highlighted = resultSet
                                                        .getString(i++);
                                        Calendar insertionDate = Calendar
                                                        .getInstance();
                                        insertionDate.setTime(resultSet
                                                        .getDate(i++));

                                        results.add(new EventTO(eventID,
                                                        description, date,
                                                        categoryID, betTypeID,
                                                        highlighted,
                                                        insertionDate));
                                        currentCount++;

                                } while (resultSet.next()
                                                && (currentCount < count));
                        }

                        return results;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
