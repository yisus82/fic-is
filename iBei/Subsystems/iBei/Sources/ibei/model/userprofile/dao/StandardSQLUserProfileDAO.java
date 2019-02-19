package ibei.model.userprofile.dao;

import ibei.model.sellerprofile.to.SellerProfileTO;
import ibei.model.userprofile.to.UserProfileDetailsTO;
import ibei.model.userprofile.to.UserProfileTO;
import ibei.model.util.DateOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class StandardSQLUserProfileDAO implements SQLUserProfileDAO {

        public UserProfileTO create(Connection connection,
                        UserProfileTO userProfileTO) throws InstanceException,
                        InternalErrorException {

                /* Check if the user already exists. */
                if (exists(connection, userProfileTO.getLogin(), userProfileTO
                                .getType()))
                        throw new DuplicateInstanceException(userProfileTO
                                        .getLogin(), UserProfileTO.class
                                        .getName());

                if ((userProfileTO.getType() == UserProfileTO.BUYER)
                                && (exists(connection,
                                                userProfileTO.getLogin(),
                                                UserProfileTO.SELLER)))
                        throw new DuplicateInstanceException(userProfileTO
                                        .getLogin(), UserProfileTO.class
                                        .getName());

                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "";
                        if (userProfileTO.getType() == UserProfileTO.SELLER) {
                                if (exists(connection,
                                                userProfileTO.getLogin(),
                                                UserProfileTO.BUYER))
                                        remove(connection, userProfileTO
                                                        .getLogin());
                                queryString = "INSERT INTO UserProfile (login, password,"
                                                + " type, firstName, surname, email, street,"
                                                + " city, state, postalCode, countryID, creditCardNumber,"
                                                + " expirationDate) "
                                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        } else
                                queryString = "INSERT INTO UserProfile (login, password,"
                                                + " type, firstName, surname, email, street,"
                                                + " city, state, postalCode, countryID) "
                                                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, userProfileTO
                                        .getLogin());
                        preparedStatement.setString(i++, userProfileTO
                                        .getPassword());
                        preparedStatement.setString(i++, userProfileTO
                                        .getType());
                        UserProfileDetailsTO details = userProfileTO
                                        .getUserProfileDetails();
                        preparedStatement
                                        .setString(i++, details.getFirstName());
                        preparedStatement.setString(i++, details.getSurname());
                        preparedStatement.setString(i++, details.getEmail());
                        preparedStatement.setString(i++, details.getStreet());
                        preparedStatement.setString(i++, details.getCity());
                        preparedStatement.setString(i++, details.getState());
                        preparedStatement
                                        .setShort(i++, details.getPostalCode());
                        preparedStatement
                                        .setString(i++, details.getCountryID());
                        if (userProfileTO.getType() == UserProfileTO.SELLER) {
                                SellerProfileTO profile = (SellerProfileTO) userProfileTO;
                                preparedStatement.setString(i++, profile
                                                .getCreditCardNumber());
                                Calendar dateWithoutMilliSeconds = DateOperations
                                                .getDateWithoutMilliSeconds(profile
                                                                .getExpirationDate());
                                preparedStatement
                                                .setTimestamp(
                                                                i++,
                                                                new Timestamp(
                                                                                dateWithoutMilliSeconds
                                                                                                .getTime()
                                                                                                .getTime()));
                        }

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'UserProfile'");
                        }

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for login = '"
                                                                + userProfileTO
                                                                                .getLogin()
                                                                + "' in table 'UserProfile'");
                        }

                        /* Return the value object. */
                        return userProfileTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

        public boolean exists(Connection connection, String login, String type)
                        throws InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT login FROM UserProfile"
                                        + " WHERE login = ? AND type = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, login);
                        preparedStatement.setString(i++, type);

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
                        String queryString = "SELECT password, type, firstName,"
                                        + " surname, email, street, city, state, postalCode, "
                                        + "countryID, creditCardNumber, expirationDate "
                                        + "FROM UserProfile WHERE login = ?";
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
                        String type = resultSet.getString(i++);
                        String name = resultSet.getString(i++);
                        String surname = resultSet.getString(i++);
                        String email = resultSet.getString(i++);
                        String street = resultSet.getString(i++);
                        String city = resultSet.getString(i++);
                        String state = resultSet.getString(i++);
                        Short postalCode = resultSet.getShort(i++);
                        String countryID = resultSet.getString(i++);
                        String creditCardNumber = resultSet.getString(i++);
                        Calendar expirationDate = Calendar.getInstance();
                        expirationDate.setTime(resultSet.getTimestamp(i++));
                        UserProfileDetailsTO details = new UserProfileDetailsTO(
                                        name, surname, email, street, city,
                                        state, postalCode, countryID);

                        /* Return the value object. */
                        if (type.equals(UserProfileTO.SELLER))
                                return new SellerProfileTO(login, password,
                                                details, creditCardNumber,
                                                expirationDate);
                        return new UserProfileTO(login, password, details);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

        public UserProfileTO update(Connection connection, String login,
                        UserProfileTO userProfileTO) throws InstanceException,
                        InternalErrorException, DuplicateInstanceException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        /* Check if it is an incorrect login update. */
                        String queryString = "SELECT login FROM UserProfile "
                                        + "WHERE login = ? ";

                        preparedStatement = connection
                                        .prepareStatement(queryString);
                        int i = 1;
                        preparedStatement.setString(i++, userProfileTO
                                        .getLogin());

                        resultSet = preparedStatement.executeQuery();
                        if ((!login.equals(userProfileTO.getLogin()))
                                        && (resultSet.next()))
                                throw new DuplicateInstanceException(
                                                userProfileTO.getLogin(),
                                                UserProfileTO.class.getName());

                        /* Create "preparedStatement". */
                        queryString = "";
                        if (userProfileTO.getType() == UserProfileTO.SELLER)
                                queryString = "UPDATE UserProfile SET login = ?, password = ?,"
                                                + " firstName = ?, surname = ?, "
                                                + "email = ?, street = ?, city = ?, state = ?, "
                                                + "postalCode = ?, countryID = ?, creditCardNumber = ?, "
                                                + "expirationDate = ? WHERE login = ?";
                        else
                                queryString = "UPDATE UserProfile SET login = ?, password = ?,"
                                                + " firstName = ?, surname = ?, email = ?,"
                                                + " street = ?, city = ?, state = ?, postalCode = ?, "
                                                + "countryID = ? WHERE login = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        i = 1;
                        preparedStatement.setString(i++, userProfileTO
                                        .getLogin());
                        preparedStatement.setString(i++, userProfileTO
                                        .getPassword());
                        UserProfileDetailsTO details = userProfileTO
                                        .getUserProfileDetails();
                        preparedStatement
                                        .setString(i++, details.getFirstName());
                        preparedStatement.setString(i++, details.getSurname());
                        preparedStatement.setString(i++, details.getEmail());
                        preparedStatement.setString(i++, details.getStreet());
                        preparedStatement.setString(i++, details.getCity());
                        preparedStatement.setString(i++, details.getState());
                        preparedStatement
                                        .setShort(i++, details.getPostalCode());
                        preparedStatement
                                        .setString(i++, details.getCountryID());
                        if (userProfileTO.getType() == UserProfileTO.SELLER) {
                                SellerProfileTO profile = (SellerProfileTO) userProfileTO;
                                preparedStatement.setString(i++, profile
                                                .getCreditCardNumber());
                                Calendar dateWithoutMilliSeconds = DateOperations
                                                .getDateWithoutMilliSeconds(profile
                                                                .getExpirationDate());
                                preparedStatement
                                                .setTimestamp(
                                                                i++,
                                                                new Timestamp(
                                                                                dateWithoutMilliSeconds
                                                                                                .getTime()
                                                                                                .getTime()));
                        }
                        preparedStatement.setString(i++, login);

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(
                                                userProfileTO.getLogin(),
                                                UserProfileTO.class.getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for login = '"
                                                                + login
                                                                + "' in table 'UserProfile'");
                        }

                        /* Return the value object. */
                        return userProfileTO;

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
