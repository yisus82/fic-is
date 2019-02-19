package ubet.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import ubet.test.adminfacade.TestInsertBetType;
import ubet.test.adminfacade.TestInsertEvent;
import ubet.test.adminfacade.TestPublishResults;
import ubet.test.searchfacade.TestFindBetTypesByEvent;
import ubet.test.searchfacade.TestFindEventsByCategory;
import ubet.test.userfacade.TestAddToAccount;
import ubet.test.userfacade.TestBet;
import ubet.test.userfacade.TestChangePassword;
import ubet.test.userfacade.TestCreateAccount;
import ubet.test.userfacade.TestFindAccount;
import ubet.test.userfacade.TestFindAccountsByUser;
import ubet.test.userfacade.TestFindBetsByAccount;
import ubet.test.userfacade.TestFindUser;
import ubet.test.userfacade.TestLogin;
import ubet.test.userfacade.TestRegisterUser;
import ubet.test.userfacade.TestUpdateAccountDetails;
import ubet.test.userfacade.TestUpdateUserDetails;
import ubet.test.userfacade.TestWithdrawFromAccount;

public class MainTest extends TestCase {

        public static TestSuite suite() {
                TestSuite main = new TestSuite("Main");
                TestSuite admin = new TestSuite("Admin facade");
                TestSuite search = new TestSuite("Search facade");
                TestSuite user = new TestSuite("User facade");
                admin.addTestSuite(TestInsertBetType.class);
                admin.addTestSuite(TestInsertEvent.class);
                admin.addTestSuite(TestPublishResults.class);
                search.addTestSuite(TestFindBetTypesByEvent.class);
                search.addTestSuite(TestFindEventsByCategory.class);
                user.addTestSuite(TestAddToAccount.class);
                user.addTestSuite(TestBet.class);
                user.addTestSuite(TestChangePassword.class);
                user.addTestSuite(TestCreateAccount.class);
                user.addTestSuite(TestFindAccount.class);
                user.addTestSuite(TestFindAccountsByUser.class);
                user.addTestSuite(TestFindBetsByAccount.class);
                user.addTestSuite(TestFindUser.class);
                user.addTestSuite(TestLogin.class);
                user.addTestSuite(TestRegisterUser.class);
                user.addTestSuite(TestUpdateAccountDetails.class);
                user.addTestSuite(TestUpdateUserDetails.class);
                user.addTestSuite(TestWithdrawFromAccount.class);
                main.addTest(admin);
                main.addTest(search);
                main.addTest(user);
                return main;
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(MainTest.suite());
        }

}
