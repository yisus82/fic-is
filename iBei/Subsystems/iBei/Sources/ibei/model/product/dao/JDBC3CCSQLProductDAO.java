package ibei.model.product.dao;

import ibei.model.product.to.ProductDetailsTO;
import ibei.model.product.to.ProductTO;
import ibei.model.util.DateOperations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import es.udc.fbellas.j2ee.util.exceptions.InstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class JDBC3CCSQLProductDAO extends AbstractSQLProductDAO {

        public ProductTO create(Connection connection, ProductTO productTO)
                        throws InstanceException, InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Product (name, description, "
                                        + "endTime, currentPrice, startTime, startingPrice,"
                                        + "shippingInfo, categoryID, userID, winnerID) "
                                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        Statement.RETURN_GENERATED_KEYS);

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
                        dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(details
                                                        .getStartTime());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        preparedStatement.setDouble(i++, details
                                        .getStartingPrice());
                        preparedStatement.setString(i++, details
                                        .getShippingInfo());
                        preparedStatement.setString(i++, details
                                        .getCategoryID());
                        preparedStatement.setString(i++, details.getUserID());
                        preparedStatement.setString(i++, details.getWinnerID());
                       
                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'Product'");
                        }

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for Product ID = '"
                                                                + productTO
                                                                                .getProductID()
                                                                + "' in table 'Product'");
                        }

                        /* Get identifier. */
                        resultSet = preparedStatement.getGeneratedKeys();

                        if (!resultSet.next()) {
                                throw new InternalErrorException(
                                                new SQLException(
                                                                "JDBC driver did not return generated key."));
                        }
                        Long productID = resultSet.getLong(1);
                        productTO.setProductID(productID);
                        return productTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

}
