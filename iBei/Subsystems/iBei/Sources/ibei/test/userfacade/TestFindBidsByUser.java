package ibei.test.userfacade;

import ibei.model.bid.to.BidTO;
import ibei.model.product.to.ProductDetailsTO;
import ibei.model.product.to.ProductTO;
import ibei.model.sellerprofile.to.SellerProfileTO;
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

public class TestFindBidsByUser extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private SellerProfileTO seller;

        private UserProfileTO user1;

        private ProductTO product1;

        private ProductTO product2;

        private ProductTO product3;

        private BidTO bid1;

        private BidTO bid2;

        private BidTO bid3;

        public TestFindBidsByUser(String name) {
                super(name);
                try {
                        this.testFacade = TestFacadeDelegateFactory
                                        .getDelegate();
                        this.userFacade = UserFacadeDelegateFactory
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
                UserProfileDetailsTO details1 = new UserProfileDetailsTO(
                                "Name1", "Surname1", "Em@il1", "Street1",
                                "City1", "State1", new Short("15080"), "ES");
                user1 = new UserProfileTO("login1", "PassWord1", details1);
                userFacade.registerUser(user1);

                UserProfileDetailsTO details = new UserProfileDetailsTO("Name",
                                "Surname", "Em@il", "Street", "City", "State",
                                new Short("15080"), "ES");
                Calendar date = Calendar.getInstance();
                date.add(Calendar.YEAR, 2);

                seller = new SellerProfileTO("Login", "PassWord", details,
                                "CreditCardNumber", date);
                userFacade.registerUser(seller);

                date = Calendar.getInstance();
                date.add(Calendar.HOUR_OF_DAY, -1);

                ProductDetailsTO productDetails1 = new ProductDetailsTO(date,
                                new Double("10.95"), "Shipping Info1", "Root",
                                seller.getLogin(), null, null);
                product1 = new ProductTO(new Long("-1"), "Name 1",
                                "Description 1", Calendar.getInstance(),
                                new Double("10.95"), productDetails1);

                date.add(Calendar.HOUR_OF_DAY, -2);

                ProductDetailsTO productDetails2 = new ProductDetailsTO(date,
                                new Double("0.95"), "Shipping Info2", "Root",
                                seller.getLogin(), null, null);
                product2 = new ProductTO(new Long("-1"), "Name 2",
                                "Description 2", Calendar.getInstance(),
                                new Double("0.95"), productDetails2);

                date.add(Calendar.HOUR_OF_DAY, -3);

                ProductDetailsTO productDetails3 = new ProductDetailsTO(date,
                                new Double("130.65"), "Shipping Info3", "Root",
                                seller.getLogin(), null, null);
                product3 = new ProductTO(new Long("-1"), "Name 3",
                                "Description 3", Calendar.getInstance(),
                                new Double("130.65"), productDetails3);

                product1 = userFacade.announce(product1);
                product2 = userFacade.announce(product2);
                product3 = userFacade.announce(product3);

                date = Calendar.getInstance();
                date.add(Calendar.HOUR_OF_DAY, -1);

                bid1 = new BidTO(new Long("-1"), user1.getLogin(), product1
                                .getProductID(), new Double("10.35"),
                                new Double("25.75"), date);

                date.add(Calendar.HOUR_OF_DAY, -2);

                bid2 = new BidTO(new Long("-1"), user1.getLogin(), product2
                                .getProductID(), new Double("1.35"),
                                new Double("5.75"), date);

                date.add(Calendar.HOUR_OF_DAY, -3);

                bid3 = new BidTO(new Long("-1"), user1.getLogin(), product3
                                .getProductID(), new Double("0.35"),
                                new Double("2.75"), date);

                bid1 = userFacade.bid(bid1);
                bid2 = userFacade.bid(bid2);
                bid3 = userFacade.bid(bid3);

        }

        public void tearDown() throws Exception {

                /* Leaving the DB as it were before. */
                testFacade.removeBid(bid1.getBidID());
                testFacade.removeBid(bid2.getBidID());
                testFacade.removeBid(bid3.getBidID());
                testFacade.removeProduct(product1.getProductID());
                testFacade.removeProduct(product2.getProductID());
                testFacade.removeProduct(product3.getProductID());
                testFacade.removeUserProfile(user1.getLogin());
                testFacade.removeUserProfile(seller.getLogin());

        }

        public void testFindAllBids() throws Exception {

                try {
                        /* Find bids. */
                        Vector<BidTO> results = userFacade.findBidsByUser(user1
                                        .getLogin(), -1, -1);

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
                        Vector<BidTO> results = userFacade.findBidsByUser(user1
                                        .getLogin(), 1, 2);

                        if (results.size() != 2)
                                fail("Incorrect number of bids found (Expected = 2 Found = "
                                                + results.size() + ")");

                        /* Comparations. */
                        BidTO bidFound = results.get(0);
                        assertEquals(bid1, bidFound);

                        bidFound = results.get(1);
                        assertEquals(bid2, bidFound);

                        /* Page 2. */
                        results = userFacade.findBidsByUser(user1.getLogin(),
                                        3, 2);

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
                        Vector<BidTO> results = userFacade.findBidsByUser(
                                        "login2", 1, 2);

                        if (!results.isEmpty())
                                fail("Bids were found where there should be none");
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestFindBidsByUser.class);
        }

}
