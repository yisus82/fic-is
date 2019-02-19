package ibei.model.country.dao;

import ibei.model.country.to.CountryTO;
import ibei.model.increment.to.IncrementTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class StandardSQLCountryDAO implements SQLCountryDAO {

        public Vector<CountryTO> findAll(Connection connection)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Vector<CountryTO> results = new Vector<CountryTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT countryID, name, language, languageID "
                                        + "FROM Increment ORDER BY name";

                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(
                                                "Countries", IncrementTO.class
                                                                .getName());
                        }

                        /* Read objects. */
                        do {
                                int i = 1;
                                String countryID = resultSet.getString(i++);
                                String name = resultSet.getString(i++);
                                String language = resultSet.getString(i++);
                                String languageID = resultSet.getString(i++);

                                results.add(new CountryTO(countryID, name,
                                                language, languageID));

                        } while (resultSet.next());

                        /* Return value objects. */
                        return results;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                        GeneralOperations.closeConnection(connection);
                }

        }

}
