package ibei.test.userfacade;

import ibei.model.sellerprofile.to.SellerProfileTO;
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

public class TestFindUserProfile extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private UserProfileTO user;

        private SellerProfileTO seller;

        public TestFindUserProfile(String name) {
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
                                "City1", "State1", new Short("15080"), "es");
                user = new UserProfileTO("login1", "PassWord1", details1);

                UserProfileDetailsTO details2 = new UserProfileDetailsTO(
                                "Name2", "Surname2", "Em@il2", "Street2",
                                "City2", "State2", new Short("15080"), "es");
                seller = new SellerProfileTO("login2", "PassWord2", details2,
                                "CardNumber", Calendar.getInstance());

                userFacade.registerUser(user);
                userFacade.registerUser(seller);

        }

        public void tearDown() throws Exception {

                /* Leaving the DB as it were before. */
                testFacade.removeUserProfile(user.getLogin());
                testFacade.removeUserProfile(seller.getLogin());

        }

        public void testFindNormalUserProfile() throws Exception {

                try {
                        /* Find user. */
                        UserProfileTO userFound = userFacade
                                        .findUserProfile(user.getLogin());

                        /* Comparation. */
                        assertEquals(userFound, user);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindSellerProfile() throws Exception {

                try {
                        /* Find user. */
                        UserProfileTO userFound = userFacade
                                        .findUserProfile(seller.getLogin());
                        
                        /* Comparation. */
                        assertEquals(userFound, seller);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindNonExistentUserProfile() throws Exception {

                try {
                        /* Find user. */
                        userFacade.findUserProfile("nonExistentLogin");

                        fail("A non-existent user profile was found");
                } catch (InstanceNotFoundException e) {
                        /* This is the correct way to exit this test. */
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestFindUserProfile.class);
        }

}
