package ibei.model.increment.dao;

import ibei.model.increment.to.IncrementTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class StandardSQLIncrementDAO implements SQLIncrementDAO {

        public Vector<IncrementTO> findAll(Connection connection)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Vector<IncrementTO> results = new Vector<IncrementTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT minValue, maxValue, increment "
                                        + "FROM Increment ORDER BY minValue";

                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(
                                                "Increments", IncrementTO.class
                                                                .getName());
                        }

                        /* Read objects. */
                        do {
                                int i = 1;
                                Double minValue = resultSet.getDouble(i++);
                                Double maxValue = resultSet.getDouble(i++);
                                Double increment = resultSet.getDouble(i++);

                                results.add(new IncrementTO(minValue, maxValue,
                                                increment));

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
