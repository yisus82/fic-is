package ibei.model.bid.dao;

import ibei.model.bid.to.BidTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public abstract class AbstractSQLBidDAO implements SQLBidDAO {

        public abstract BidTO create(Connection connection, BidTO bidVO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long bidID)
                        throws InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT bidID FROM Bid WHERE bidID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, bidID);

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

        public BidTO find(Connection connection, Long bidID)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT userID, productID, currentBid, maxBid, "
                                        + "date FROM Bid WHERE bidID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, bidID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(bidID,
                                                BidTO.class.getName());
                        }

                        /* Get first result. */
                        i = 1;
                        String userID = resultSet.getString(i++);
                        Long productID = resultSet.getLong(i++);
                        Double currentBid = resultSet.getDouble(i++);
                        Double maxBid = resultSet.getDouble(i++);
                        Calendar date = Calendar.getInstance();
                        date.setTime(resultSet.getTimestamp(i++));

                        /* Return the value object. */
                        return new BidTO(bidID, userID, productID, currentBid,
                                        maxBid, date);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

        public BidTO findMax(Connection connection, Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT bidID, userID, currentBid, maxBid, "
                                        + "date FROM Bid WHERE productID = ? ORDER by date";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, productID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next())
                                return null;

                        /* Get first result. */
                        i = 1;
                        Long bidID = resultSet.getLong(i++);
                        String userID = resultSet.getString(i++);
                        Double currentBid = resultSet.getDouble(i++);
                        Double maxBid = resultSet.getDouble(i++);
                        Calendar date = Calendar.getInstance();
                        date.setTime(resultSet.getTimestamp(i++));

                        /* Return the value object. */
                        return new BidTO(bidID, userID, productID, currentBid,
                                        maxBid, date);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

        public Vector<BidTO> findByProduct(Connection connection,
                        Long productID, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Vector<BidTO> results = new Vector<BidTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT bidID, userID, currentBid, "
                                        + "maxBid, date FROM Bid WHERE productID = ? ORDER BY date DESC";

                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, productID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */
                        if ((startIndex == -1) && (count == -1)
                                        && resultSet.first()) {

                                do {

                                        i = 1;
                                        Long bidID = resultSet.getLong(i++);
                                        String userID = resultSet
                                                        .getString(i++);
                                        Double currentBid = resultSet
                                                        .getDouble(i++);
                                        Double maxBid = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));

                                        results.add(new BidTO(bidID, userID,
                                                        productID, currentBid,
                                                        maxBid, date));

                                } while (resultSet.next());
                                return results;

                        }

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long bidID = resultSet.getLong(i++);
                                        String userID = resultSet
                                                        .getString(i++);
                                        Double currentBid = resultSet
                                                        .getDouble(i++);
                                        Double maxBid = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));

                                        results.add(new BidTO(bidID, userID,
                                                        productID, currentBid,
                                                        maxBid, date));

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

        public Vector<BidTO> findByUser(Connection connection, String userID,
                        int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Vector<BidTO> results = new Vector<BidTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT bidID, productID, "
                                        + "currentBid, maxBid, date FROM Bid WHERE userID = ? "
                                        + "ORDER BY date DESC";

                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, userID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */
                        if ((startIndex == -1) && (count == -1)
                                        && resultSet.first()) {

                                do {

                                        i = 1;
                                        Long bidID = resultSet.getLong(i++);
                                        Long productID = resultSet.getLong(i++);
                                        Double currentBid = resultSet
                                                        .getDouble(i++);
                                        Double maxBid = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));

                                        results.add(new BidTO(bidID, userID,
                                                        productID, currentBid,
                                                        maxBid, date));

                                } while (resultSet.next());
                                return results;

                        }
                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long bidID = resultSet.getLong(i++);
                                        Long productID = resultSet.getLong(i++);
                                        Double currentBid = resultSet
                                                        .getDouble(i++);
                                        Double maxBid = resultSet
                                                        .getDouble(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));

                                        results.add(new BidTO(bidID, userID,
                                                        productID, currentBid,
                                                        maxBid, date));

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

        public void update(Connection connection, BidTO bidTO)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "UPDATE Bid SET currentBid = ? "
                                        + "WHERE bidID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setDouble(i++, bidTO.getCurrentBid());
                        preparedStatement.setLong(i++, bidTO.getBidID());

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(bidTO
                                                .getBidID(), BidTO.class
                                                .getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for bid ID = '"
                                                                + bidTO
                                                                                .getBidID()
                                                                + "' in table 'Bid'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

        public void remove(Connection connection, Long bidID)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM Bid WHERE bidID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, bidID);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(bidID,
                                                BidTO.class.getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

}
