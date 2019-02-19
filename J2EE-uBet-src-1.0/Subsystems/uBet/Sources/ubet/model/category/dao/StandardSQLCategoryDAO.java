package ubet.model.category.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ubet.model.category.to.CategoryTO;
import ubet.model.country.to.CountryTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class StandardSQLCategoryDAO implements SQLCategoryDAO {

        public boolean exists(Connection connection, String categoryID)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        /* Create "preparedStatement". */
                        String queryString = "SELECT categoryID FROM Category"
                                        + " WHERE categoryID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, categoryID);

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

        public CategoryTO find(Connection connection, String categoryID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        /* Create "preparedStatement". */
                        String queryString = "SELECT name, parent, leaf, questionID"
                                        + " FROM Category WHERE categoryID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, categoryID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(categoryID,
                                                CountryTO.class.getName());
                        }

                        /* Get first result. */
                        i = 1;
                        String name = resultSet.getString(i++);
                        String parentID = resultSet.getString(i++);
                        String leaf = resultSet.getString(i++);
                        Long questionID = resultSet.getLong(i++);
                        return new CategoryTO(categoryID, name, parentID, leaf
                                        .equals("Y"), questionID);

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

        public List<CategoryTO> getAll(Connection connection)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        /* Create "preparedStatement". */
                        String queryString = "SELECT categoryID, name, parent, leaf, questionID"
                                        + " FROM Category";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_FORWARD_ONLY,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects. */
                        List<CategoryTO> categoryTOs = new ArrayList<CategoryTO>();

                        while (resultSet.next()) {
                                int i = 1;
                                String categoryID = resultSet.getString(i++);
                                String name = resultSet.getString(i++);
                                String parentID = resultSet.getString(i++);
                                String leaf = resultSet.getString(i++);
                                Long questionID = resultSet.getLong(i++);
                                CategoryTO category = new CategoryTO(
                                                categoryID, name, parentID,
                                                leaf.equals("Y"), questionID);
                                categoryTOs.add(category);
                        }

                        return categoryTOs;

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
