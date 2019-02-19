package ibei.test.productfacade;

import ibei.model.bid.to.BidTO;
import ibei.model.product.to.ProductDetailsTO;
import ibei.model.product.to.ProductTO;
import ibei.model.productfacade.delegate.ProductFacadeDelegate;
import ibei.model.productfacade.delegate.ProductFacadeDelegateFactory;
import ibei.model.userfacade.delegate.UserFacadeDelegate;
import ibei.model.userfacade.delegate.UserFacadeDelegateFactory;
import ibei.model.userprofile.to.UserProfileDetailsTO;
import ibei.model.userprofile.to.UserProfileTO;
import ibei.model.util.GlobalNames;
import ibei.test.testfacade.delegate.TestFacadeDelegate;
import ibei.test.testfacade.delegate.TestFacadeDelegateFactory;

import java.util.Calendar;
import java.util.Vector;

import junit.framework.TestCase;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestFindBidsByProduct extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private ProductFacadeDelegate productFacade;

        private UserProfileTO user;

        private ProductTO product;

        private BidTO bid1;

        private BidTO bid2;

        private BidTO bid3;

        public TestFindBidsByProduct(String name) {
                super(name);
                try {
                        this.testFacade = TestFacadeDelegateFactory
                                        .getDelegate();
                        this.userFacade = UserFacadeDelegateFactory
                                        .getDelegate();
                        this.productFacade = ProductFacadeDelegateFactory
                                        .getDelegate();
                } catch (InternalErrorException e) {
                        e.printStackTrace();
                }
        }

        public void setUp() throws Exception {

                try {
                        DataSourceLocator
                                        .getDataSource(GlobalNames.IBEI_DATA_SOURCE);
                } catch (InternalErrorException e) {
                        /* Add the datasource. */
                        DataSourceLocator.addDataSource(
                                        GlobalNames.IBEI_DATA_SOURCE,
                                        new SimpleDataSource());
                }

                /* Inicialization of DB. */
                UserProfileDetailsTO userDetails = new UserProfileDetailsTO(
                                "Name", "Surname", "em@il.com", "Street",
                                "City", "State", new Short("15080"), "ES");
                user = new UserProfileTO("Login", "PassWord", userDetails);

                userFacade.registerUser(user);

                Calendar date = Calendar.getInstance();
                date.add(Calendar.HOUR_OF_DAY, 5);
                ProductDetailsTO productDetails = new ProductDetailsTO(date,
                                new Double("15.25"), "Shipping Info", "Root",
                                user.getLogin(), null, null);
                product = new ProductTO(new Long("-1"), "Product1",
                                "A test product", date, new Double("15.25"),
                                productDetails);
                product = userFacade.announce(product);

                date = Calendar.getInstance();
                date.add(Calendar.MINUTE, 1);

                bid1 = new BidTO(new Long("-1"), user.getLogin(), product
                                .getProductID(), new Double("15.35"),
                                new Double("25.75"), date);

                date.add(Calendar.MINUTE, 2);

                bid2 = new BidTO(new Long("-1"), user.getLogin(), product
                                .getProductID(), new Double("16.85"),
                                new Double("35.75"), date);

                date.add(Calendar.MINUTE, 3);

                bid3 = new BidTO(new Long("-1"), user.getLogin(), product
                                .getProductID(), new Double("20.35"),
                                new Double("23.45"), date);

                bid1 = userFacade.bid(bid1);
                bid2 = userFacade.bid(bid2);
                bid3 = userFacade.bid(bid3);

                /*
                 * Get the bids from the database (because they might be changed
                 * because of the automatic bid system).
                 */
                bid1 = testFacade.findBid(bid1.getBidID());
                bid2 = testFacade.findBid(bid2.getBidID());
                bid3 = testFacade.findBid(bid3.getBidID());

        }

        public void tearDown() throws Exception {

                /* Leaving the DB as it were before. */
                testFacade.removeBid(bid1.getBidID());
                testFacade.removeBid(bid2.getBidID());
                testFacade.removeBid(bid3.getBidID());
                testFacade.removeProduct(product.getProductID());
                testFacade.removeUserProfile(user.getLogin());
        }

        public void testFindAllBids() throws Exception {

                try {
                        /* Find bids. */
                        Vector<BidTO> results = productFacade
                                        .findBidsByProduct(product
                                                        .getProductID(), -1, -1);

                        if (results.size() != 3)
                                fail("Incorrect number of bids found (Expected = 3 Found = "
                                                + results.size() + ")");

                        /* Comparations. */
                        BidTO bidFound = results.get(0);
                        assertEquals(bid1, bidFound);

                        bidFound = results.get(1);
                        assertEquals(bid2, bidFound);

                        bidFound = results.get(2);
                        assertEquals(bid3, bidFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindBidsPageByPage() throws Exception {

                try {
                        /* Find bids. */
                        Vector<BidTO> results = productFacade
                                        .findBidsByProduct(product
                                                        .getProductID(), 1, 2);

                        if (results.size() != 2)
                                fail("Incorrect number of bids found (Expected = 2 Found = "
                                                + results.size() + ")");

                        /* Comparations. */
                        BidTO bidFound = results.get(0);
                        assertEquals(bid1, bidFound);

                        bidFound = results.get(1);
                        assertEquals(bid2, bidFound);

                        /* Page 2. */
                        results = productFacade.findBidsByProduct(product
                                        .getProductID(), 3, 2);

                        if (results.size() != 1)
                                fail("Incorrect number of bids found (Expected = 1 Found = "
                                                + results.size() + ")");

                        /* Comparation. */
                        bidFound = results.get(0);
                        assertEquals(bid3, bidFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindBidsByUserWithNoBids() throws Exception {

                try {
                        /* Find bids. */
                        Vector<BidTO> results = productFacade
                                        .findBidsByProduct(new Long("-2"), 1, 2);

                        if (!results.isEmpty())
                                fail("Bids were found where there should be none");
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestFindBidsByProduct.class);
        }

}
