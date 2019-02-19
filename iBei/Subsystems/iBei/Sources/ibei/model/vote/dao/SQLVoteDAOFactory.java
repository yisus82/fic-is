package ibei.model.vote.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLVoteDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLVoteDAOFactory/daoClassName</code>: it must specify the full
 * class name of the class implementing <code>SQLVoteDAO</code>.</li>
 * </ul>
 */
public final class SQLVoteDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLVoteDAOFactory/daoClassName";

        private final static Class daoClass = getDAOClass();

        private SQLVoteDAOFactory() {
        }

        private static Class getDAOClass() {

                Class theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = Class.forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;

        }

        public static SQLVoteDAO getDAO() throws InternalErrorException {

                try {
                        return (SQLVoteDAO) daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}
