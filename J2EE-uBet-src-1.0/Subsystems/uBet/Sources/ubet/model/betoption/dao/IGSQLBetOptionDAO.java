package ubet.model.betoption.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ubet.model.betoption.to.BetOptionTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGeneratorFactory;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLBetOptionDAO</code> for databases with
 * identifier generators (IG). The implementation uses
 * {@link es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator} with
 * "BetOption" as the name of the entity.
 */
public class IGSQLBetOptionDAO extends AbstractSQLBetOptionDAO {

        @Override
        public BetOptionTO create(Connection connection, BetOptionTO betOptionTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {
                        /* Get identifier. */
                        EntityIdentifierGenerator entityIdentifierGenerator = EntityIdentifierGeneratorFactory
                                        .getGenerator();
                        Long betOptionID = entityIdentifierGenerator
                                        .nextIdentifier(connection, "BetOption");

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO BetOption (betOptionID, description,"
                                        + " odds, betTypeID, status) VALUES (?, ?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betOptionID);
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
