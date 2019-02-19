package ibei.test.userfacade;

import ibei.model.product.to.ProductDetailsTO;
import ibei.model.product.to.ProductTO;
import ibei.model.userfacade.delegate.UserFacadeDelegate;
import ibei.model.userfacade.delegate.UserFacadeDelegateFactory;
import ibei.model.userprofile.to.UserProfileDetailsTO;
import ibei.model.userprofile.to.UserProfileTO;
import ibei.model.util.GlobalNames;
import ibei.test.testfacade.delegate.TestFacadeDelegate;
import ibei.test.testfacade.delegate.TestFacadeDelegateFactory;

import java.util.Calendar;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;
import junit.framework.TestCase;

public class TestAnnounce extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;
        
        private UserProfileTO user;

        public TestAnnounce(String name) {
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
                UserProfileDetailsTO details = new UserProfileDetailsTO(
                                "Name1", "Surname1", "Em@il1", "Street1",
                                "City1", "State1", new Short("15080"), "es");
                user = new UserProfileTO("login1", "PassWord1", details);

                userFacade.registerUser(user);

        }

        public void tearDown() throws Exception {

                /* Leaving the DB as it were before. */
                testFacade.removeUserProfile(user.getLogin());

        }
        
        public void testAnnounceProduct() throws Exception {

                /* Create product. */
                ProductDetailsTO details = new ProductDetailsTO(Calendar
                                .getInstance(), new Double("10.95"),
                                "Shipping Info", "Root", user.getLogin(), null, null);
                ProductTO originalProduct = new ProductTO(new Long("-1"),
                                "Name", "Description", Calendar.getInstance(), new Double("10.95"),
                                details);

                try {
                        /* Announce product. */
                        ProductTO announcedProduct = userFacade
                                        .announce(originalProduct);

                        /* Remove product. */
                        testFacade.removeProduct(announcedProduct
                                        .getProductID());

                        /* Set the productID. */
                        originalProduct.setProductID(announcedProduct
                                        .getProductID());

                        /* Comparation. */
                        assertEquals(originalProduct, announcedProduct);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestAnnounce.class);
        }

}
