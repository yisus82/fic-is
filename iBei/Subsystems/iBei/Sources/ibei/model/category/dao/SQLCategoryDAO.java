package ibei.model.category.dao;

import ibei.model.category.to.CategoryTO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLCategoryDAO {

        public CategoryTO find(Connection connection, String categoryID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public HashMap<String, CategoryTO> findAll(Connection connection)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<CategoryTO> findLeafs(Connection connection)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
