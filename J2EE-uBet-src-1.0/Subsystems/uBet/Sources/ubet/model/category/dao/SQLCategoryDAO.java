package ubet.model.category.dao;

import java.sql.Connection;
import java.util.List;

import ubet.model.category.to.CategoryTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLCategoryDAO {

        /**
         * Checks if a category exists in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param categoryID
         *                the category identifier
         * @return <code>true</code> if the category exists<br>
         *         <code>false</code> if the category doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, String categoryID)
                        throws InternalErrorException;

        /**
         * Finds a category in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param categoryID
         *                the category identifier
         * @return a transfer object with the category data
         * @throws InstanceNotFoundException
         *                 if there is no category with the
         *                 <code>categoryID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public CategoryTO find(Connection connection, String categoryID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Finds all the categories in the database.
         * 
         * @param connection
         *                the connection to the database
         * @return a collection of transfer objects with the categories data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<CategoryTO> getAll(Connection connection)
                        throws InternalErrorException;

}
