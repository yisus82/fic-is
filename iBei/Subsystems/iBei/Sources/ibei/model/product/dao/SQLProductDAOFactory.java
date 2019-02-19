package ibei.model.product.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLProductDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLProductDAOFactory/daoClassName</code>: it must specify the
 * full class name of the class implementing <code>SQLProductDAO</code>.
 * </li>
 * </ul>
 */
public final class SQLProductDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLProductDAOFactory/daoClassName";

        private final static Class<SQLProductDAO> daoClass = getDAOClass();

        private SQLProductDAOFactory() {
        }

        private static Class<SQLProductDAO> getDAOClass() {

                Class<SQLProductDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLProductDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;

        }

        public static SQLProductDAO getDAO() throws InternalErrorException {

                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}