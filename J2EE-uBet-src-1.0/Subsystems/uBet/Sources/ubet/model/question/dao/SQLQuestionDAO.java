package ubet.model.question.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import ubet.model.question.to.QuestionTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLQuestionDAO {

        /**
         * Checks if a question exists in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param questionID
         *                the question identifier
         * @return <code>true</code> if the question exists<br>
         *         <code>false</code> if the question doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, Long questionID)
                        throws InternalErrorException;

        /**
         * Finds a question in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param questionID
         *                the question identifier
         * @return a transfer object with the question data
         * @throws InstanceNotFoundException
         *                 if there is no question with the
         *                 <code>questionID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public QuestionTO find(Connection connection, Long questionID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Finds questions in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param questionIDs
         *                question identifiers
         * @return a collection of transfer objects with the questions data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public Map<Long, QuestionTO> findByIDs(Connection connection,
                        List<Long> questionIDs) throws InternalErrorException;

        /**
         * Finds all the questions in the database.
         * 
         * @param connection
         *                the connection to the database
         * @return a collection of transfer objects with the questions data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<QuestionTO> getAll(Connection connection)
                        throws InternalErrorException;

}
