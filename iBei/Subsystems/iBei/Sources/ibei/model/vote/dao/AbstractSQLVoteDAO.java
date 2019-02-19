package ibei.model.vote.dao;

import ibei.model.vote.to.VoteTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public abstract class AbstractSQLVoteDAO implements SQLVoteDAO {

        public abstract VoteTO create(Connection connection, VoteTO voteVO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long voteID)
                        throws InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT voteID FROM Vote WHERE voteID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, voteID);

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

        public VoteTO find(Connection connection, Long voteID)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT rating, comment, voterID, type, date, "
                                        + "votedID, productID FROM Vote WHERE voteID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, voteID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(voteID,
                                                VoteTO.class.getName());
                        }

                        /* Get results. */
                        i = 1;
                        int rating = resultSet.getInt(i++);
                        String comment = resultSet.getString(i++);
                        String voterID = resultSet.getString(i++);
                        String type = resultSet.getString(i++);
                        Calendar date = Calendar.getInstance();
                        date.setTime(resultSet.getTimestamp(i++));
                        String votedID = resultSet.getString(i++);
                        Long productID = resultSet.getLong(i++);

                        /* Return the value object. */
                        return new VoteTO(voteID, rating, comment, voterID,
                                        type, date, votedID, productID);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

        public void remove(Connection connection, Long voteID)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                PreparedStatement preparedStatement = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "DELETE FROM Vote WHERE voteID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, voteID);

                        /* Execute query. */
                        int removedRows = preparedStatement.executeUpdate();

                        if (removedRows == 0) {
                                throw new InstanceNotFoundException(voteID,
                                                VoteTO.class.getName());
                        }

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeStatement(preparedStatement);
                }

        }

}
