package ubet.model.account.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLAccountDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLAccountDAOFactory/daoClassName</code>: it must specify the
 * full class name of the class implementing <code>SQLaccountDAO</code>.</li>
 * </ul>
 */
public final class SQLAccountDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLAccountDAOFactory/daoClassName";

        private final static Class<SQLAccountDAO> daoClass = getDAOClass();

        private SQLAccountDAOFactory() {
        }

        private static Class<SQLAccountDAO> getDAOClass() {
                Class<SQLAccountDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLAccountDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SQLAccountDAO getDAO() throws InternalErrorException {
                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
