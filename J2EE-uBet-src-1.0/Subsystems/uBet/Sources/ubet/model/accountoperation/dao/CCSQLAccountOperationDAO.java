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
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetriever;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetrieverFactory;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLAccountDAO</code> for databases with counter
 * columns (CC) without JDBC 3 drivers. The implementation uses
 * {@link es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetriever}.
 */
public class CCSQLAccountOperationDAO extends AbstractSQLAccountOperationDAO {

        @Override
        public AccountOperationTO create(Connection connection,
                        AccountOperationTO accountOperationTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO AccountOperation (accountID, amount,"
                                        + " type, description, date, betID) VALUES (?, ?, ?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
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

                        /* Get identifier. */
                        EntityIdentifierRetriever entityIdentifierRetriever = EntityIdentifierRetrieverFactory
                                        .getRetriever();
                        Long accountOperationID = entityIdentifierRetriever
                                        .getGeneratedIdentifier(connection);

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
