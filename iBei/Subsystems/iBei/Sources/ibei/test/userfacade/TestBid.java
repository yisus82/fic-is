package ibei.test.userfacade;

import ibei.model.bid.to.BidTO;
import ibei.model.product.to.ProductDetailsTO;
import ibei.model.product.to.ProductTO;
import ibei.model.productfacade.delegate.ProductFacadeDelegate;
import ibei.model.productfacade.delegate.ProductFacadeDelegateFactory;
import ibei.model.sellerprofile.to.SellerProfileTO;
import ibei.model.userfacade.delegate.UserFacadeDelegate;
import ibei.model.userfacade.delegate.UserFacadeDelegateFactory;
import ibei.model.userfacade.exceptions.BidOutOfTimeException;
import ibei.model.userprofile.to.UserProfileDetailsTO;
import ibei.model.userprofile.to.UserProfileTO;
import ibei.model.util.GlobalNames;
import ibei.test.testfacade.delegate.TestFacadeDelegate;
import ibei.test.testfacade.delegate.TestFacadeDelegateFactory;

import java.util.Calendar;

import junit.framework.TestCase;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestBid extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private ProductFacadeDelegate productFacade;

        private SellerProfileTO seller;

        private ProductTO product;

        private UserProfileTO user1;

        private UserProfileTO user2;

        public TestBid(String name) {
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
                UserProfileDetailsTO details1 = new UserProfileDetailsTO(
                                "Name1", "Surname1", "Em@il1", "Street1",
                                "City1", "State1", new Short("15080"), "ES");
                user1 = new UserProfileTO("login1", "PassWord1", details1);

                UserProfileDetailsTO details2 = new UserProfileDetailsTO(
                                "Name2", "Surname2", "Em@il2", "Street2",
                                "City2", "State2", new Short("15080"), "ES");
                user2 = new UserProfileTO("login2", "PassWord2", details2);

                userFacade.registerUser(user1);
                userFacade.registerUser(user2);

                UserProfileDetailsTO details = new UserProfileDetailsTO("Name",
                                "Surname", "Em@il", "Street", "City", "State",
                                new Short("15080"), "ES");
                Calendar date = Calendar.getInstance();
                date.add(Calendar.YEAR, 2);

                seller = new SellerProfileTO("Login", "PassWord", details,
                                "CreditCardNumber", date);
                userFacade.registerUser(seller);

                date = Calendar.getInstance();
                ProductDetailsTO productDetails = new ProductDetailsTO(date,
                                new Double("10"), "Shipping Info", "Root",
                                seller.getLogin(), null, null);
                date.add(Calendar.MINUTE, 5);
                product = new ProductTO(new Long("-1"), "Name1",
                                "Description1", date, new Double("10"),
                                productDetails);

                product = userFacade.announce(product);

        }

        public void tearDown() throws Exception {

                /* Leaving the DB as it were before. */
                testFacade.removeProduct(product.getProductID());
                testFacade.removeUserProfile(user1.getLogin());
                testFacade.removeUserProfile(user2.getLogin());
                testFacade.removeUserProfile(seller.getLogin());

        }

        public void testBidWithOnlyOneUser() throws Exception {

                try {
                        /* Bid. */
                        BidTO bid = new BidTO(new Long("-1"), user1.getLogin(),
                                        product.getProductID(), product
                                                        .getCurrentPrice(),
                                        new Double("12"), Calendar
                                                        .getInstance());
                        bid = userFacade.bid(bid);

                        /* Get the current price of the product. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        assertEquals(product.getCurrentPrice(),
                                        new Double("10"));

                        /* Remove bid. */
                        testFacade.removeBid(bid.getBidID());

                        /* Get the winnerID. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        String winnerID = product.getProductDetails()
                                        .getWinnerID();

                        /* Comparation. */
                        assertEquals(winnerID, user1.getLogin());

                        /* Get the value of the winner bid. */
                        Double winnerBid = product.getProductDetails()
                                        .getWinnerBid();

                        /* Comparations. */
                        assertEquals(product.getCurrentPrice(), winnerBid);
                        assertEquals(new Double("10"), winnerBid);
                        assertEquals(bid.getCurrentBid(), new Double("10"));
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testBidWithTwoUsersWinnerFirst() throws Exception {

                try {

                        /* Bids. */
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.MINUTE, 1);

                        BidTO bid1 = new BidTO(new Long("-1"),
                                        user1.getLogin(), product
                                                        .getProductID(),
                                        product.getCurrentPrice(), new Double(
                                                        "12"), date);
                        bid1 = userFacade.bid(bid1);

                        /* Get the current price of the product. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        assertEquals(product.getCurrentPrice(),
                                        new Double("10"));

                        date.add(Calendar.MINUTE, 2);
                        BidTO bid2 = new BidTO(new Long("-1"),
                                        user2.getLogin(), product
                                                        .getProductID(),
                                        product.getCurrentPrice(), new Double(
                                                        "11"), date);
                        bid2 = userFacade.bid(bid2);

                        /*
                         * Get the bid from the database (because it might be changed
                         * because of the automatic bid system).
                         */
                        bid1 = testFacade.findBid(bid1.getBidID());

                        /* Get the current price of the product. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        assertEquals(product.getCurrentPrice(),
                                        new Double("11"));

                        /* Remove bids. */
                        testFacade.removeBid(bid1.getBidID());
                        testFacade.removeBid(bid2.getBidID());

                        /* Get the winnerID. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        String winnerID = product.getProductDetails()
                                        .getWinnerID();

                        /* Comparation. */
                        assertEquals(winnerID, user1.getLogin());

                        /* Get the value of the winner bid. */
                        Double winnerBid = product.getProductDetails()
                                        .getWinnerBid();

                        /* Comparations. */
                        assertEquals(product.getCurrentPrice(), winnerBid);
                        assertEquals(new Double("11"), winnerBid);
                        assertEquals(bid1.getCurrentBid(), new Double("11"));
                        assertEquals(bid2.getCurrentBid(), bid2.getMaxBid());
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testBidWithTwoUsersWinnerSecond() throws Exception {

                try {
                        /* Bids. */
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.MINUTE, 1);

                        BidTO bid1 = new BidTO(new Long("-1"),
                                        user1.getLogin(), product
                                                        .getProductID(),
                                        product.getCurrentPrice(), new Double(
                                                        "12"), date);
                        bid1 = userFacade.bid(bid1);

                        /* Get the current price of the product. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        assertEquals(product.getCurrentPrice(),
                                        new Double("10"));

                        date.add(Calendar.MINUTE, 2);
                        BidTO bid2 = new BidTO(new Long("-1"),
                                        user2.getLogin(), product
                                                        .getProductID(),
                                        product.getCurrentPrice(), new Double(
                                                        "14"), date);
                        bid2 = userFacade.bid(bid2);

                        /*
                         * Get the bid from the database (because it might be changed
                         * because of the automatic bid system).
                         */
                        bid1 = testFacade.findBid(bid1.getBidID());

                        /* Get the current price of the product. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        assertEquals(product.getCurrentPrice(), new Double(
                                        "12.5"));

                        /* Remove bids. */
                        testFacade.removeBid(bid1.getBidID());
                        testFacade.removeBid(bid2.getBidID());

                        /* Get the winnerID. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        String winnerID = product.getProductDetails()
                                        .getWinnerID();

                        /* Comparation. */
                        assertEquals(winnerID, user2.getLogin());

                        /* Get the value of the winner bid. */
                        Double winnerBid = product.getProductDetails()
                                        .getWinnerBid();

                        /* Comparations. */
                        assertEquals(product.getCurrentPrice(), winnerBid);
                        assertEquals(new Double("12.5"), winnerBid);
                        assertEquals(bid1.getCurrentBid(), bid1.getMaxBid());
                        assertEquals(bid2.getCurrentBid(), new Double("12.5"));
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testBidWithTwoUsersTie() throws Exception {

                try {
                        /* Bids. */
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.MINUTE, 1);

                        BidTO bid1 = new BidTO(new Long("-1"),
                                        user1.getLogin(), product
                                                        .getProductID(),
                                        product.getCurrentPrice(), new Double(
                                                        "12"), date);
                        bid1 = userFacade.bid(bid1);

                        /* Get the current price of the product. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        assertEquals(product.getCurrentPrice(),
                                        new Double("10"));

                        date.add(Calendar.MINUTE, 2);
                        BidTO bid2 = new BidTO(new Long("-1"),
                                        user2.getLogin(), product
                                                        .getProductID(),
                                        product.getCurrentPrice(), new Double(
                                                        "12"), date);
                        bid2 = userFacade.bid(bid2);

                        /*
                         * Get the bid from the database (because it might be changed
                         * because of the automatic bid system).
                         */
                        bid1 = testFacade.findBid(bid1.getBidID());

                        /* Get the current price of the product. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        assertEquals(product.getCurrentPrice(),
                                        new Double("12"));

                        /* Remove bids. */
                        testFacade.removeBid(bid1.getBidID());
                        testFacade.removeBid(bid2.getBidID());

                        /* Get the winnerID. */
                        product = productFacade.findProduct(product
                                        .getProductID());
                        String winnerID = product.getProductDetails()
                                        .getWinnerID();

                        /* Comparation. */
                        assertEquals(winnerID, user1.getLogin());

                        /* Get the value of the winner bid. */
                        Double winnerBid = product.getProductDetails()
                                        .getWinnerBid();

                        /* Comparations. */
                        assertEquals(product.getCurrentPrice(), winnerBid);
                        assertEquals(new Double("12"), winnerBid);
                        assertEquals(bid1.getCurrentBid(), new Double("12"));
                        assertEquals(bid2.getCurrentBid(), new Double("12"));
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testBidOutOfTime() throws Exception {

                try {
                        /* Bid. */
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.MINUTE, 10);
                        BidTO bid = new BidTO(new Long("-1"), user1.getLogin(),
                                        product.getProductID(), product
                                                        .getCurrentPrice(),
                                        new Double("12"), date);
                        userFacade.bid(bid);

                        /* Remove bid. */
                        testFacade.removeBid(bid.getBidID());

                        fail("A bid was made when the time to bid was over");
                } catch (BidOutOfTimeException e) {
                        /* This is the correct way to exit this test. */
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestBid.class);
        }

}
