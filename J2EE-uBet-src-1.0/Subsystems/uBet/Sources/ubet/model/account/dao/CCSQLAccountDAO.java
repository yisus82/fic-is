package ubet.model.account.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import ubet.model.account.to.AccountTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetriever;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetrieverFactory;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLAccountDAO</code> for databases with counter
 * columns (CC) without JDBC 3 drivers. The implementation uses
 * {@link es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetriever}.
 */
public class CCSQLAccountDAO extends AbstractSQLAccountDAO {

        @Override
        public AccountTO create(Connection connection, AccountTO accountTO)
                        throws InternalErrorException {

                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Account (userID, creditCardNumber,"
                                        + " expirationDate, balance) VALUES (?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, accountTO.getUserID());
                        preparedStatement.setString(i++, accountTO
                                        .getCreditCardNumber());
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(accountTO
                                                        .getExpirationDate());
                        preparedStatement.setDate(i++, new Date(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        preparedStatement
                                        .setDouble(i++, accountTO.getBalance());

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'Account'");
                        }

                        /* Get identifier. */
                        EntityIdentifierRetriever entityIdentifierRetriever = EntityIdentifierRetrieverFactory
                                        .getRetriever();
                        Long accountID = entityIdentifierRetriever
                                        .getGeneratedIdentifier(connection);

                        accountTO.setAccountID(accountID);

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for account ID = '"
                                                                + accountTO
                                                                                .getAccountID()
                                                                + "' in table 'Account'");
                        }

                        return accountTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

}
