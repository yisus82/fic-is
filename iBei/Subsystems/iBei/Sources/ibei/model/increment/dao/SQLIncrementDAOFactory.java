package ibei.model.increment.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLIncrementDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLIncrementDAOFactory/daoClassName</code>: it must specify the
 * full class name of the class implementing <code>SQLIncrementDAO</code>.
 * </li>
 * </ul>
 */
public final class SQLIncrementDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLIncrementDAOFactory/daoClassName";

        private final static Class<SQLIncrementDAO> daoClass = getDAOClass();

        private SQLIncrementDAOFactory() {
        }

        private static Class<SQLIncrementDAO> getDAOClass() {

                Class<SQLIncrementDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLIncrementDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;

        }

        public static SQLIncrementDAO getDAO() throws InternalErrorException {

                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}
