package ubet.model.country.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ubet.model.country.to.CountryTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class StandardSQLCountryDAO implements SQLCountryDAO {

        public boolean exists(Connection connection, String countryID)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        /* Create "preparedStatement". */
                        String queryString = "SELECT countryID FROM Country"
                                        + " WHERE countryID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, countryID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        return resultSet.next();

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                        if (connection != null) {
                                try {
                                        GeneralOperations
                                                        .closeConnection(connection);
                                } catch (InternalErrorException e) {
                                        e.printStackTrace();
                                }
                        }
                }
        }

        public CountryTO find(Connection connection, String countryID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        /* Create "preparedStatement". */
                        String queryString = "SELECT name, language, languageID"
                                        + " FROM Country WHERE countryID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, countryID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(countryID,
                                                CountryTO.class.getName());
                        }

                        /* Get first result. */
                        i = 1;
                        String name = resultSet.getString(i++);
                        String language = resultSet.getString(i++);
                        String languageID = resultSet.getString(i++);

                        return new CountryTO(countryID, name, language,
                                        languageID);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                        if (connection != null) {
                                try {
                                        GeneralOperations
                                                        .closeConnection(connection);
                                } catch (InternalErrorException e) {
                                        e.printStackTrace();
                                }
                        }
                }
        }

        public List<CountryTO> getAll(Connection connection)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        /* Create "preparedStatement". */
                        String queryString = "SELECT countryID, name, language, languageID"
                                        + " FROM Country ORDER BY name";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_FORWARD_ONLY,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects. */
                        List<CountryTO> countryTOs = new ArrayList<CountryTO>();

                        while (resultSet.next()) {
                                int i = 1;
                                String countryID = resultSet.getString(i++);
                                String name = resultSet.getString(i++);
                                String language = resultSet.getString(i++);
                                String languageID = resultSet.getString(i++);
                                CountryTO country = new CountryTO(countryID,
                                                name, language, languageID);
                                countryTOs.add(country);
                        }

                        return countryTOs;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                        if (connection != null) {
                                try {
                                        GeneralOperations
                                                        .closeConnection(connection);
                                } catch (InternalErrorException e) {
                                        e.printStackTrace();
                                }
                        }
                }
        }
}
