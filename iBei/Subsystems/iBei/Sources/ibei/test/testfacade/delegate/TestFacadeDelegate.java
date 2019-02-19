package ibei.test.testfacade.delegate;

import ibei.model.bid.to.BidTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface TestFacadeDelegate {

        public void removeUserProfile(String login)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public void removeProduct(Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public void removeBid(Long bidID) throws InstanceNotFoundException,
                        InternalErrorException;
        
        public BidTO findBid(Long bidID) throws InstanceNotFoundException,
        InternalErrorException;

}
