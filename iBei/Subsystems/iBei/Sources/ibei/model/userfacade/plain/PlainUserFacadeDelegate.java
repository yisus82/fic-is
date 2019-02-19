package ibei.model.userfacade.plain;

import ibei.model.bid.to.BidTO;
import ibei.model.product.to.ProductTO;
import ibei.model.productfacade.to.ProductResultTO;
import ibei.model.sellerprofile.to.SellerProfileTO;
import ibei.model.userfacade.delegate.UserFacadeDelegate;
import ibei.model.userfacade.exceptions.BidOutOfTimeException;
import ibei.model.userfacade.exceptions.IncorrectPasswordException;
import ibei.model.userfacade.plain.actions.AnnounceAction;
import ibei.model.userfacade.plain.actions.BidAction;
import ibei.model.userfacade.plain.actions.ChangePasswordAction;
import ibei.model.userfacade.plain.actions.FindBidsByUserAction;
import ibei.model.userfacade.plain.actions.FindProductsByUserAction;
import ibei.model.userfacade.plain.actions.FindUserProfileAction;
import ibei.model.userfacade.plain.actions.LoginAction;
import ibei.model.userfacade.plain.actions.RegisterUserAction;
import ibei.model.userfacade.plain.actions.UpdateUserProfileDetailsAction;
import ibei.model.userfacade.to.LoginResultTO;
import ibei.model.userprofile.to.UserProfileTO;
import ibei.model.util.GlobalNames;

import java.util.Vector;

import javax.sql.DataSource;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.PlainActionProcessor;

public class PlainUserFacadeDelegate implements UserFacadeDelegate {

        private String login;

        public PlainUserFacadeDelegate() {
                this.login = null;
        }

        private DataSource getDataSource() throws InternalErrorException {
                return DataSourceLocator
                                .getDataSource(GlobalNames.IBEI_DATA_SOURCE);
        }

        public UserProfileTO registerUser(UserProfileTO userProfileTO)
                        throws DuplicateInstanceException,
                        InternalErrorException {

                try {

                        RegisterUserAction action = new RegisterUserAction(
                                        userProfileTO);

                        this.login = userProfileTO.getLogin();
                        return (UserProfileTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (DuplicateInstanceException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public LoginResultTO login(String login, String password,
                        boolean passwordIsEncrypted)
                        throws InstanceNotFoundException,
                        IncorrectPasswordException, InternalErrorException {

                try {

                        LoginAction action = new LoginAction(login, password,
                                        passwordIsEncrypted);

                        this.login = login;
                        return (LoginResultTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (IncorrectPasswordException e) {
                        throw e;
                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public UserProfileTO updateUserProfileDetails(
                        UserProfileTO userProfileTO)
                        throws InternalErrorException,
                        DuplicateInstanceException {

                try {

                        UpdateUserProfileDetailsAction action = new UpdateUserProfileDetailsAction(
                                        login, userProfileTO);

                        if (userProfileTO.getType() == UserProfileTO.SELLER)
                                return (SellerProfileTO) PlainActionProcessor
                                                .process(getDataSource(),
                                                                action);
                        return (UserProfileTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (DuplicateInstanceException e) {
                        throw e;
                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public void changePassword(String oldClearPassword,
                        String newClearPassword)
                        throws IncorrectPasswordException,
                        InternalErrorException {

                try {

                        ChangePasswordAction action = new ChangePasswordAction(
                                        login, oldClearPassword,
                                        newClearPassword);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (IncorrectPasswordException e) {
                        throw e;
                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public BidTO bid(BidTO bidTO) throws BidOutOfTimeException,
                        InternalErrorException {

                try {

                        BidAction action = new BidAction(bidTO);

                        return (BidTO) PlainActionProcessor.process(
                                        getDataSource(), action);
                } catch (BidOutOfTimeException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public ProductTO announce(ProductTO productTO)
                        throws InternalErrorException {

                try {

                        AnnounceAction action = new AnnounceAction(productTO);

                        return (ProductTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public UserProfileTO findUserProfile(String login)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        FindUserProfileAction action = new FindUserProfileAction(
                                        login);

                        return (UserProfileTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public Vector<BidTO> findBidsByUser(String login, int startIndex,
                        int count) throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        FindBidsByUserAction action = new FindBidsByUserAction(
                                        login, startIndex, count);

                        return (Vector<BidTO>) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public Vector<ProductResultTO> findProductsByUser(String login,
                        int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        FindProductsByUserAction action = new FindProductsByUserAction(
                                        login, startIndex, count);

                        return (Vector<ProductResultTO>) PlainActionProcessor
                                        .process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}
