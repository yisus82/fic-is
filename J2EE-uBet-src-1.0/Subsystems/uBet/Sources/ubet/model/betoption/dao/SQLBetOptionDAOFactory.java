package ubet.model.betoption.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLBetOptionDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLBetOptionDAOFactory/daoClassName</code>: it must specify the
 * full class name of the class implementing <code>SQLBetOptionDAO</code>.</li>
 * </ul>
 */
public final class SQLBetOptionDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLBetOptionDAOFactory/daoClassName";

        private final static Class<SQLBetOptionDAO> daoClass = getDAOClass();

        private SQLBetOptionDAOFactory() {
        }

        private static Class<SQLBetOptionDAO> getDAOClass() {
                Class<SQLBetOptionDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLBetOptionDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SQLBetOptionDAO getDAO() throws InternalErrorException {
                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
