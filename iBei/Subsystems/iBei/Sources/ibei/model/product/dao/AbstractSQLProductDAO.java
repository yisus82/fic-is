package ibei.model.product.dao;

import ibei.model.product.to.ProductDetailsTO;
import ibei.model.product.to.ProductTO;
import ibei.model.productfacade.to.ProductResultTO;
import ibei.model.util.DateOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public abstract class AbstractSQLProductDAO implements SQLProductDAO {

        public abstract ProductTO create(Connection connection,
                        ProductTO productTO) throws InstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long productID)
                        throws InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT productID FROM Product"
                                        + " WHERE productID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, productID);

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

        public ProductTO find(Connection connection, Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT name, description, "
                                        + "endTime, currentPrice, startTime, startingPrice,"
                                        + "shippingInfo, categoryID, userID, winnerID, winnerBid "
                                        + "FROM Product WHERE productID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, productID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(productID,
                                                ProductTO.class.getName());
                        }

                        /* Get results. */
                        i = 1;
                        String name = resultSet.getString(i++);
                        String description = resultSet.getString(i++);
                        Calendar endTime = Calendar.getInstance();
                        endTime.setTime(resultSet.getTimestamp(i++));
                        Double currentPrice = resultSet.getDouble(i++);
                        Calendar startTime = Calendar.getInstance();
                        startTime.setTime(resultSet.getTimestamp(i++));
                        Double startingPrice = resultSet.getDouble(i++);
                        String shippingInfo = resultSet.getString(i++);
                        String categoryID = resultSet.getString(i++);
                        String userID = resultSet.getString(i++);
                        String winnerID = resultSet.getString(i++);
                        Double winnerBid = resultSet.getDouble(i++);
                        if (winnerBid == 0)
                                winnerBid = null;
                        ProductDetailsTO details = new ProductDetailsTO(
                                        startTime, startingPrice, shippingInfo,
                                        categoryID, userID, winnerID, winnerBid);

                        /* Return the value object. */
                        return new ProductTO(productID, name, description,
                                        endTime, currentPrice, details);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

        public Vector<ProductResultTO> findByCategory(Connection connection,
                        String categoryID, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Vector<ProductResultTO> results = new Vector<ProductResultTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT productID, name, endTime, "
                                        + "currentPrice FROM Product WHERE categoryID = ? ORDER BY endTime";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, categoryID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */
                        if ((startIndex == -1) && (count == -1)
                                        && resultSet.first()) {
                                do {
                                        i = 1;
                                        Long productID = resultSet.getLong(i++);
                                        String name = resultSet.getString(i++);
                                        Calendar endTime = Calendar
                                                        .getInstance();
                                        endTime.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Double currentPrice = resultSet
                                                        .getDouble(i++);
                                        results.add(new ProductResultTO(
                                                        productID, name,
                                                        endTime, currentPrice));
                                } while (resultSet.next());
                                return results;
                        }

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {
                                do {
                                        i = 1;
                                        Long productID = resultSet.getLong(i++);
                                        String name = resultSet.getString(i++);
                                        Calendar endTime = Calendar
                                                        .getInstance();
                                        endTime.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Double currentPrice = resultSet
                                                        .getDouble(i++);
                                        results.add(new ProductResultTO(
                                                        productID, name,
                                                        endTime, currentPrice));
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

        public Vector<ProductResultTO> findByKeywords(Connection connection,
                        Vector<String> keywords, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Vector<ProductResultTO> results = new Vector<ProductResultTO>();

                for (int j = 0; j < keywords.size(); j++)
                        keywords.setElementAt((keywords.elementAt(j))
                                        .toUpperCase(), j);

                try {

                        if (keywords.isEmpty())
                                throw new InternalErrorException(new Exception(
                                                "No keywords"));

                        /* Create "preparedStatement". */
                        String queryString = "SELECT productID, name, endTime, "
                                        + "currentPrice FROM Product WHERE UPPER(name) LIKE '%"
                                        + keywords.elementAt(0) + "%'";
                        for (int j = 1; j < keywords.size(); j++) {
                                queryString += " AND UPPER(name) LIKE '%"
                                                + keywords.elementAt(j) + "%'";
                        }
                        queryString += " ORDER BY endTime";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects */
                        int i;
                        if ((startIndex == -1) && (count == -1)
                                        && resultSet.first()) {
                                do {
                                        i = 1;
                                        Long productID = resultSet.getLong(i++);
                                        String name = resultSet.getString(i++);
                                        Calendar endTime = Calendar
                                                        .getInstance();
                                        endTime.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Double currentPrice = resultSet
                                                        .getDouble(i++);
                                        results.add(new ProductResultTO(
                                                        productID, name,
                                                        endTime, currentPrice));

                                } while (resultSet.next());
                        }

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {
                                do {
                                        i = 1;
                                        Long productID = resultSet.getLong(i++);
                                        String name = resultSet.getString(i++);
                                        Calendar endTime = Calendar
                                                        .getInstance();
                                        endTime.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Double currentPrice = resultSet
                                                        .getDouble(i++);
                                        results.add(new ProductResultTO(
                                                        productID, name,
                                                        endTime, currentPrice));
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

        public Vector<ProductResultTO> findByUser(Connection connection,
                        String login, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Vector<ProductResultTO> results = new Vector<ProductResultTO>();

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT productID, name, endTime, "
                                        + "currentPrice FROM Product WHERE userID = ? ORDER BY endTime";
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
                        if ((startIndex == -1) && (count == -1)
                                        && resultSet.first()) {
                                do {
                                        i = 1;
                                        Long productID = resultSet.getLong(i++);
                                        String name = resultSet.getString(i++);
                                        Calendar endTime = Calendar
                                                        .getInstance();
                                        endTime.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Double currentPrice = resultSet
                                                        .getDouble(i++);
                                        results.add(new ProductResultTO(
                                                        productID, name,
                                                        endTime, currentPrice));
                                } while (resultSet.next());
                                return results;
                        }

                        int currentCount = 0;

                        if ((startIndex >= 1) && resultSet.absolute(startIndex)) {
                                do {
                                        i = 1;
                                        Long productID = resultSet.getLong(i++);
                                        String name = resultSet.getString(i++);
                                        Calendar endTime = Calendar
                                                        .getInstance();
                                        endTime.setTime(resultSet
                                                        .getTimestamp(i++));
                                        Double currentPrice = resultSet
                                                        .getDouble(i++);
                                        results.add(new ProductResultTO(
                                                        productID, name,
                                                        endTime, currentPrice));
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

        public void update(Connection connection, ProductTO productTO)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "UPDATE Product SET name = ?, description = ?, "
                                        + "endTime = ?, currentPrice = ?, shippingInfo = ?, "
                                        + "categoryID = ?, winnerID = ?, winnerBid = ? WHERE productID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, productTO.getName());
                        preparedStatement.setString(i++, productTO
                                        .getDescription());
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(productTO
                                                        .getEndTime());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        preparedStatement.setDouble(i++, productTO
                                        .getCurrentPrice());
                        ProductDetailsTO details = productTO
                                        .getProductDetails();
                        preparedStatement.setString(i++, details
                                        .getShippingInfo());
                        preparedStatement.setString(i++, details
                                        .getCategoryID());
                        preparedStatement.setString(i++, details.getWinnerID());
                        preparedStatement
                                        .setDouble(i++, details.getWinnerBid());
                        preparedStatement
                                        .setLong(i++, productTO.getProductID());

                        /* Execute query. */
                        int updatedRows = preparedStatement.executeUpdate();

                        if (updatedRows == 0) {
                                throw new InstanceNotFoundException(productTO
                                                .getProductID(),
                                                ProductTO.class.getName());
                        }

                        if (updatedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for product ID = '"
                                                                + productTO
                                                                                .getProductID()
                                                                + "' in table 'Product'");
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

        public void remove(Connection connection, Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM Product WHERE productID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, productID);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(productID,
                                                ProductTO.class.getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

}
