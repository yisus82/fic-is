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

public class TestCreateAccount extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private UserProfileTO user;

        public TestCreateAccount(String name) {
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
                this.user = new UserProfileTO("login", "PassWord", details);

                Calendar date = Calendar.getInstance();
                date.add(Calendar.YEAR, 1);

                userFacade.registerUser(user, "creditCardNumber1", date,
                                new Double("12.00"));
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeUser(user.getLogin());
        }

        public void testCreateAccount() throws Exception {
                /* Create account. */
                Calendar date = Calendar.getInstance();
                date.add(Calendar.YEAR, 2);

                AccountTO originalAccount = new AccountTO(new Long("-1"),
                                "login", "creditCardNumber2", date, new Double(
                                                "123.45"));

                try {
                        /* Login user. */
                        userFacade.login(user.getLogin(), user.getPassword(),
                                        false);

                        /* Create account. */
                        AccountTO createdAccount = userFacade
                                        .createAccount(originalAccount);

                        /* Remove account. */
                        testFacade.removeAccount(createdAccount.getAccountID());

                        /* Set the accountID. */
                        originalAccount.setAccountID(createdAccount
                                        .getAccountID());

                        /* Comparation. */
                        assertEquals(originalAccount, createdAccount);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestBet.class);
        }

}
