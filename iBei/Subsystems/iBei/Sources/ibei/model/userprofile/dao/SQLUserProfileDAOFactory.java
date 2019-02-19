package ibei.model.userprofile.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLUserProfileDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLUserProfileDAOFactory/daoClassName</code>: it must specify
 * the full class name of the class implementing <code>SQLUserProfileDAO</code>.
 * </li>
 * </ul>
 */
public final class SQLUserProfileDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLUserProfileDAOFactory/daoClassName";

        private final static Class<SQLUserProfileDAO> daoClass = getDAOClass();

        private SQLUserProfileDAOFactory() {
        }

        private static Class<SQLUserProfileDAO> getDAOClass() {

                Class<SQLUserProfileDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLUserProfileDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;

        }

        public static SQLUserProfileDAO getDAO() throws InternalErrorException {

                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}
