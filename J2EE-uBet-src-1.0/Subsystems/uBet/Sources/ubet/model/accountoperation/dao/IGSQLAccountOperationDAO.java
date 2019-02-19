package ubet.model.accountoperation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGeneratorFactory;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLAccountOperationDAO</code> for databases with
 * identifier generators (IG). The implementation uses
 * {@link es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator} with
 * "AccountOperation" as the name of the entity.
 */
public class IGSQLAccountOperationDAO extends AbstractSQLAccountOperationDAO {

        @Override
        public AccountOperationTO create(Connection connection,
                        AccountOperationTO accountOperationTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Get identifier. */
                        EntityIdentifierGenerator entityIdentifierGenerator = EntityIdentifierGeneratorFactory
                                        .getGenerator();
                        Long accountOperationID = entityIdentifierGenerator
                                        .nextIdentifier(connection,
                                                        "AccountOperation");

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO AccountOperation (accountOperationID,"
                                        + " accountID, amount, type, description, date, betID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountOperationID);
                        preparedStatement.setLong(i++, accountOperationTO
                                        .getAccountID());
                        preparedStatement.setDouble(i++, accountOperationTO
                                        .getAmount());
                        preparedStatement.setString(i++, accountOperationTO
                                        .getType());
                        preparedStatement.setString(i++, accountOperationTO
                                        .getDescription());
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(accountOperationTO
                                                        .getDate());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        if (accountOperationTO.getBetID() != null)
                                preparedStatement.setLong(i++,
                                                accountOperationTO.getBetID());
                        else
                                preparedStatement.setNull(i++, Types.NULL);

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'AccountOperation'");
                        }

                        accountOperationTO
                                        .setAccountOperationID(accountOperationID);

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for AccountOperation ID = '"
                                                                + accountOperationTO
                                                                                .getAccountOperationID()
                                                                + "' in table 'AccountOperation'");
                        }

                        return accountOperationTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
