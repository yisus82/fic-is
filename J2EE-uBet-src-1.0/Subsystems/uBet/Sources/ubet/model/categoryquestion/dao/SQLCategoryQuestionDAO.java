package ubet.model.categoryquestion.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import ubet.model.question.to.QuestionTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLCategoryQuestionDAO {

        /**
         * Finds all the category questions in the database.
         * 
         * @param connection
         *                the connection to the database
         * @return a collection of transfer objects with the category questions
         *         data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public Map<String, List<QuestionTO>> getAll(Connection connection)
                        throws InternalErrorException,
                        InstanceNotFoundException;

}
