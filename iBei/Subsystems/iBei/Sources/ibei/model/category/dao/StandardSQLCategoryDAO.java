package ibei.model.category.dao;

import ibei.model.category.to.CategoryTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class StandardSQLCategoryDAO implements SQLCategoryDAO {

        public CategoryTO find(Connection connection, String categoryID)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT name, parentID "
                                        + "FROM Category WHERE categoryID = ?";

                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, categoryID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next())
                                return null;

                        /* Read objects. */
                        i = 1;
                        String name = resultSet.getString(i++);
                        String parentID = resultSet.getString(i++);

                        /* Return value objects. */
                        return new CategoryTO(categoryID, name,
                                        new Vector<String>(), parentID);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                        GeneralOperations.closeConnection(connection);
                }

        }

        public HashMap<String, CategoryTO> findAll(Connection connection)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                HashMap<String, CategoryTO> results = new HashMap<String, CategoryTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT categoryID, parentID "
                                        + "FROM Category WHERE parentID is NOT NULL ORDER BY name";

                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(
                                                "Categories", CategoryTO.class
                                                                .getName());
                        }

                        /* Read objects. */
                        do {
                                int i = 1;
                                String categoryID = resultSet.getString(i++);
                                String parentID = resultSet.getString(i++);
                                CategoryTO categoryTO = results.get(parentID);
                                Vector<String> subcategories = new Vector<String>();
                                if (categoryTO == null)
                                        categoryTO = find(connection, parentID);
                                else
                                        subcategories = categoryTO
                                                        .getSubcategories();
                                subcategories.add(categoryID);
                                categoryTO.setSubcategories(subcategories);
                                results.put(parentID, categoryTO);
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

        public Vector<CategoryTO> findLeafs(Connection connection)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Vector<CategoryTO> results = new Vector<CategoryTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT categoryID, name, parentID "
                                        + "FROM Category WHERE leaf = 'Y' ORDER BY name";

                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(
                                                "Categories", CategoryTO.class
                                                                .getName());
                        }

                        /* Read objects. */
                        do {
                                int i = 1;
                                String categoryID = resultSet.getString(i++);
                                String name = resultSet.getString(i++);
                                results.add(new CategoryTO(categoryID, name,
                                                null, null));
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
