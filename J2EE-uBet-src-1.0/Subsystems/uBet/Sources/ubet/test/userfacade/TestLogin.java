package ubet.test.userfacade;

import java.util.Calendar;

import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.delegate.UserFacadeDelegateFactory;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.to.LoginResultTO;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;
import junit.framework.TestCase;

public class TestLogin extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private UserProfileTO user;

        public TestLogin(String name) {
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
                UserProfileDetailsTO details = new UserProfileDetailsTO("Name",
                                "Surname", "em@il.com", "ES");
                user = new UserProfileTO("login", "PassWord", details);

                Calendar date = Calendar.getInstance();
                date.add(Calendar.YEAR, 1);

                userFacade.registerUser(user, "creditCardNumber", date,
                                new Double("12.00"));
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeUser(user.getLogin());
        }

        public void testCorrectUserLogin() throws Exception {
                try {
                        /* Login user. */
                        LoginResultTO loggedUser = userFacade.login(user
                                        .getLogin(), user.getPassword(), false);

                        /* Set the encrypted password. */
                        user.setPassword("Igs111EZ0hk");

                        /* Create the correct login result. */
                        LoginResultTO originalUser = new LoginResultTO(user
                                        .getLogin(), user.getPassword(), user
                                        .getUserDetails().getCountryID());

                        /* Comparation. */
                        assertEquals(loggedUser, originalUser);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testIncorrectUserLogin() throws Exception {
                try {
                        /* Login user. */
                        userFacade.login(user.getLogin(), "IncorrectPassWord",
                                        false);

                        fail("A user has logged with an incorrect password");
                } catch (IncorrectPasswordException e) {
                        /* This is the correct way to exit this test. */
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testNonExistentUserLogin() throws Exception {
                try {
                        /* Login user. */
                        userFacade.login("nonExistentLogin", "PassWord", false);

                        fail("A user has logged with a non-existent login");
                } catch (InstanceNotFoundException e) {
                        /* This is the correct way to exit this test. */
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestLogin.class);
        }

}
