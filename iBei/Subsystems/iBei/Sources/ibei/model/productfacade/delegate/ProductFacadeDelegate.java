package ibei.model.productfacade.delegate;

import ibei.model.bid.to.BidTO;
import ibei.model.product.to.ProductTO;
import ibei.model.productfacade.to.ProductResultTO;

import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface ProductFacadeDelegate {

        public Vector<BidTO> findBidsByProduct(Long productID, int startIndex,
                        int count) throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<ProductResultTO> findProductsByCategory(
                        String categoryID, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<ProductResultTO> findProductsByKeywords(
                        Vector<String> keywords, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public ProductTO findProduct(Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
