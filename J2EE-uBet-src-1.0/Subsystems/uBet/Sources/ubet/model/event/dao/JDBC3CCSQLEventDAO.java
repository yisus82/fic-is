package ubet.model.event.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import ubet.model.event.to.EventTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLEventDAO</code> for databases providing
 * counter columns (CC) with JDBC 3 drivers.
 */
public class JDBC3CCSQLEventDAO extends AbstractSQLEventDAO {

        @Override
        public EventTO create(Connection connection, EventTO eventTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Event (description, date,"
                                        + " categoryID, highlighted, insertionDate) VALUES (?, ?, ?, ?, ?)";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        Statement.RETURN_GENERATED_KEYS);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setString(i++, eventTO
                                        .getDescription());
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(eventTO
                                                        .getDate());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        preparedStatement.setString(i++, eventTO
                                        .getCategoryID());
                        preparedStatement.setString(i++, eventTO
                                        .getHighlighted());
                        dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(eventTO
                                                        .getInsertionDate());
                        preparedStatement.setDate(i++, new Date(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'Event'");
                        }

                        /* Get identifier. */
                        resultSet = preparedStatement.getGeneratedKeys();

                        if (!resultSet.next()) {
                                throw new InternalErrorException(
                                                new SQLException(
                                                                "JDBC driver did not return generated key."));
                        }
                        Long eventID = resultSet.getLong(1);
                        eventTO.setEventID(eventID);

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for event ID = '"
                                                                + eventTO
                                                                                .getEventID()
                                                                + "' in table 'Event'");
                        }

                        return eventTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

}
