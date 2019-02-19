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

public class TestUpdateUserProfileDetails extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private UserProfileTO user;

        private SellerProfileTO seller;

        public TestUpdateUserProfileDetails(String name) {
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

        public void testUpdateNormalUserProfileDetails() throws Exception {

                /* Change some values. */
                String oldLogin = user.getLogin();
                user.setLogin("AnotherLogin1");
                user.getUserProfileDetails().setFirstName("AnotherName1");
                user.getUserProfileDetails().setStreet("AnotherStreet1");
                try {
                        /* Login user. */
                        userFacade.login(oldLogin, user.getPassword(), true);

                        /* Update user. */
                        UserProfileTO updatedUser = userFacade
                                        .updateUserProfileDetails(user);

                        /* Comparation. */
                        assertEquals(updatedUser, user);
                } catch (Exception e) {
                        /* Restore the old login because there was no update in the database. */
                        user.setLogin(oldLogin);
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testUpdateSellerProfileDetails() throws Exception {

                /* Change some values. */
                String oldLogin = seller.getLogin();
                seller.setLogin("AnotherLogin2");
                seller.getUserProfileDetails().setFirstName("AnotherName2");
                Calendar date = Calendar.getInstance();
                date.add(Calendar.YEAR, 2);
                seller.setExpirationDate(date);
                try {
                        /* Login seller. */
                        userFacade.login(oldLogin, seller.getPassword(), true);

                        /* Update seller. */
                        SellerProfileTO updatedSeller = (SellerProfileTO) userFacade
                                        .updateUserProfileDetails(seller);

                        /* Comparation. */
                        assertEquals(updatedSeller, seller);
                } catch (Exception e) {
                        /* Restore the old login because there was no update in the database. */
                        seller.setLogin(oldLogin);
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testIncorrectLoginUpdate() throws Exception {

                /* Change login. */
                String oldLogin = user.getLogin();
                user.setLogin(seller.getLogin());
                try {
                        /* Login user. */
                        userFacade.login(oldLogin, user.getPassword(), true);

                        /* Update user. */
                        userFacade.updateUserProfileDetails(user);

                        fail("An incorrect login update has been made");
                } catch (DuplicateInstanceException e) {
                        /* This is the correct way to exit this test. */
                        /* Restore the old login because there was no login update in the database. */
                        user.setLogin(oldLogin);
                } catch (Exception e) {
                        /* Restore the old login because there was no login update in the database. */
                        user.setLogin(oldLogin);
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestUpdateUserProfileDetails.class);
        }

}
