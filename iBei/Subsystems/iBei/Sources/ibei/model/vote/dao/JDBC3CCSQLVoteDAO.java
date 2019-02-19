package ibei.model.vote.dao;

import ibei.model.util.DateOperations;
import ibei.model.vote.to.VoteTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class JDBC3CCSQLVoteDAO extends AbstractSQLVoteDAO {

        public VoteTO create(Connection connection, VoteTO voteTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "INSERT INTO Vote (rating, comment, voterID, "
                                        + "type, date, votedID, productID) "
                                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setInt(i++, voteTO.getRating());
                        preparedStatement.setString(i++, voteTO.getComment());
                        preparedStatement.setString(i++, voteTO.getVoterID());
                        preparedStatement.setString(i++, voteTO.getType());
                        Calendar dateWithoutMilliSeconds = DateOperations
                                        .getDateWithoutMilliSeconds(voteTO
                                                        .getDate());
                        preparedStatement.setTimestamp(i++, new Timestamp(
                                        dateWithoutMilliSeconds.getTime()
                                                        .getTime()));
                        preparedStatement.setString(i++, voteTO.getVotedID());
                        preparedStatement.setLong(i++, voteTO.getProductID());

                        /* Execute query. */
                        int insertedRows = preparedStatement.executeUpdate();

                        if (insertedRows == 0) {
                                throw new SQLException(
                                                "Cannot add row to table 'Vote'");
                        }

                        if (insertedRows > 1) {
                                throw new SQLException(
                                                "Duplicate row for vote ID = '"
                                                                + voteTO
                                                                                .getVoteID()
                                                                + "' in table 'Vote'");
                        }

                        /* Get identifier. */
                        resultSet = preparedStatement.getGeneratedKeys();

                        if (!resultSet.next()) {
                                throw new InternalErrorException(
                                                new SQLException(
                                                                "JDBC driver did not return generated key."));
                        }
                        Long voteID = resultSet.getLong(1);
                        voteTO.setVoteID(voteID);
                        return voteTO;

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

}
