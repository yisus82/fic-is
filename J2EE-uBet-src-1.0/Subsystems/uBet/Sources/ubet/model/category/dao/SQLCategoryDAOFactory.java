package ubet.model.category.dao;

import ubet.model.category.dao.SQLCategoryDAO;
import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SQLCategoryDAO</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SQLCategoryDAOFactory/daoClassName</code>: it must specify the
 * full class name of the class implementing <code>SQLCategoryDAO</code>.</li>
 * </ul>
 */
public final class SQLCategoryDAOFactory {

        private final static String DAO_CLASS_NAME_PARAMETER = "SQLCategoryDAOFactory/daoClassName";

        private final static Class<SQLCategoryDAO> daoClass = getDAOClass();

        private SQLCategoryDAOFactory() {
        }

        private static Class<SQLCategoryDAO> getDAOClass() {
                Class<SQLCategoryDAO> theClass = null;

                try {

                        String daoClassName = ConfigurationParametersManager
                                        .getParameter(DAO_CLASS_NAME_PARAMETER);

                        theClass = (Class<SQLCategoryDAO>) Class
                                        .forName(daoClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SQLCategoryDAO getDAO() throws InternalErrorException {
                try {
                        return daoClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}