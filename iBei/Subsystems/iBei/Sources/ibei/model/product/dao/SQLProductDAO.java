package ibei.model.product.dao;

import ibei.model.product.to.ProductTO;
import ibei.model.productfacade.to.ProductResultTO;

import java.sql.Connection;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLProductDAO {

        public ProductTO create(Connection connection, ProductTO productTO)
                        throws InstanceException, InternalErrorException;

        public boolean exists(Connection connection, Long productID)
                        throws InternalErrorException;

        public ProductTO find(Connection connection, Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<ProductResultTO> findByCategory(Connection connection,
                        String categoryID, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<ProductResultTO> findByKeywords(Connection connection,
                        Vector<String> keywords, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<ProductResultTO> findByUser(Connection connection,
                        String login, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public void update(Connection connection, ProductTO productTO)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public void remove(Connection connection, Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
