package ubet.model.account.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ubet.model.account.to.AccountTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A partial implementation of <code>SQLAccountDAO</code> that leaves
 * <code>create(Connection, AccountTO)</code> as abstract.
 */
public abstract class AbstractSQLAccountDAO implements SQLAccountDAO {

        /**
         * Sole constructor. (For invocation by subclass constructors, typically
         * implicit).
         */
        protected AbstractSQLAccountDAO() {
        }

        public abstract AccountTO create(Connection connection,
                        AccountTO accountTO) throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long accountID)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT accountID FROM Account WHERE accountID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        return resultSet.next();

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public AccountTO find(Connection connection, Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT userID, creditCardNumber, expirationDate, "
                                        + "balance FROM Account WHERE accountID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(accountID,
                                                AccountTO.class.getName());
                        }

                        /* Get first result. */
                        i = 1;
                        String userID = resultSet.getString(i++);
                        String creditCardNumber = resultSet.getString(i++);
                        Calendar expirationDate = Calendar.getInstance();
                        expirationDate.setTime(resultSet.getDate(i++));
                        Double balance = resultSet.getDouble(i++);

                        return new AccountTO(accountID, userID,
                                        creditCardNumber, expirationDate,
                                        balance);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public List<AccountTO> findByUser(Connection connection, String login,
                        int startIndex, int count)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<AccountTO> results = new ArrayList<AccountTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT accountID, creditCardNumber, expirationDate, "
                                        + "balance FROM Account WHERE userID = ? ORDER BY accountID";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, login);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */
                        if ((startIndex < 1) && (count < 1)
                                        && resultSet.first()) {

                                do {

                                        i = 1;
                                        Long accountID = resultSet.getLong(i++);
                                        String creditCardNumber = resultSet
                                                        .getString(i++);
                                        Calendar expirationDate = Calendar
                                                        .getInstance();
                                        expirationDate.setTime(resultSet
                                                        .getDate(i++));
                                        Double balance = resultSet
                                                        .getDouble(i++);

                                        results
                                                        .add(new AccountTO(
                                                                        accountID,
                                                                        login,
                                                                        creditCardNumber,
                                                                        expirationDate,
                                                                        balance));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long accountID = resultSet.getLong(i++);
                                        String creditCardNumber = resultSet
                                                        .getString(i++);
                                        Calendar expirationDate = Calendar
                                                        .getInstance();
                                        expirationDate.setTime(resultSet
                                                        .getDate(i++));
                                        Double balance = resultSet
                                                        .getDouble(i++);

                                        results
                                                        .add(new AccountTO(
                                                                        accountID,
                                                                        login,
                                                                        creditCardNumber,
                                                                        expirationDate,
                                                                        balance));

                                        currentCount++;

                                } while (resultSet.next()
                                                && (currentCount < count));
                        }

                        return results;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void update(Connection connection, AccountTO accountTO)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "UPDATE Account SET userID = ?, creditCardNumber = ?, "
                                        + "expirationDate = ?, balance = ? WHERE accountID = ?";
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
                        preparedStatement
                                        .setLong(i++, accountTO.getAccountID());

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(accountTO
                                                .getAccountID(),
                                                AccountTO.class.getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for accountID = '"
                                                                + accountTO
                                                                                .getAccountID()
                                                                + "' in table 'Account'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void remove(Connection connection, Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM Account WHERE accountID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountID);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(accountID,
                                                AccountTO.class.getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
