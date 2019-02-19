package ibei.model.bid.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLBidDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLBidDAOFactory/daoClassName</code>: it must specify the full
 * class name of the class implementing <code>SQLBidDAO</code>.</li>
 * </ul>
 */
public final class SQLBidDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLBidDAOFactory/daoClassName";

        private final static Class<SQLBidDAO> daoClass = getDAOClass();

        private SQLBidDAOFactory() {
        }

        private static Class<SQLBidDAO> getDAOClass() {

                Class<SQLBidDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLBidDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;

        }

        public static SQLBidDAO getDAO() throws InternalErrorException {

                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}
