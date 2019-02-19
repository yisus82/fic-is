package ubet.model.userprofile.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A standard implementation of <code>SQLUserProfileDAO</code>.
 */
public class StandardSQLUserProfileDAO implements SQLUserProfileDAO {

        public void create(Connection connection, UserProfileTO userTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                /* Check if there is an existing user with the same login. */
                if (exists(connection, userTO.getLogin())) {
                        throw new DuplicateInstanceException(userTO.getLogin(),
                                        UserProfileTO.class.getName());
                }

                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO UserProfile"
                                        + " (login, password, firstName, surname, email,"
                                        + " countryID) VALUES (?, ?, ?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, userTO.getLogin());
                        preparedStatement.setString(i++, userTO.getPassword());
                        preparedStatement.setString(i++, userTO
                                        .getUserDetails().getFirstName());
                        preparedStatement.setString(i++, userTO
                                        .getUserDetails().getSurname());
                        preparedStatement.setString(i++, userTO
                                        .getUserDetails().getEmail());
                        preparedStatement.setString(i++, userTO
                                        .getUserDetails().getCountryID());

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Can not add row to table"
                                                                + " 'UserProfile'");
                        }

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for login name = '"
                                                                + userTO
                                                                                .getLogin()
                                                                + "' in table 'UserProfile'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public boolean exists(Connection connection, String login)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT login FROM UserProfile"
                                        + " WHERE login = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, login);

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

        public UserProfileTO find(Connection connection, String login)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT password, firstName, surname,"
                                        + " email, countryID FROM UserProfile WHERE login = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, login);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(login,
                                                UserProfileTO.class.getName());
                        }

                        /* Get results. */
                        i = 1;
                        String password = resultSet.getString(i++);
                        String firstName = resultSet.getString(i++);
                        String surname = resultSet.getString(i++);
                        String email = resultSet.getString(i++);
                        String country = resultSet.getString(i++);

                        UserProfileDetailsTO userDetailsTO = new UserProfileDetailsTO(
                                        firstName, surname, email, country);

                        return new UserProfileTO(login, password, userDetailsTO);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void update(Connection connection, String login,
                        UserProfileTO userTO)
                        throws DuplicateInstanceException,
                        InstanceNotFoundException, InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Check if it is an incorrect login update. */
                        String queryString = "SELECT login FROM UserProfile "
                                        + "WHERE login = ? ";

                        preparedStatement = connection
                                        .prepareStatement(queryString);
                        int i = 1;
                        preparedStatement.setString(i++, userTO.getLogin());

                        resultSet = preparedStatement.executeQuery();

                        if ((!login.equals(userTO.getLogin()))
                                        && (resultSet.next()))
                                throw new DuplicateInstanceException(userTO
                                                .getLogin(),
                                                UserProfileTO.class.getName());

                        /* Create "preparedStatement". */
                        queryString = "UPDATE UserProfile SET login = ?, password = ?,"
                                        + " firstName = ?, surname = ?, email = ?,"
                                        + " countryID = ? WHERE login = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        i = 1;
                        preparedStatement.setString(i++, userTO.getLogin());
                        preparedStatement.setString(i++, userTO.getPassword());
                        UserProfileDetailsTO details = userTO.getUserDetails();
                        preparedStatement
                                        .setString(i++, details.getFirstName());
                        preparedStatement.setString(i++, details.getSurname());
                        preparedStatement.setString(i++, details.getEmail());
                        preparedStatement
                                        .setString(i++, details.getCountryID());
                        preparedStatement.setString(i++, login);

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(userTO
                                                .getLogin(),
                                                UserProfileTO.class.getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for login = '"
                                                                + login
                                                                + "' in table 'UserProfile'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public void remove(Connection connection, String login)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM UserProfile WHERE login = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, login);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(login,
                                                UserProfileTO.class.getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
