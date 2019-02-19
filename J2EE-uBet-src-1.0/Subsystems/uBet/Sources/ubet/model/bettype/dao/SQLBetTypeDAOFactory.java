package ubet.model.bettype.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLBetTypeDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLBetTypeDAOFactory/daoClassName</code>: it must specify the
 * full class name of the class implementing <code>SQLBetTypeDAO</code>.</li>
 * </ul>
 */
public final class SQLBetTypeDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLBetTypeDAOFactory/daoClassName";

        private final static Class<SQLBetTypeDAO> daoClass = getDAOClass();

        private SQLBetTypeDAOFactory() {
        }

        private static Class<SQLBetTypeDAO> getDAOClass() {
                Class<SQLBetTypeDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLBetTypeDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SQLBetTypeDAO getDAO() throws InternalErrorException {
                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
