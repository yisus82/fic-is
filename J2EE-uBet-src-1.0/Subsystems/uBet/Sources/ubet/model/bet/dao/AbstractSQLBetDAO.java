package ubet.model.bet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ubet.model.bet.to.BetTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A partial implementation of <code>SQLBetDAO</code> that leaves
 * <code>create(Connection, BetTO)</code> as abstract.
 */
public abstract class AbstractSQLBetDAO implements SQLBetDAO {

        /**
         * Sole constructor. (For invocation by subclass constructors, typically
         * implicit).
         */
        protected AbstractSQLBetDAO() {
        }

        public abstract BetTO create(Connection connection, BetTO betTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long betID)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betID FROM Bet WHERE betID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betID);

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

        public BetTO find(Connection connection, Long betID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betID, betTypeID, betOptionID, accountID, "
                                        + "eventID, amount, date, status FROM Bet WHERE betID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(betID,
                                                BetTO.class.getName());
                        }

                        /* Get first result. */
                        i = 1;

                        Long betTypeID = resultSet.getLong(i++);
                        Long betOptionID = resultSet.getLong(i++);
                        Long accountID = resultSet.getLong(i++);
                        Long eventID = resultSet.getLong(i++);
                        Double amount = resultSet.getDouble(i++);
                        Calendar date = Calendar.getInstance();
                        date.setTime(resultSet.getTimestamp(i++));
                        String status = resultSet.getString(i++);

                        return new BetTO(betID, betTypeID, betOptionID,
                                        accountID, eventID, amount, date,
                                        status);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public List<BetTO> findByAccount(Connection connection, Long accountID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<BetTO> results = new ArrayList<BetTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betID, betTypeID, betOptionID, "
                                        + "eventID, amount, date, status FROM Bet WHERE accountID = ?";
                        if (startDate != null)
                                queryString += " AND date >= ?";
                        if (endDate != null)
                                queryString += " AND date <= ?";
                        queryString += " ORDER BY date DESC";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountID);
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
                                        Long betID = resultSet.getLong(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        Long eventID = resultSet.getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetTO(betID, betTypeID,
                                                        betOptionID, accountID,
                                                        eventID, amount, date,
                                                        status));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long betID = resultSet.getLong(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        Long eventID = resultSet.getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetTO(betID, betTypeID,
                                                        betOptionID, accountID,
                                                        eventID, amount, date,
                                                        status));

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

        public List<BetTO> findByEvent(Connection connection, Long eventID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<BetTO> results = new ArrayList<BetTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betID, betTypeID, betOptionID, "
                                        + "accountID, amount, date, status FROM Bet WHERE eventID = ?";
                        if (startDate != null)
                                queryString += " AND date >= ?";
                        if (endDate != null)
                                queryString += " AND date <= ?";
                        queryString += " ORDER BY date DESC";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, eventID);
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
                                        Long betID = resultSet.getLong(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        Long accountID = resultSet.getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetTO(betID, betTypeID,
                                                        betOptionID, accountID,
                                                        eventID, amount, date,
                                                        status));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long betID = resultSet.getLong(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        Long accountID = resultSet.getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetTO(betID, betTypeID,
                                                        betOptionID, accountID,
                                                        eventID, amount, date,
                                                        status));

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

        public List<BetTO> findByBetType(Connection connection, Long betTypeID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<BetTO> results = new ArrayList<BetTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betID, betOptionID, accountID,"
                                        + "eventID, amount, date, status FROM Bet WHERE betTypeID = ?";
                        if (startDate != null)
                                queryString += " AND date >= ?";
                        if (endDate != null)
                                queryString += " AND date <= ?";
                        queryString += " ORDER BY date DESC";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTypeID);
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
                                        Long betID = resultSet.getLong(i++);
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        Long accountID = resultSet.getLong(i++);
                                        Long eventID = resultSet.getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetTO(betID, betTypeID,
                                                        betOptionID, accountID,
                                                        eventID, amount, date,
                                                        status));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long betID = resultSet.getLong(i++);
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        Long accountID = resultSet.getLong(i++);
                                        Long eventID = resultSet.getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetTO(betID, betTypeID,
                                                        betOptionID, accountID,
                                                        eventID, amount, date,
                                                        status));

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

        public List<BetTO> findByBetOption(Connection connection,
                        Long betOptionID, int startIndex, int count,
                        Calendar startDate, Calendar endDate)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<BetTO> results = new ArrayList<BetTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betID, betTypeID, accountID,"
                                        + "eventID, amount, date, status FROM Bet WHERE betOptionID = ?";
                        if (startDate != null)
                                queryString += " AND date >= ?";
                        if (endDate != null)
                                queryString += " AND date <= ?";
                        queryString += " ORDER BY date DESC";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betOptionID);
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
                                        Long betID = resultSet.getLong(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long accountID = resultSet.getLong(i++);
                                        Long eventID = resultSet.getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetTO(betID, betTypeID,
                                                        betOptionID, accountID,
                                                        eventID, amount, date,
                                                        status));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long betID = resultSet.getLong(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long accountID = resultSet.getLong(i++);
                                        Long eventID = resultSet.getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetTO(betID, betTypeID,
                                                        betOptionID, accountID,
                                                        eventID, amount, date,
                                                        status));

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

        public void update(Connection connection, BetTO betTO)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "UPDATE Bet SET betTypeID = ?, betOptionID = ?, "
                                        + "accountID = ?, eventID = ?, amount = ?, date = ?, status = ? WHERE betID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTO.getBetTypeID());
                        preparedStatement.setLong(i++, betTO.getBetOptionID());
                        preparedStatement.setLong(i++, betTO.getAccountID());
                        preparedStatement.setLong(i++, betTO.getEventID());
                        preparedStatement.setDouble(i++, betTO.getAmount());
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(betTO
                                                        .getDate());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        preparedStatement.setString(i++, betTO.getStatus());
                        preparedStatement.setLong(i++, betTO.getBetID());

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(betTO
                                                .getBetID(), BetTO.class
                                                .getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for betID = '"
                                                                + betTO
                                                                                .getBetID()
                                                                + "' in table 'Bet'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void remove(Connection connection, Long betID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM Bet WHERE betID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betID);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(betID,
                                                BetTO.class.getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
