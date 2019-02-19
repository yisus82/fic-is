package ibei.model.userfacade.delegate;

import ibei.model.bid.to.BidTO;
import ibei.model.product.to.ProductTO;
import ibei.model.productfacade.to.ProductResultTO;
import ibei.model.userfacade.exceptions.BidOutOfTimeException;
import ibei.model.userfacade.exceptions.IncorrectPasswordException;
import ibei.model.userfacade.to.LoginResultTO;
import ibei.model.userprofile.to.UserProfileTO;

import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface UserFacadeDelegate {

        public UserProfileTO registerUser(UserProfileTO userProfileTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        public LoginResultTO login(String login, String password,
                        boolean passwordIsEncrypted)
                        throws InstanceNotFoundException,
                        IncorrectPasswordException, InternalErrorException;

        public UserProfileTO updateUserProfileDetails(
                        UserProfileTO userProfileTO)
                        throws InternalErrorException,
                        DuplicateInstanceException;

        public void changePassword(String oldClearPassword,
                        String newClearPassword)
                        throws IncorrectPasswordException,
                        InternalErrorException;

        public BidTO bid(BidTO bidTO) throws BidOutOfTimeException,
                        InternalErrorException;

        public ProductTO announce(ProductTO productTO)
                        throws InternalErrorException;

        public UserProfileTO findUserProfile(String login)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<BidTO> findBidsByUser(String login, int startIndex,
                        int count) throws InstanceNotFoundException,
                        InternalErrorException;

        public Vector<ProductResultTO> findProductsByUser(String login,
                        int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
