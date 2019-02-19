package ubet.model.bettype.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ubet.model.bettype.to.BetTypeTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGeneratorFactory;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLBetTypeDAO</code> for databases with
 * identifier generators (IG). The implementation uses
 * {@link es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator} with "BetType"
 * as the name of the entity.
 */
public class IGSQLBetTypeDAO extends AbstractSQLBetTypeDAO {

        @Override
        public BetTypeTO create(Connection connection, BetTypeTO betTypeTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {
                        /* Get identifier. */
                        EntityIdentifierGenerator entityIdentifierGenerator = EntityIdentifierGeneratorFactory
                                        .getGenerator();
                        Long betTypeID = entityIdentifierGenerator
                                        .nextIdentifier(connection, "BetType");

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO BetType (betTypeID, eventID, questionID) VALUES (?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, betTypeID);
                        preparedStatement.setLong(i++, betTypeTO.getEventID());
                        preparedStatement.setDouble(i++, betTypeTO
                                        .getQuestionID());

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'BetType'");
                        }

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
