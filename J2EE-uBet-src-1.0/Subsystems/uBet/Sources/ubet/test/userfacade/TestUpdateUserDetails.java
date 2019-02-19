package ubet.test.userfacade;

import java.util.Calendar;

import junit.framework.TestCase;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.delegate.UserFacadeDelegateFactory;
import ubet.model.userfacade.util.PasswordEncrypter;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestUpdateUserDetails extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private UserProfileTO user1;

        private UserProfileTO user2;

        public TestUpdateUserDetails(String name) {
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

        @Override
        public void setUp() throws Exception {
                try {
                        DataSourceLocator
                                        .getDataSource(GlobalNames.UBET_DATA_SOURCE);
                } catch (InternalErrorException e) {
                        /* Add the datasource. */
                        DataSourceLocator.addDataSource(
                                        GlobalNames.UBET_DATA_SOURCE,
                                        new SimpleDataSource());
                }

                /* Inicialization of DB. */
                Calendar date = Calendar.getInstance();
                date.add(Calendar.YEAR, 1);

                UserProfileDetailsTO details1 = new UserProfileDetailsTO(
                                "Name1", "Surname1", "em@il.com", "ES");
                user1 = new UserProfileTO("Login1", "PassWord1", details1);
                date.add(Calendar.YEAR, 2);
                UserProfileDetailsTO details2 = new UserProfileDetailsTO(
                                "Name2", "Surname2", "em@il.com", "ES");
                user2 = new UserProfileTO("Login2", "PassWord2", details2);

                userFacade.registerUser(user1, "creditCardNumber1", date,
                                new Double("12.00"));
                userFacade.registerUser(user2, "creditCardNumber2", date,
                                new Double("53.10"));
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeUser(user1.getLogin());
                testFacade.removeUser(user2.getLogin());
        }

        public void testUpdateUserProfileDetails() throws Exception {
                /* Change some values. */
                String oldLogin = user1.getLogin();
                /* Only for the plain version of uBet. */
                // user1.setLogin("AnotherLogin");
                user1.getUserDetails().setFirstName("AnotherName1");
                user1.getUserDetails().setSurname("AnotherSurname1");
                try {
                        /* Login user. */
                        userFacade.login(oldLogin, user1.getPassword(), false);

                        /* Set the encrypted password. */
                        user1.setPassword(PasswordEncrypter.crypt(user1
                                        .getPassword()));

                        /* Update user. */
                        userFacade.updateUserDetails(user1);

                        /* Find the updated user. */
                        UserProfileTO updatedUser = userFacade.findUser();

                        /* Comparation. */
                        assertEquals(user1, updatedUser);
                } catch (Exception e) {
                        /*
                         * Restore the old login because there was no update in
                         * the database.
                         */
                        user1.setLogin(oldLogin);
                        e.printStackTrace();
                        throw e;
                }
        }

        /* Only for the plain version of uBet. */
        // public void testIncorrectLoginUpdate() throws Exception {
        // /* Change login. */
        // String oldLogin = user1.getLogin();
        // user1.setLogin(user2.getLogin());
        // try {
        // /* Login user. */
        // userFacade.login(oldLogin, user1.getPassword(), false);
        //
        // /* Update user. */
        // userFacade.updateUserDetails(user1);
        //
        // fail("An incorrect login update has been made");
        // } catch (DuplicateInstanceException e) {
        // /* This is the correct way to exit this test. */
        // /* Restore the old login because there was no login update in the
        // database. */
        // user1.setLogin(oldLogin);
        // } catch (Exception e) {
        // /* Restore the old login because there was no login update in the
        // database. */
        // user1.setLogin(oldLogin);
        // e.printStackTrace();
        // throw e;
        // }
        // }
        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestUpdateUserDetails.class);
        }

}
