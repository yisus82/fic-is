package ubet.model.categoryquestion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ubet.model.categoryquestion.to.CategoryQuestionTO;
import ubet.model.question.to.QuestionTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

public class StandardSQLCategoryQuestionDAO implements SQLCategoryQuestionDAO {

        public Map<String, List<QuestionTO>> getAll(Connection connection)
                        throws InternalErrorException,
                        InstanceNotFoundException {
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try {
                        /* Create "preparedStatement". */
                        String queryString = "SELECT DISTINCT categoryID"
                                        + " FROM CategoryQuestion";
                        preparedStatement = connection.prepareStatement(
                                        queryString,
                                        ResultSet.TYPE_FORWARD_ONLY,
                                        ResultSet.CONCUR_READ_ONLY);

                        /* Execute query. */
                        resultSet = preparedStatement.executeQuery();

                        /* Read objects. */
                        List<String> categoryIDs = new ArrayList<String>();
                        while (resultSet.next()) {
                                int i = 1;
                                String categoryID = resultSet.getString(i++);
                                categoryIDs.add(categoryID);
                        }

                        List<CategoryQuestionTO> categoryQuestionTOs = new ArrayList<CategoryQuestionTO>();

                        for (String ID : categoryIDs) {

                                queryString = "SELECT questionID"
                                                + " FROM CategoryQuestion WHERE categoryID = ?";
                                preparedStatement = connection
                                                .prepareStatement(
                                                                queryString,
                                                                ResultSet.TYPE_FORWARD_ONLY,
                                                                ResultSet.CONCUR_READ_ONLY);

                                /* Fill "preparedStatement". */
                                int i = 1;
                                preparedStatement.setString(i++, ID);

                                /* Execute query. */
                                resultSet = preparedStatement.executeQuery();

                                /* Read objects. */
                                CategoryQuestionTO categoryQuestion = new CategoryQuestionTO(
                                                ID);

                                i = 1;
                                while (resultSet.next()) {
                                        int j = 1;
                                        Long questionID = resultSet.getLong(i);
                                        String queryString2 = "SELECT description"
                                                        + " FROM Question WHERE questionID = ?";
                                        PreparedStatement preparedStatement2 = connection
                                                        .prepareStatement(queryString2);

                                        /* Fill "preparedStatement". */
                                        j = 1;
                                        preparedStatement2.setLong(j++,
                                                        questionID);

                                        /* Execute query. */
                                        ResultSet resultSet2 = preparedStatement2
                                                        .executeQuery();

                                        if (!resultSet2.next()) {
                                                throw new InstanceNotFoundException(
                                                                questionID,
                                                                QuestionTO.class
                                                                                .getName());
                                        }

                                        /* Get first result. */
                                        j = 1;
                                        String description = resultSet2
                                                        .getString(j++);
                                        categoryQuestion
                                                        .addQuestion(new QuestionTO(
                                                                        questionID,
                                                                        description));
                                }
                                categoryQuestionTOs.add(categoryQuestion);
                        }

                        Map<String, List<QuestionTO>> map = new HashMap<String, List<QuestionTO>>();

                        for (CategoryQuestionTO c : categoryQuestionTOs) {
                                map.put(c.getCategoryID(), c.getQuestions());
                        }

                        return map;

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
