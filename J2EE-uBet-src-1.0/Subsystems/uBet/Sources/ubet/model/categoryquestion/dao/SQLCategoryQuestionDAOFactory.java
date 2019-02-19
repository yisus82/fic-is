package ubet.model.categoryquestion.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLCategoryQuestionsDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLCategoryQuestionsDAOFactory/daoClassName</code>: it must
 * specify the full class name of the class implementing
 * <code>SQLCategoryQuestionsDAO</code>.</li>
 * </ul>
 */
public final class SQLCategoryQuestionDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLCategoryQuestionDAOFactory/daoClassName";

        private final static Class<SQLCategoryQuestionDAO> daoClass = getDAOClass();

        private SQLCategoryQuestionDAOFactory() {
        }

        private static Class<SQLCategoryQuestionDAO> getDAOClass() {
                Class<SQLCategoryQuestionDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLCategoryQuestionDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SQLCategoryQuestionDAO getDAO()
                        throws InternalErrorException {
                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}