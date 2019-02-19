package ubet.model.account.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import ubet.model.account.to.AccountTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLAccountDAO</code> for databases providing
 * counter columns (CC) with JDBC 3 drivers.
 */
public class JDBC3CCSQLAccountDAO extends AbstractSQLAccountDAO {

        @Override
        public AccountTO create(Connection connection, AccountTO accountTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Account (userID, creditCardNumber,"
                                        + " expirationDate, balance) VALUES (?, ?, ?, ?)";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        Statement.RETURN_GENERATED_KEYS);

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
                        resultSet = preparedStatement.getGeneratedKeys();

                        if (!resultSet.next()) {
                                throw new InternalErrorException(
                                                new SQLException(
                                                                "JDBC driver did not return generated key."));
                        }
                        Long accountID = resultSet.getLong(1);
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
