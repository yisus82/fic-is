package ibei.test;

import ibei.test.productfacade.TestFindBidsByProduct;
import ibei.test.productfacade.TestFindProduct;
import ibei.test.productfacade.TestFindProductsByCategory;
import ibei.test.productfacade.TestFindProductsByKeywords;
import ibei.test.userfacade.TestAnnounce;
import ibei.test.userfacade.TestBid;
import ibei.test.userfacade.TestFindBidsByUser;
import ibei.test.userfacade.TestFindProductsByUser;
import ibei.test.userfacade.TestFindUserProfile;
import ibei.test.userfacade.TestLogin;
import ibei.test.userfacade.TestRegisterUser;
import ibei.test.userfacade.TestUpdateUserProfileDetails;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MainTest extends TestCase {

        public static TestSuite suite() {

                TestSuite main = new TestSuite("Main");
                TestSuite user = new TestSuite("User facade");
                TestSuite product = new TestSuite("Product facade");
                user.addTestSuite(TestAnnounce.class);
                user.addTestSuite(TestBid.class);
                user.addTestSuite(TestFindBidsByUser.class);
                user.addTestSuite(TestFindProductsByUser.class);
                user.addTestSuite(TestFindUserProfile.class);
                user.addTestSuite(TestLogin.class);
                user.addTestSuite(TestRegisterUser.class);
                user.addTestSuite(TestUpdateUserProfileDetails.class);
                product.addTestSuite(TestFindBidsByProduct.class);
                product.addTestSuite(TestFindProduct.class);
                product.addTestSuite(TestFindProductsByCategory.class);
                product.addTestSuite(TestFindProductsByKeywords.class);
                main.addTest(user);
                main.addTest(product);
                return main;

        }

        public static void main(String[] args) {

                junit.textui.TestRunner.run(MainTest.suite());

        }

}
