package ubet.test.userfacade;

import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;
import ubet.model.account.to.AccountTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.delegate.UserFacadeDelegateFactory;
import ubet.model.userfacade.to.AccountChunkTO;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestFindAccountsByUser extends TestCase {

        private TestFacadeDelegate testFacade;

        private UserFacadeDelegate userFacade;

        private AccountTO account1;

        private AccountTO account2;

        private AccountTO account3;

        private UserProfileTO user;

        public TestFindAccountsByUser(String name) {
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
                user = new UserProfileTO("Login", "PassWord", details);

                Calendar date = Calendar.getInstance();
                date.add(Calendar.YEAR, 1);

                account1 = userFacade.registerUser(user, "creditCardNumber1",
                                date, new Double("12.00"));

                date.add(Calendar.YEAR, 2);

                account2 = new AccountTO(new Long("-1"), "Login",
                                "creditCardNumber2", date, new Double("15.50"));

                date.add(Calendar.YEAR, 3);

                account3 = new AccountTO(new Long("-1"), "Login",
                                "creditCardNumber3", date, new Double("22.37"));

                account2 = userFacade.createAccount(account2);
                account3 = userFacade.createAccount(account3);
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeAccount(account1.getAccountID());
                testFacade.removeAccount(account2.getAccountID());
                testFacade.removeAccount(account3.getAccountID());
                testFacade.removeUser(user.getLogin());
        }

        public void testFindAllAccounts() throws Exception {
                try {
                        /* Find accounts. */
                        AccountChunkTO results = userFacade.findAccountsByUser(
                                        -1, -1);

                        List<AccountTO> accounts = results.getAccountTOs();

                        if (accounts.size() != 3)
                                fail("Incorrect number of accounts found (Expected = 3 Found = "
                                                + accounts.size() + ")");

                        /* Comparations. */
                        AccountTO accountFound = accounts.get(0);
                        assertEquals(account1, accountFound);

                        accountFound = accounts.get(1);
                        assertEquals(account2, accountFound);

                        accountFound = accounts.get(2);
                        assertEquals(account3, accountFound);

                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindAccountsPageByPage() throws Exception {
                try {
                        /* Find accounts. */
                        AccountChunkTO results = userFacade.findAccountsByUser(
                                        1, 2);

                        List<AccountTO> accounts = results.getAccountTOs();

                        if (accounts.size() != 2)
                                fail("Incorrect number of accounts found (Expected = 2 Found = "
                                                + accounts.size() + ")");

                        /* Comparations. */
                        AccountTO accountFound = accounts.get(0);
                        assertEquals(account1, accountFound);

                        accountFound = accounts.get(1);
                        assertEquals(account2, accountFound);

                        /* Page 2. */
                        results = userFacade.findAccountsByUser(3, 2);

                        accounts = results.getAccountTOs();

                        if (accounts.size() != 1)
                                fail("Incorrect number of accounts found (Expected = 1 Found = "
                                                + accounts.size() + ")");

                        /* Comparation. */
                        accountFound = accounts.get(0);
                        assertEquals(account3, accountFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestFindAccountsByUser.class);
        }

}
