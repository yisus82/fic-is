package ubet.model.accountoperation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ubet.model.account.to.AccountTO;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A partial implementation of <code>SQLAccountOperationDAO</code> that leaves
 * <code>create(Connection, AccountOperationTO)</code> as abstract.
 */
public abstract class AbstractSQLAccountOperationDAO implements
                SQLAccountOperationDAO {

        /**
         * Sole constructor. (For invocation by subclass constructors, typically
         * implicit).
         */
        protected AbstractSQLAccountOperationDAO() {
        }

        public abstract AccountOperationTO create(Connection connection,
                        AccountOperationTO accountOperationTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long accountOperationID)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT accountOperationID FROM AccountOperation WHERE accountOperationID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountOperationID);

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

        public AccountOperationTO find(Connection connection,
                        Long accountOperationID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT accountID, amount, type, description, date, betID FROM AccountOperation WHERE accountOperationID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountOperationID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(
                                                accountOperationID,
                                                AccountOperationTO.class
                                                                .getName());
                        }

                        /* Get first result. */
                        i = 1;
                        Long accountID = resultSet.getLong(i++);
                        Double amount = resultSet.getDouble(i++);
                        String type = resultSet.getString(i++);
                        String description = resultSet.getString(i++);
                        Calendar date = Calendar.getInstance();
                        date.setTime(resultSet.getTimestamp(i++));
                        Long betID = resultSet.getLong(i++);

                        return new AccountOperationTO(accountOperationID,
                                        accountID, amount, type, description,
                                        date, betID);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public List<AccountOperationTO> findByAccount(Connection connection,
                        Long accountID, int startIndex, int count,
                        Calendar startDate, Calendar endDate)
                        throws InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                List<AccountOperationTO> results = new ArrayList<AccountOperationTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT accountOperationID, amount, type, description, date, betID FROM AccountOperation WHERE accountID = ?";
                        if (startDate != null)
                                queryString += " AND date >= ?";
                        if (endDate != null)
                                queryString += " AND date <= ?";
                        queryString += " ORDER BY date DESC";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountID);
                        if (startDate != null) {
                                Calendar dateWithoutMilliSeconds = DateOperations
                                                .getDateWithoutMilliSeconds(startDate);
                                preparedStatement
                                                .setTimestamp(
                                                                i++,
                                                                new Timestamp(
                                                                                dateWithoutMilliSeconds
                                                                                                .getTime()
                                                                                                .getTime()));
                        }
                        if (endDate != null) {
                                Calendar dateWithoutMilliSeconds = DateOperations
                                                .getDateWithoutMilliSeconds(endDate);
                                preparedStatement
                                                .setTimestamp(
                                                                i++,
                                                                new Timestamp(
                                                                                dateWithoutMilliSeconds
                                                                                                .getTime()
                                                                                                .getTime()));
                        }

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */

                        /* Read all. */

                        if ((startIndex < 1) && (count < 1)
                                        && resultSet.first()) {

                                do {

                                        i = 1;
                                        Long accountOperationID = resultSet
                                                        .getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        String type = resultSet.getString(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Long betID = resultSet.getLong(i++);

                                        if (betID.equals(new Long("0")))
                                                results
                                                                .add(new AccountOperationTO(
                                                                                accountOperationID,
                                                                                accountID,
                                                                                amount,
                                                                                type,
                                                                                description,
                                                                                date,
                                                                                null));
                                        else
                                                results
                                                                .add(new AccountOperationTO(
                                                                                accountOperationID,
                                                                                accountID,
                                                                                amount,
                                                                                type,
                                                                                description,
                                                                                date,
                                                                                betID));

                                } while (resultSet.next());
                                return results;
                        }

                        /* Read page by page. */

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {

                                do {

                                        i = 1;
                                        Long accountOperationID = resultSet
                                                        .getLong(i++);
                                        Double amount = resultSet
                                                        .getDouble(i++);
                                        String type = resultSet.getString(i++);
                                        String description = resultSet
                                                        .getString(i++);
                                        Calendar date = Calendar.getInstance();
                                        date.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Long betID = resultSet.getLong(i++);

                                        if (betID.equals(new Long("0")))
                                                results
                                                                .add(new AccountOperationTO(
                                                                                accountOperationID,
                                                                                accountID,
                                                                                amount,
                                                                                type,
                                                                                description,
                                                                                date,
                                                                                null));
                                        else
                                                results
                                                                .add(new AccountOperationTO(
                                                                                accountOperationID,
                                                                                accountID,
                                                                                amount,
                                                                                type,
                                                                                description,
                                                                                date,
                                                                                betID));

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

        public void update(Connection connection,
                        AccountOperationTO accountOperationTO)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "UPDATE AccountOperation SET accountID = ?, amount = ?, type = ?, description = ? "
                                        + "date = ?, betID = ? WHERE accountOperationID = ?";
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
                        preparedStatement.setLong(i++, accountOperationTO
                                        .getAccountOperationID());
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(accountOperationTO
                                                        .getDate());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        preparedStatement.setLong(i++, accountOperationTO
                                        .getBetID());

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(
                                                accountOperationTO
                                                                .getAccountID(),
                                                AccountTO.class.getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for accountOperationID = '"
                                                                + accountOperationTO
                                                                                .getAccountID()
                                                                + "' in table 'AccountOperation'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void remove(Connection connection, Long accountOperationID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM AccountOperation WHERE accountOperationID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, accountOperationID);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(
                                                accountOperationID,
                                                AccountOperationTO.class
                                                                .getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
