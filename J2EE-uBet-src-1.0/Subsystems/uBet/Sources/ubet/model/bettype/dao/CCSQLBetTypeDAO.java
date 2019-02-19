package ubet.model.bettype.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ubet.model.bettype.to.BetTypeTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetriever;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetrieverFactory;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLBetTypeDAO</code> for databases with counter
 * columns (CC) without JDBC 3 drivers. The implementation uses
 * {@link es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetriever}.
 */
public class CCSQLBetTypeDAO extends AbstractSQLBetTypeDAO {

        @Override
        public BetTypeTO create(Connection connection, BetTypeTO betTypeTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO BetType (eventID, questionID) VALUES (?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

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
                        EntityIdentifierRetriever entityIdentifierRetriever = EntityIdentifierRetrieverFactory
                                        .getRetriever();
                        Long betTypeID = entityIdentifierRetriever
                                        .getGeneratedIdentifier(connection);

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
