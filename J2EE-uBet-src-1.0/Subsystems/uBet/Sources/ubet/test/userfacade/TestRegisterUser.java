package ubet.test.userfacade;

import java.util.Calendar;

import junit.framework.TestCase;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.delegate.UserFacadeDelegateFactory;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

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
        }

        public void testRegisterUser() throws Exception {
                /* Create user. */
                UserProfileDetailsTO details = new UserProfileDetailsTO("Name",
                                "Surname", "em@il.com", "ES");
                UserProfileTO user = new UserProfileTO("login1", "PassWord1",
                                details);

                try {
                        /* Register user. */
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.YEAR, 1);

                        userFacade.registerUser(user, "creditCardNumber", date,
                                        new Double("12.00"));

                        /* Find the registered user. */
                        UserProfileTO registeredUser = userFacade.findUser();

                        /* Remove user. */
                        testFacade.removeUser(user.getLogin());

                        /* Set the encrypted password. */
                        user.setPassword("Igs111EZ0hk");

                        /* Comparation. */
                        assertEquals(user, registeredUser);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testRegisterRepeatedUser() throws Exception {
                /* Inicialization of DB. */
                UserProfileDetailsTO details1 = new UserProfileDetailsTO(
                                "Name1", "Surname1", "em@il.com", "ES");
                UserProfileTO user1 = new UserProfileTO("login", "PassWord1",
                                details1);

                try {
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.YEAR, 1);
                        userFacade.registerUser(user1, "creditCardNumber1",
                                        date, new Double("12.00"));
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

                /* Create user. */
                UserProfileDetailsTO details2 = new UserProfileDetailsTO(
                                "Name2", "Surname2", "em@il.es", "ES");
                UserProfileTO user2 = new UserProfileTO("login", "PassWord2",
                                details2);

                try {
                        /* Register user. */
                        Calendar date = Calendar.getInstance();
                        date.add(Calendar.YEAR, 2);
                        userFacade.registerUser(user2, "creditCardNumber2",
                                        date, new Double("132.50"));

                        /* Remove user. */
                        testFacade.removeUser(user2.getLogin());

                        fail("A repeated user has been registered");
                } catch (DuplicateInstanceException e) {
                        /* This is the correct way to exit this test. */
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                } finally {
                        try {
                                testFacade.removeUser(user1.getLogin());
                        } catch (Exception e) {
                                e.printStackTrace();
                                throw e;
                        }
                }
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestRegisterUser.class);
        }

}
