package ibei.model.bid.dao;

import ibei.model.bid.to.BidTO;

import java.sql.Connection;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLBidDAO {

        public BidTO create(Connection connection, BidTO bidVO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long bidID)
                        throws InternalErrorException;

        public BidTO find(Connection connection, Long bidID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public BidTO findMax(Connection connection, Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<BidTO> findByProduct(Connection connection,
                        Long productID, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<BidTO> findByUser(Connection connection, String userID,
                        int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public void update(Connection connection, BidTO bidVO)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public void remove(Connection connection, Long bidID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
