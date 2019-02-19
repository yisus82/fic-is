package ubet.model.event.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import ubet.model.event.to.EventTO;
import ubet.model.util.DateOperations;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetriever;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetrieverFactory;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLEventDAO</code> for databases with counter
 * columns (CC) without JDBC 3 drivers. The implementation uses
 * {@link es.udc.fbellas.j2ee.util.sql.EntityIdentifierRetriever}.
 */
public class CCSQLEventDAO extends AbstractSQLEventDAO {

        @Override
        public EventTO create(Connection connection, EventTO eventTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Event (description, date,"
                                        + " categoryID, highlighted, insertionDate) VALUES (?, ?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

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
                        EntityIdentifierRetriever entityIdentifierRetriever = EntityIdentifierRetrieverFactory
                                        .getRetriever();
                        Long eventID = entityIdentifierRetriever
                                        .getGeneratedIdentifier(connection);

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
