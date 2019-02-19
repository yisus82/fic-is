package ubet.model.question.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ubet.model.question.to.QuestionTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A standard implementation of <code>SQLQuestionDAO</code>.
 */
public class StandardSQLQuestionDAO implements SQLQuestionDAO {

        public boolean exists(Connection connection, Long questionID)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT questionID FROM Question WHERE questionID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, questionID);

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

        public QuestionTO find(Connection connection, Long questionID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {

                        /* Create "preparedStatement". */
                        String queryString = "SELECT description "
                                        + "FROM Question WHERE questionID = ?";
                        preparedStatement = connection
                                        .prepareStatement(queryString);

                        /* Fill "preparedStatement". */
                        int i = 1;
                        preparedStatement.setLong(i++, questionID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        if (!resultSet.next()) {
                                throw new InstanceNotFoundException(questionID,
                                                QuestionTO.class.getName());
                        }

                        /* Get first result. */
                        i = 1;
                        String description = resultSet.getString(i++);

                        return new QuestionTO(questionID, description);

                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeResultSet(resultSet);
                        GeneralOperations.closeStatement(preparedStatement);
                }
        }

        public Map<Long, QuestionTO> findByIDs(Connection connection,
                        List<Long> questionIDs) throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                Map<Long, QuestionTO> results = new HashMap<Long, QuestionTO>();
                if (questionIDs.isEmpty())
                        return results;

                try {
                        /* Create "preparedStatement". */
                        String queryString = "SELECT questionID, description "
                                        + "FROM Question WHERE questionID IN (";
                        int i;
                        for (i = 0; i < questionIDs.size(); i++)
                                queryString += "?, ";
                        queryString = queryString.substring(0, queryString
                                        .length() - 2);
                        queryString += ") ORDER BY questionID";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_FORWARD_ONLY,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Fill "preparedStatement". */
                        i = 1;
                        for (Long questionID : questionIDs)
                                preparedStatement.setLong(i++, questionID);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects. */
                        if (resultSet.first()) {

                                do {

                                        i = 1;
                                        Long questionID = resultSet
                                                        .getLong(i++);
                                        String description = resultSet
                                                        .getString(i++);

                                        results
                                                        .put(
                                                                        questionID,
                                                                        new QuestionTO(
                                                                                        questionID,
                                                                                        description));

                                } while (resultSet.next());

                        }

                        return results;

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

        public List<QuestionTO> getAll(Connection connection)
                        throws InternalErrorException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        /* Create "preparedStatement". */
                        String queryString = "SELECT questionID, description "
                                        + "FROM Question";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_FORWARD_ONLY,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects. */
                        List<QuestionTO> questionTOs = new ArrayList<QuestionTO>();

                        int i;

                        while (resultSet.next()) {
                                i = 1;
                                Long questionID = resultSet.getLong(i++);
                                String description = resultSet.getString(i++);
                                QuestionTO question = new QuestionTO(
                                                questionID, description);
                                questionTOs.add(question);
                        }

                        return questionTOs;

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
