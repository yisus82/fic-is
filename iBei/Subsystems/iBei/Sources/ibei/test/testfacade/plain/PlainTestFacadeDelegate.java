package ibei.test.testfacade.plain;

import ibei.model.bid.to.BidTO;
import ibei.model.util.GlobalNames;
import ibei.test.testfacade.delegate.TestFacadeDelegate;
import ibei.test.testfacade.plain.actions.FindBidAction;
import ibei.test.testfacade.plain.actions.RemoveBidAction;
import ibei.test.testfacade.plain.actions.RemoveProductAction;
import ibei.test.testfacade.plain.actions.RemoveUserProfileAction;

import javax.sql.DataSource;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.PlainActionProcessor;

public class PlainTestFacadeDelegate implements TestFacadeDelegate {

        private DataSource getDataSource() throws InternalErrorException {
                return DataSourceLocator
                                .getDataSource(GlobalNames.IBEI_DATA_SOURCE);
        }

        public void removeUserProfile(String login)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        RemoveUserProfileAction action = new RemoveUserProfileAction(
                                        login);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public void removeProduct(Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        RemoveProductAction action = new RemoveProductAction(
                                        productID);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public void removeBid(Long bidID) throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        RemoveBidAction action = new RemoveBidAction(bidID);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public BidTO findBid(Long bidID) throws InstanceNotFoundException, InternalErrorException {
                
                try {

                        FindBidAction action = new FindBidAction(bidID);

                        return (BidTO) PlainActionProcessor.process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
                
        }

}
