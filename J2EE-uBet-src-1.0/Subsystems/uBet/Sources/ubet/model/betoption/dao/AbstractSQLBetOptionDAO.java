package ubet.model.betoption.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ubet.model.betoption.to.BetOptionTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A partial implementation of <code>SQLBetOptionDAO</code> that leaves
 * <code>create(Connection, BetOptionTO)</code> as abstract.
 */
public abstract class AbstractSQLBetOptionDAO implements SQLBetOptionDAO {

        /**
         * Sole constructor. (For invocation by subclass constructors, typically
         * implicit).
         */
        protected AbstractSQLBetOptionDAO() {
        }

        public abstract BetOptionTO create(Connection connection,
                        BetOptionTO betOptionTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long betOptionID)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betOptionID FROM BetOption WHERE betOptionID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betOptionID);

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

        public BetOptionTO find(Connection connection, Long betOptionID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT description, "
                                        + "odds, betTypeID, status FROM BetOption WHERE betOptionID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betOptionID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(
                                                betOptionID, BetOptionTO.class
                                                                .getName());
                        }

                        /* Get first result. */
                        i = 1;
                        String description = resultSet.getString(i++);
                        Double odds = resultSet.getDouble(i++);
                        Long betTypeID = resultSet.getLong(i++);
                        String status = resultSet.getString(i++);

                        return new BetOptionTO(betOptionID, description, odds,
                                        betTypeID, status);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public Map<Long, BetOptionTO> findByIDs(Connection connection,
                        List<Long> betOptionIDs) throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Map<Long, BetOptionTO> results = new HashMap<Long, BetOptionTO>();
                if (betOptionIDs.isEmpty())
                        return results;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betOptionID, description, "
                                        + "odds, betTypeID, status FROM BetOption WHERE betOptionID IN (";
                        int i;
                        for (i = 0; i < betOptionIDs.size(); i++)
                                queryString += "?, ";
                        queryString = queryString.substring(0, queryString
                                        .length() - 2);
                        queryString += ") ORDER BY betOptionID";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        i = 1;
                        for (Long betOptionID : betOptionIDs)
                                preparedStatement.setLong(i++, betOptionID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if (resultSet.first()) {

                                do {

                                        i = 1;
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Double odds = resultSet.getDouble(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        String status = resultSet
                                                        .getString(i++);

                                        results
                                                        .put(
                                                                        betOptionID,
                                                                        new BetOptionTO(
                                                                                        betOptionID,
                                                                                        description,
                                                                                        odds,
                                                                                        betTypeID,
                                                                                        status));

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

        public List<BetOptionTO> findByBetType(Connection connection,
                        Long betTypeID, int startIndex, int count)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<BetOptionTO> results = new ArrayList<BetOptionTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betOptionID, description, "
                                        + "odds, status FROM BetOption WHERE betTypeID = ? ORDER BY betOptionID";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTypeID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if ((startIndex < 1) && (count < 1)
                                        && resultSet.first()) {

                                do {

                                        i = 1;
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Double odds = resultSet.getDouble(i++);
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetOptionTO(
                                                        betOptionID,
                                                        description, odds,
                                                        betTypeID, status));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Double odds = resultSet.getDouble(i++);
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetOptionTO(
                                                        betOptionID,
                                                        description, odds,
                                                        betTypeID, status));
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

        public List<BetOptionTO> findWinners(Connection connection,
                        Long betTypeID) throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<BetOptionTO> results = new ArrayList<BetOptionTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betOptionID, description, "
                                        + "odds, status FROM BetOption WHERE betTypeID = ? AND status = 'W' ORDER BY betOptionID";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTypeID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if (resultSet.first()) {

                                do {

                                        i = 1;
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Double odds = resultSet.getDouble(i++);
                                        String status = resultSet
                                                        .getString(i++);

                                        results.add(new BetOptionTO(
                                                        betOptionID,
                                                        description, odds,
                                                        betTypeID, status));

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

        public Map<Long, List<BetOptionTO>> findWinnersByIDs(
                        Connection connection, List<Long> betTypeIDs)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Map<Long, List<BetOptionTO>> results = new HashMap<Long, List<BetOptionTO>>();
                if (betTypeIDs.isEmpty())
                        return results;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT betOptionID, description, "
                                        + "odds, betTypeID, status FROM BetOption WHERE status = 'W' AND betTypeID IN (";
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
                        List<BetOptionTO> betOptions = new ArrayList<BetOptionTO>();

                        if (resultSet.first()) {

                                do {

                                        i = 1;
                                        Long betOptionID = resultSet
                                                        .getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Double odds = resultSet.getDouble(i++);
                                        Long betTypeID = resultSet.getLong(i++);
                                        String status = resultSet
                                                        .getString(i++);

                                        betOptions.add(new BetOptionTO(
                                                        betOptionID,
                                                        description, odds,
                                                        betTypeID, status));

                                } while (resultSet.next());

                        }

                        List<BetOptionTO> aux;
                        for (Long betTypeID : betTypeIDs) {
                                aux = new ArrayList<BetOptionTO>();
                                for (BetOptionTO betOption : betOptions) {
                                        if (betOption.getBetTypeID() > betTypeID)
                                                break;
                                        if (betOption.getBetTypeID().equals(
                                                        betTypeID))
                                                aux.add(betOption);
                                }
                                results.put(betTypeID, aux);
                        }

                        return results;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void update(Connection connection, BetOptionTO betOptionTO)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "UPDATE BetOption SET description = ?, odds = ?, betTypeID = ?, "
                                        + "status = ? WHERE betOptionID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, betOptionTO
                                        .getDescription());
                        preparedStatement.setDouble(i++, betOptionTO.getOdds());
                        preparedStatement.setLong(i++, betOptionTO
                                        .getBetTypeID());
                        preparedStatement.setString(i++, betOptionTO
                                        .getStatus());
                        preparedStatement.setLong(i++, betOptionTO
                                        .getBetOptionID());

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(betOptionTO
                                                .getBetOptionID(),
                                                BetOptionTO.class.getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for betOptionID = '"
                                                                + betOptionTO
                                                                                .getBetOptionID()
                                                                + "' in table 'BetOption'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void remove(Connection connection, Long betOptionID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM BetOption WHERE betOptionID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betOptionID);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(
                                                betOptionID, BetOptionTO.class
                                                                .getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
