package ibei.model.bid.dao;

import ibei.model.bid.to.BidTO;
import ibei.model.util.DateOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class JDBC3CCSQLBidDAO extends AbstractSQLBidDAO {

        public BidTO create(Connection connection, BidTO bidTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Bid (userID, productID, currentBid,"
                                        + " maxBid, date) VALUES (?, ?, ?, ?, ?)";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        Statement.RETURN_GENERATED_KEYS);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, bidTO.getUserID());
                        preparedStatement.setLong(i++, bidTO.getProductID());
                        preparedStatement.setDouble(i++, bidTO.getCurrentBid());
                        preparedStatement.setDouble(i++, bidTO.getMaxBid());
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(bidTO
                                                        .getDate());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'Bid'");
                        }

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for bid ID = '"
                                                                + bidTO
                                                                                .getBidID()
                                                                + "' in table 'Bid'");
                        }

                        /* Get identifier. */
                        resultSet = preparedStatement.getGeneratedKeys();

                        if (!resultSet.next()) {
                                throw new InternalErrorException(
                                                new SQLException(
                                                                "JDBC driver did not return generated key."));
                        }
                        Long bidID = resultSet.getLong(1);
                        bidTO.setBidID(bidID);
                        return bidTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

}
