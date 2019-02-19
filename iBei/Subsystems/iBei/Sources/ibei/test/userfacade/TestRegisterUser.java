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

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;
import junit.framework.TestCase;

public class TestRegisterUser extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        public TestRegisterUser(String name) {
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
        }
        
        public void testRegisterNormalUser() throws Exception {

                /* Create user. */
                UserProfileDetailsTO details = new UserProfileDetailsTO("Name",
                                "Surname", "Em@il", "Street", "City", "State",
                                new Short("15080"), "es");
                UserProfileTO originalUser = new UserProfileTO("login",
                                "PassWord", details);

                try {
                        /* Register user. */
                        UserProfileTO registeredUser = userFacade
                                        .registerUser(originalUser);

                        /* Remove user. */
                        testFacade.removeUserProfile(registeredUser.getLogin());

                        /* Comparation. */
                        assertEquals(originalUser, registeredUser);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testRegisterSeller() throws Exception {

                /* Create user. */
                UserProfileDetailsTO details = new UserProfileDetailsTO("Name",
                                "Surname", "Em@il", "Street", "City", "State",
                                new Short("15080"), "es");
                SellerProfileTO originalSeller = new SellerProfileTO("login",
                                "PassWord", details, "Card Number", Calendar
                                                .getInstance());

                try {
                        /* Register user. */
                        SellerProfileTO registeredSeller = (SellerProfileTO) userFacade
                                        .registerUser(originalSeller);

                        /* Remove user. */
                        testFacade.removeUserProfile(registeredSeller
                                        .getLogin());

                        /* Comparation. */
                        assertEquals(originalSeller, registeredSeller);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testRegisterRepeatedNormalUser() throws Exception {

                /* Inicialization of DB. */
                UserProfileDetailsTO details1 = new UserProfileDetailsTO(
                                "Name1", "Surname1", "Em@il1", "Street1",
                                "City1", "State1", new Short("15080"), "es");
                UserProfileTO user1 = new UserProfileTO("login", "PassWord1",
                                details1);

                try {
                        userFacade.registerUser(user1);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

                /* Create user. */
                UserProfileDetailsTO details2 = new UserProfileDetailsTO(
                                "Name2", "Surname2", "Em@il2", "Street2",
                                "City2", "State2", new Short("15080"), "es");
                UserProfileTO user2 = new UserProfileTO("login", "PassWord2",
                                details2);

                try {
                        /* Register user. */
                        userFacade.registerUser(user2);

                        /* Remove user. */
                        testFacade.removeUserProfile(user2.getLogin());

                        fail("A repeated user has been registered");
                } catch (DuplicateInstanceException e) {

                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                } finally {
                        try {
                                testFacade.removeUserProfile(user1.getLogin());
                        } catch (Exception e) {
                                e.printStackTrace();
                                throw e;
                        }
                }

        }

        public void testRegisterRepeatedSeller() throws Exception {

                /* Inicialization of DB. */
                UserProfileDetailsTO details1 = new UserProfileDetailsTO(
                                "Name1", "Surname1", "Em@il1", "Street1",
                                "City1", "State1", new Short("15080"), "es");
                SellerProfileTO seller1 = new SellerProfileTO("login",
                                "PassWord1", details1, "cardNumber1", Calendar
                                                .getInstance());

                try {
                        userFacade.registerUser(seller1);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

                /* Create seller. */
                UserProfileDetailsTO details2 = new UserProfileDetailsTO(
                                "Name2", "Surname2", "Em@il2", "Street2",
                                "City2", "State2", new Short("15080"), "es");
                SellerProfileTO seller2 = new SellerProfileTO("login",
                                "PassWord2", details2, "Card Number2", Calendar
                                                .getInstance());

                try {
                        /* Register seller. */
                        userFacade.registerUser(seller2);

                        /* Remove seller. */
                        testFacade.removeUserProfile(seller2.getLogin());

                        fail("A repeated seller has been registered");
                } catch (DuplicateInstanceException e) {

                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                } finally {
                        try {
                                testFacade
                                                .removeUserProfile(seller1
                                                                .getLogin());
                        } catch (Exception e) {
                                e.printStackTrace();
                                throw e;
                        }
                }

        }

        public void testRegisterRepeatedUser() throws Exception {

                /* Inicialization of DB. */
                UserProfileDetailsTO details1 = new UserProfileDetailsTO(
                                "Name1", "Surname1", "Em@il1", "Street1",
                                "City1", "State1", new Short("15080"), "es");
                SellerProfileTO seller = new SellerProfileTO("login",
                                "PassWord1", details1, "cardNumber1", Calendar
                                                .getInstance());

                try {
                        userFacade.registerUser(seller);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

                /* Create user. */
                UserProfileDetailsTO details2 = new UserProfileDetailsTO(
                                "Name2", "Surname2", "Em@il2", "Street2",
                                "City2", "State2", new Short("15080"), "es");
                UserProfileTO user = new UserProfileTO("login", "PassWord2",
                                details2);

                try {
                        /* Register user. */
                        userFacade.registerUser(user);

                        /* Remove user. */
                        testFacade.removeUserProfile(user.getLogin());

                        fail("There are an user and a seller with the same login");
                } catch (DuplicateInstanceException e) {

                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                } finally {
                        try {
                                testFacade.removeUserProfile(seller.getLogin());
                        } catch (Exception e) {
                                e.printStackTrace();
                                throw e;
                        }
                }

        }

        public void testUpgradeToSeller() throws Exception {

                /* Inicialization of DB. */
                UserProfileDetailsTO details1 = new UserProfileDetailsTO(
                                "Name1", "Surname1", "Em@il1", "Street1",
                                "City1", "State1", new Short("15080"), "es");
                UserProfileTO user = new UserProfileTO("login", "PassWord",
                                details1);

                try {
                        userFacade.registerUser(user);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

                /* Create seller. */
                UserProfileDetailsTO details2 = new UserProfileDetailsTO(
                                "Name2", "Surname2", "Em@il2", "Street2",
                                "City2", "State2", new Short("15080"), "es");
                SellerProfileTO seller = new SellerProfileTO("login",
                                "PassWord", details2, "Card Number2", Calendar
                                                .getInstance());

                try {
                        /* Register seller. */
                        userFacade.registerUser(seller);

                        /* Remove seller. */
                        testFacade.removeUserProfile(seller.getLogin());
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestRegisterUser.class);
        }

}
