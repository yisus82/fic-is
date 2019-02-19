package ibei.test.userfacade;

import ibei.model.userfacade.delegate.UserFacadeDelegate;
import ibei.model.userfacade.delegate.UserFacadeDelegateFactory;
import ibei.model.userfacade.exceptions.IncorrectPasswordException;
import ibei.model.userfacade.to.LoginResultTO;
import ibei.model.userprofile.to.UserProfileDetailsTO;
import ibei.model.userprofile.to.UserProfileTO;
import ibei.model.util.GlobalNames;
import ibei.test.testfacade.delegate.TestFacadeDelegate;
import ibei.test.testfacade.delegate.TestFacadeDelegateFactory;
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

        public void testCorrectUserLogin() throws Exception {

                try {
                        /* Login user. */
                        LoginResultTO loggedUser = userFacade.login("login1",
                                        "PassWord1", false);

                        /* Create the correct LoginResultVO. */
                        LoginResultTO originalUser = new LoginResultTO(user
                                        .getLogin(), user.getPassword(), user
                                        .getType(), user
                                        .getUserProfileDetails().getCountryID());

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
                        userFacade.login("login1", "BadPassWord", false);

                        fail("A user has logged with a bad password");
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
