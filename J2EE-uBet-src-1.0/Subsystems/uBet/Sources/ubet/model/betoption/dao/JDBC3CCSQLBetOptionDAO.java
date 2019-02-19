package ubet.model.betoption.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ubet.model.betoption.to.BetOptionTO;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLBetOptionDAO</code> for databases providing
 * counter columns (CC) with JDBC 3 drivers.
 */
public class JDBC3CCSQLBetOptionDAO extends AbstractSQLBetOptionDAO {

        @Override
        public BetOptionTO create(Connection connection, BetOptionTO betOptionTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO BetOption (description,"
                                        + " odds, betTypeID, status) VALUES (?, ?, ?, ?)";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        Statement.RETURN_GENERATED_KEYS);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, betOptionTO
                                        .getDescription());
                        preparedStatement.setDouble(i++, betOptionTO.getOdds());
                        preparedStatement.setLong(i++, betOptionTO
                                        .getBetTypeID());
                        preparedStatement.setString(i++, betOptionTO
                                        .getStatus());

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'BetOption'");
                        }

                        /* Get identifier. */
                        resultSet = preparedStatement.getGeneratedKeys();

                        if (!resultSet.next()) {
                                throw new InternalErrorException(
                                                new SQLException(
                                                                "JDBC driver did not return generated key."));
                        }
                        Long betOptionID = resultSet.getLong(1);
                        betOptionTO.setBetOptionID(betOptionID);

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for BetOption ID = '"
                                                                + betOptionTO
                                                                                .getBetOptionID()
                                                                + "' in table 'BetOption'");
                        }

                        return betOptionTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
