package ubet.model.account.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import ubet.model.account.to.AccountTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGeneratorFactory;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLAccountDAO</code> for databases with
 * identifier generators (IG). The implementation uses
 * {@link es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator} with "Account"
 * as the name of the entity.
 */
public class IGSQLAccountDAO extends AbstractSQLAccountDAO {

        @Override
        public AccountTO create(Connection connection, AccountTO accountTO)
                        throws InternalErrorException {

                PreparedStatement preparedStatement = null;

                try {

                        /* Get identifier. */
                        EntityIdentifierGenerator entityIdentifierGenerator = EntityIdentifierGeneratorFactory
                                        .getGenerator();
                        Long accountID = entityIdentifierGenerator
                                        .nextIdentifier(connection, "Account");

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Account (accountID, userID,"
                                        + " creditCardNumber, expirationDate, balance) VALUES (?, ?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountID);
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
