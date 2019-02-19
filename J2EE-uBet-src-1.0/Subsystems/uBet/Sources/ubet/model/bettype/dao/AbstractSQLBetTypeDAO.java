package ubet.model.bettype.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ubet.model.bettype.to.BetTypeTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A partial implementation of <code>SQLBetTypeDAO</code> that leaves
 * <code>create(Connection, BetTypeTO)</code> as abstract.
 */
public abstract class AbstractSQLBetTypeDAO implements SQLBetTypeDAO {

        /**
         * Sole constructor. (For invocation by subclass constructors, typically
         * implicit).
         */
        protected AbstractSQLBetTypeDAO() {
        }

        public abstract BetTypeTO create(Connection connection,
                        BetTypeTO betTypeTO) throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long betTypeID)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betTypeID FROM BetType WHERE betTypeID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTypeID);

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

        public BetTypeTO find(Connection connection, Long betTypeID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT eventID, questionID FROM BetType WHERE betTypeID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTypeID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(betTypeID,
                                                BetTypeTO.class.getName());
                        }

                        /* Get first result. */
                        i = 1;
                        Long eventID = resultSet.getLong(i++);
                        Long questionID = resultSet.getLong(i++);

                        return new BetTypeTO(betTypeID, eventID, questionID);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public Map<Long, BetTypeTO> findByIDs(Connection connection,
                        List<Long> betTypeIDs) throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Map<Long, BetTypeTO> results = new HashMap<Long, BetTypeTO>();
                if (betTypeIDs.isEmpty())
                        return results;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betTypeID, eventID, questionID FROM BetType WHERE betTypeID IN (";
                        int i;
                        for (i = 0; i < betTypeIDs.size(); i++)
                                queryString += "?, ";
                        queryString = queryString.substring(0, queryString
                                        .length() - 2);
                        queryString += ") ORDER BY betTypeID";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        i = 1;
                        for (Long betTypeID : betTypeIDs)
                                preparedStatement.setLong(i++, betTypeID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if (resultSet.first()) {

                                do {

                                        i = 1;
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long eventID = resultSet.getLong(i++);
                                        Long questionID = resultSet
                                                        .getLong(i++);

                                        results.put(betTypeID, new BetTypeTO(
                                                        betTypeID, eventID,
                                                        questionID));

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

        public List<BetTypeTO> findByEvent(Connection connection, Long eventID,
                        int startIndex, int count)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<BetTypeTO> results = new ArrayList<BetTypeTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betTypeID, questionID FROM BetType WHERE eventID = ? ORDER BY betTypeID";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, eventID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if ((startIndex < 1) && (count < 1)
                                        && resultSet.first()) {

                                do {

                                        i = 1;
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long questionID = resultSet
                                                        .getLong(i++);

                                        results.add(new BetTypeTO(betTypeID,
                                                        eventID, questionID));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long questionID = resultSet
                                                        .getLong(i++);

                                        results.add(new BetTypeTO(betTypeID,
                                                        eventID, questionID));
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

        public List<BetTypeTO> findByQuestion(Connection connection,
                        Long questionID, int startIndex, int count)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<BetTypeTO> results = new ArrayList<BetTypeTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betTypeID, eventID FROM BetType WHERE questionID = ? ORDER BY betTypeID";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, questionID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if ((startIndex < 1) && (count < 1)
                                        && resultSet.first()) {

                                do {

                                        i = 1;
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long eventID = resultSet.getLong(i++);

                                        results.add(new BetTypeTO(betTypeID,
                                                        eventID, questionID));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long betTypeID = resultSet.getLong(i++);
                                        Long eventID = resultSet.getLong(i++);

                                        results.add(new BetTypeTO(betTypeID,
                                                        eventID, questionID));
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

        public void update(Connection connection, BetTypeTO betTypeTO)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "UPDATE BetType SET eventID = ?, questionID = ? WHERE betTypeID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTypeTO.getEventID());
                        preparedStatement.setLong(i++, betTypeTO
                                        .getQuestionID());
                        preparedStatement
                                        .setLong(i++, betTypeTO.getBetTypeID());

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(betTypeTO
                                                .getBetTypeID(),
                                                BetTypeTO.class.getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for betTypeID = '"
                                                                + betTypeTO
                                                                                .getBetTypeID()
                                                                + "' in table 'BetType'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void remove(Connection connection, Long betTypeID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM BetType WHERE betTypeID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTypeID);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(betTypeID,
                                                BetTypeTO.class.getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
