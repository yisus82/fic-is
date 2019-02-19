package ibei.model.category;

import ibei.model.category.dao.SQLCategoryDAO;
import ibei.model.category.dao.SQLCategoryDAOFactory;
import ibei.model.category.to.CategoryTO;
import ibei.model.util.GlobalNames;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A Singleton class used to manage CategoryVO objects. As the Category SQL
 * table is read-only it is better to use a Singleton to cache the object and to
 * access the database only once.
 */
public class CategoryCache {

        private HashMap<String, CategoryTO> categories;

        private static CategoryCache instance = null;

        private CategoryCache() throws InternalErrorException {
                Connection connection = null;
                try {
                        DataSource dataSource = DataSourceLocator
                                        .getDataSource(GlobalNames.IBEI_DATA_SOURCE);
                        connection = dataSource.getConnection();
                        SQLCategoryDAO dao = SQLCategoryDAOFactory.getDAO();
                        categories = dao.findAll(connection);
                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeConnection(connection);
                }
        }

        public static CategoryCache getInstance() throws InternalErrorException {

                if (instance == null)
                        instance = new CategoryCache();
                return instance;

        }

        public HashMap<String, CategoryTO> getCategories() {
                return this.categories;
        }

        public CategoryTO getCategory(String categoryID) {
                return categories.get(categoryID);
        }

}
