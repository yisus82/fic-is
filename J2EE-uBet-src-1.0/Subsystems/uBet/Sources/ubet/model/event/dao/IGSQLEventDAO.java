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
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator;
import es.udc.fbellas.j2ee.util.sql.EntityIdentifierGeneratorFactory;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * An implementation of <code>SQLEventDAO</code> for databases with identifier
 * generators (IG). The implementation uses
 * {@link es.udc.fbellas.j2ee.util.sql.EntityIdentifierGenerator} with "Event"
 * as the name of the entity.
 */
public class IGSQLEventDAO extends AbstractSQLEventDAO {

        @Override
        public EventTO create(Connection connection, EventTO eventTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;

                try {
                        /* Get identifier. */
                        EntityIdentifierGenerator entityIdentifierGenerator = EntityIdentifierGeneratorFactory
                                        .getGenerator();
                        Long eventID = entityIdentifierGenerator
                                        .nextIdentifier(connection, "Event");

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Event (eventID, description, date,"
                                        + " categoryID, highlighted, insertionDate) VALUES (?, ?, ?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, eventID);
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
