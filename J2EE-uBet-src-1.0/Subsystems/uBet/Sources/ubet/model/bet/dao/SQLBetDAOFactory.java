package ubet.model.bet.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLBetDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLBetDAOFactory/daoClassName</code>: it must specify the full
 * class name of the class implementing <code>SQLBetDAO</code>.</li>
 * </ul>
 */
public final class SQLBetDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLBetDAOFactory/daoClassName";

        private final static Class<SQLBetDAO> daoClass = getDAOClass();

        private SQLBetDAOFactory() {
        }

        private static Class<SQLBetDAO> getDAOClass() {
                Class<SQLBetDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLBetDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SQLBetDAO getDAO() throws InternalErrorException {
                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
