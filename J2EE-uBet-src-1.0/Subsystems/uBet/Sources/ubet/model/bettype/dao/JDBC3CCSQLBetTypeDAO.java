package ubet.model.bettype.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ubet.model.bettype.to.BetTypeTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLBetTypeDAO</code> for databases providing
 * counter columns (CC) with JDBC 3 drivers.
 */
public class JDBC3CCSQLBetTypeDAO extends AbstractSQLBetTypeDAO {

        @Override
        public BetTypeTO create(Connection connection, BetTypeTO betTypeTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO BetType (eventID, questionID) VALUES (?, ?)";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        Statement.RETURN_GENERATED_KEYS);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTypeTO.getEventID());
                        preparedStatement.setDouble(i++, betTypeTO
                                        .getQuestionID());

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'BetType'");
                        }

                        /* Get identifier. */
                        resultSet = preparedStatement.getGeneratedKeys();

                        if (!resultSet.next()) {
                                throw new InternalErrorException(
                                                new SQLException(
                                                                "JDBC driver did not return generated key."));
                        }
                        Long betTypeID = resultSet.getLong(1);
                        betTypeTO.setBetTypeID(betTypeID);

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for BetType ID = '"
                                                                + betTypeTO
                                                                                .getBetTypeID()
                                                                + "' in table 'BetType'");
                        }

                        return betTypeTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
