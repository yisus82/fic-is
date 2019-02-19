package ubet.model.event.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLEventDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLEventDAOFactory/daoClassName</code>: it must specify the
 * full class name of the class implementing <code>SQLEventDAO</code>.</li>
 * </ul>
 */
public final class SQLEventDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLEventDAOFactory/daoClassName";

        private final static Class<SQLEventDAO> daoClass = getDAOClass();

        private SQLEventDAOFactory() {
        }

        private static Class<SQLEventDAO> getDAOClass() {
                Class<SQLEventDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLEventDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SQLEventDAO getDAO() throws InternalErrorException {
                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
