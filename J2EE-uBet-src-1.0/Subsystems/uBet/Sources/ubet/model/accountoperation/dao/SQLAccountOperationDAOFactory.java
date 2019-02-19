package ubet.model.accountoperation.dao;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLAccountOperationDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLAccountOperationDAOFactory/daoClassName</code>: it must
 * specify the full class name of the class implementing
 * <code>SQLAccountOperationDAO</code>.</li>
 * </ul>
 */
public final class SQLAccountOperationDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLAccountOperationDAOFactory/daoClassName";

        private final static Class<SQLAccountOperationDAO> daoClass = getDAOClass();

        private SQLAccountOperationDAOFactory() {
        }

        private static Class<SQLAccountOperationDAO> getDAOClass() {
                Class<SQLAccountOperationDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLAccountOperationDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SQLAccountOperationDAO getDAO()
                        throws InternalErrorException {
                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
