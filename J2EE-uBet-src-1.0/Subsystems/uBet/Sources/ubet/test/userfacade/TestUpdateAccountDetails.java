package ubet.test.userfacade;

import java.util.Calendar;

import junit.framework.TestCase;
import ubet.model.account.to.AccountTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.delegate.UserFacadeDelegateFactory;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestUpdateAccountDetails extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private UserProfileTO user;

        private AccountTO account;

        public TestUpdateAccountDetails(String name) {
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

                UserProfileDetailsTO details = new UserProfileDetailsTO("Name",
                                "Surname", "em@il.com", "ES");
                user = new UserProfileTO("Login", "PassWord", details);
                date.add(Calendar.YEAR, 2);

                account = userFacade.registerUser(user, "creditCardNumber1",
                                date, new Double("12.00"));
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeUser("Login");
        }

        public void testUpdateAccountDetails() throws Exception {
                /* Change some values. */
                account.setCreditCardNumber("creditCardNumber2");
                try {
                        /* Login user. */
                        userFacade.login(user.getLogin(), user.getPassword(),
                                        false);

                        /* Update account. */
                        userFacade.updateAccountDetails(account);

                        /* Find the updated account. */
                        AccountTO updatedAccount = userFacade
                                        .findAccount(account.getAccountID());

                        /* Comparation. */
                        assertEquals(account, updatedAccount);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestUpdateAccountDetails.class);
        }

}
