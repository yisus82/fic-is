package ubet.model.country.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLCountryDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLCountryDAOFactory/daoClassName</code>: it must specify the
 * full class name of the class implementing <code>SQLCountryDAO</code>.
 * </li>
 * </ul>
 */
public final class SQLCountryDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLCountryDAOFactory/daoClassName";

        private final static Class<SQLCountryDAO> daoClass = getDAOClass();

        private SQLCountryDAOFactory() {
        }

        private static Class<SQLCountryDAO> getDAOClass() {

                Class<SQLCountryDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLCountryDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;

        }

        public static SQLCountryDAO getDAO() throws InternalErrorException {

                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}
