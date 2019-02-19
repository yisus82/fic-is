package ubet.test.userfacade;

import java.util.Calendar;

import junit.framework.TestCase;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.delegate.UserFacadeDelegateFactory;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.util.PasswordEncrypter;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestChangePassword extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private UserProfileTO user;

        public TestChangePassword(String name) {
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

        public void testChangePassword() throws Exception {
                try {
                        /* Login user. */
                        userFacade.login(user.getLogin(), user.getPassword(),
                                        false);

                        /* Update user. */
                        userFacade
                                        .changePassword("PassWord",
                                                        "AnotherPassWord");

                        /* Create the correct user. */
                        UserProfileTO correctUser = new UserProfileTO(user
                                        .getLogin(), PasswordEncrypter
                                        .crypt("AnotherPassWord"), user
                                        .getUserDetails());

                        /* Find the updated user. */
                        UserProfileTO updatedUser = userFacade.findUser();

                        /* Comparation. */
                        assertEquals(correctUser, updatedUser);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testIncorrectChangePassword() throws Exception {
                try {
                        /* Login user. */
                        userFacade.login(user.getLogin(), user.getPassword(),
                                        true);

                        /* Update user. */
                        userFacade.changePassword("IncorrectPassWord",
                                        "AnotherPassWord");

                        fail("An incorrect password change has been made");
                } catch (IncorrectPasswordException e) {
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
