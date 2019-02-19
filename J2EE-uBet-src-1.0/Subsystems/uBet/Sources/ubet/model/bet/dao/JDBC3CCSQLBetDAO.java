package ubet.model.bet.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import ubet.model.bet.to.BetTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLBetDAO</code> for databases providing counter
 * columns (CC) with JDBC 3 drivers.
 */
public class JDBC3CCSQLBetDAO extends AbstractSQLBetDAO {

        @Override
        public BetTO create(Connection connection, BetTO betTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Bet (betTypeID, betOptionID,"
                                        + " accountID, eventID, amount, date, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        Statement.RETURN_GENERATED_KEYS);

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

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'Bet'");
                        }

                        /* Get identifier. */
                        resultSet = preparedStatement.getGeneratedKeys();

                        if (!resultSet.next()) {
                                throw new InternalErrorException(
                                                new SQLException(
                                                                "JDBC driver did not return generated key."));
                        }
                        Long betID = resultSet.getLong(1);
                        betTO.setBetID(betID);

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for Bet ID = '"
                                                                + betTO
                                                                                .getBetID()
                                                                + "' in table 'Bet'");
                        }

                        return betTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
