package ibei.test.productfacade;

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

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;
import junit.framework.TestCase;

public class TestFindProduct extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private ProductFacadeDelegate productFacade;

        private UserProfileTO user;

        private ProductTO product;

        public TestFindProduct(String name) {
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

                ProductDetailsTO details = new ProductDetailsTO(Calendar
                                .getInstance(), new Double("10.95"),
                                "Shipping Info1", "Root", user.getLogin(),
                                null, null);
                product = new ProductTO(new Long("-1"), "Name 1",
                                "Description 1", Calendar.getInstance(),
                                new Double("10.95"), details);

                product = userFacade.announce(product);

        }

        public void tearDown() throws Exception {

                /* Leaving the DB as it were before. */
                testFacade.removeProduct(product.getProductID());
                testFacade.removeUserProfile(user.getLogin());

        }

        public void testFindExistentProduct() throws Exception {

                try {
                        /* Find product. */
                        ProductTO productFound = productFacade
                                        .findProduct(product.getProductID());

                        /* Comparation. */
                        assertEquals(product, productFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindNonExistentProduct() throws Exception {

                try {
                        /* Find product. */
                        productFacade.findProduct(new Long("-2"));

                        fail("A non-existent product was found");
                } catch (InstanceNotFoundException e) {
                        /* This is the correct way to exit this test. */
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestFindProduct.class);
        }

}
