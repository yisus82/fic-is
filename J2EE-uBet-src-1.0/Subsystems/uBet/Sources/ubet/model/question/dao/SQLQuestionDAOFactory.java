package ubet.model.question.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLQuestionDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLQuestionDAOFactory/daoClassName</code>: it must specify the
 * full class name of the class implementing <code>SQLQuestionDAO</code>.</li>
 * </ul>
 */
public final class SQLQuestionDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLQuestionDAOFactory/daoClassName";

        private final static Class<SQLQuestionDAO> daoClass = getDAOClass();

        private SQLQuestionDAOFactory() {
        }

        private static Class<SQLQuestionDAO> getDAOClass() {
                Class<SQLQuestionDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLQuestionDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SQLQuestionDAO getDAO() throws InternalErrorException {
                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
