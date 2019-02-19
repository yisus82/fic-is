package ibei.test.productfacade;

import ibei.model.product.to.ProductDetailsTO;
import ibei.model.product.to.ProductTO;
import ibei.model.productfacade.delegate.ProductFacadeDelegate;
import ibei.model.productfacade.delegate.ProductFacadeDelegateFactory;
import ibei.model.productfacade.to.ProductResultTO;
import ibei.model.sellerprofile.to.SellerProfileTO;
import ibei.model.userfacade.delegate.UserFacadeDelegate;
import ibei.model.userfacade.delegate.UserFacadeDelegateFactory;
import ibei.model.userprofile.to.UserProfileDetailsTO;
import ibei.model.util.GlobalNames;
import ibei.test.testfacade.delegate.TestFacadeDelegate;
import ibei.test.testfacade.delegate.TestFacadeDelegateFactory;

import java.util.Calendar;
import java.util.Vector;

import junit.framework.TestCase;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestFindProductsByCategory extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private ProductFacadeDelegate productFacade;

        private SellerProfileTO seller;

        private ProductTO product1;

        private ProductTO product2;

        private ProductTO product3;

        public TestFindProductsByCategory(String name) {
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

                ProductDetailsTO details1 = new ProductDetailsTO(date,
                                new Double("10.95"), "Shipping Info1", "Root",
                                seller.getLogin(), null, null);
                product1 = new ProductTO(new Long("-1"), "Name 1",
                                "Description 1", Calendar.getInstance(),
                                new Double("10.95"), details1);

                date.add(Calendar.HOUR_OF_DAY, -2);

                ProductDetailsTO details2 = new ProductDetailsTO(date,
                                new Double("0.95"), "Shipping Info2", "Root",
                                seller.getLogin(), null, null);
                product2 = new ProductTO(new Long("-1"), "Name 2",
                                "Description 2", Calendar.getInstance(),
                                new Double("0.95"), details2);

                date.add(Calendar.HOUR_OF_DAY, -3);

                ProductDetailsTO details3 = new ProductDetailsTO(date,
                                new Double("130.65"), "Shipping Info3", "Root",
                                seller.getLogin(), null, null);
                product3 = new ProductTO(new Long("-1"), "Name 3",
                                "Description 3", Calendar.getInstance(),
                                new Double("130.65"), details3);

                product1 = userFacade.announce(product1);
                product2 = userFacade.announce(product2);
                product3 = userFacade.announce(product3);

        }

        public void tearDown() throws Exception {

                /* Leaving the DB as it were before. */
                testFacade.removeProduct(product1.getProductID());
                testFacade.removeProduct(product2.getProductID());
                testFacade.removeProduct(product3.getProductID());
                testFacade.removeUserProfile(seller.getLogin());

        }

        public void testFindAllProducts() throws Exception {

                try {
                        /* Find products. */
                        Vector<ProductResultTO> results = productFacade
                                        .findProductsByCategory("Root", -1, -1);

                        if (results.size() != 3)
                                fail("Incorrect number of products found (Expected = 3 Found = "
                                                + results.size() + ")");

                        /* Create the correct ProductResultTO. */
                        ProductResultTO originalProduct = new ProductResultTO(
                                        product1.getProductID(), product1
                                                        .getName(), product1
                                                        .getEndTime(), product1
                                                        .getCurrentPrice());

                        /* Comparation. */
                        ProductResultTO productFound = results.get(0);
                        assertEquals(originalProduct, productFound);

                        /* Create the correct ProductResultTO. */
                        originalProduct = new ProductResultTO(product2
                                        .getProductID(), product2.getName(),
                                        product2.getEndTime(), product2
                                                        .getCurrentPrice());

                        /* Comparation. */
                        productFound = results.get(1);
                        assertEquals(originalProduct, productFound);

                        /* Create the correct ProductResultTO. */
                        originalProduct = new ProductResultTO(product3
                                        .getProductID(), product3.getName(),
                                        product3.getEndTime(), product3
                                                        .getCurrentPrice());

                        /* Comparation. */
                        productFound = results.get(2);
                        assertEquals(originalProduct, productFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindProductsPageByPage() throws Exception {

                try {
                        /* Find products. */
                        Vector<ProductResultTO> results = productFacade
                                        .findProductsByCategory("Root", 1, 2);

                        if (results.size() != 2)
                                fail("Incorrect number of products found (Expected = 2 Found = "
                                                + results.size() + ")");

                        /* Create the correct ProductResultTO. */
                        ProductResultTO originalProduct = new ProductResultTO(
                                        product1.getProductID(), product1
                                                        .getName(), product1
                                                        .getEndTime(), product1
                                                        .getCurrentPrice());

                        /* Comparation. */
                        ProductResultTO productFound = results.get(0);
                        assertEquals(originalProduct, productFound);

                        /* Create the correct ProductResultTO. */
                        originalProduct = new ProductResultTO(product2
                                        .getProductID(), product2.getName(),
                                        product2.getEndTime(), product2
                                                        .getCurrentPrice());

                        /* Comparation. */
                        productFound = results.get(1);
                        assertEquals(originalProduct, productFound);

                        /* Page 2. */
                        results = productFacade.findProductsByCategory("Root",
                                        3, 2);

                        if (results.size() != 1)
                                fail("Incorrect number of products found (Expected = 1 Found = "
                                                + results.size() + ")");

                        /* Create the correct ProductResultTO. */
                        originalProduct = new ProductResultTO(product3
                                        .getProductID(), product3.getName(),
                                        product3.getEndTime(), product3
                                                        .getCurrentPrice());

                        /* Comparation. */
                        productFound = results.get(0);
                        assertEquals(originalProduct, productFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindProductsByCategoryWithNoProducts() throws Exception {

                try {
                        /* Find products. */
                        Vector<ProductResultTO> results = productFacade
                                        .findProductsByCategory("EmptyCat", 1,
                                                        2);

                        if (!results.isEmpty())
                                fail("Products were found where there should be none");
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestFindProductsByCategory.class);
        }

}
