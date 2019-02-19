package ubet.test.userfacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;
import ubet.model.account.to.AccountTO;
import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.adminfacade.delegate.AdminFacadeDelegateFactory;
import ubet.model.bet.to.BetTO;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.delegate.UserFacadeDelegateFactory;
import ubet.model.userfacade.exceptions.BetOutOfTimeException;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestBet extends TestCase {

        private TestFacadeDelegate testFacade;

        private AdminFacadeDelegate adminFacade;

        private SearchFacadeDelegate searchFacade;

        private UserFacadeDelegate userFacade;

        private UserProfileTO user;

        private AccountTO account;

        private List<BetOptionTO> options;

        private BetOptionTO betOption1;

        private BetOptionTO betOption2;

        private BetOptionTO betOption3;

        private EventTO event;

        private BetTypeTO betType;

        public TestBet(String name) {
                super(name);
                try {
                        this.testFacade = TestFacadeDelegateFactory
                                        .getDelegate();
                        this.adminFacade = AdminFacadeDelegateFactory
                                        .getDelegate();
                        this.searchFacade = SearchFacadeDelegateFactory
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

                account = userFacade.registerUser(user, "creditCardNumber",
                                date, new Double("12.00"));

                betOption1 = new BetOptionTO(new Long("-1"), "Deportivo",
                                new Double("1.05"), new Long("-1"),
                                BetOptionTO.PENDING);
                betOption2 = new BetOptionTO(new Long("-1"), "X", new Double(
                                "2.15"), new Long("-1"), BetOptionTO.PENDING);
                betOption3 = new BetOptionTO(new Long("-1"), "Real Madrid",
                                new Double("10.53"), new Long("-1"),
                                BetOptionTO.PENDING);

                options = new ArrayList<BetOptionTO>();
                options.add(betOption1);
                options.add(betOption2);
                options.add(betOption3);

                betType = new BetTypeTO(new Long("-1"), new Long("-1"),
                                new Long("1"));

                event = new EventTO(new Long("-1"), "Deportivo - Real Madrid",
                                Calendar.getInstance(), "Root", new Long("-1"),
                                EventTO.NO, Calendar.getInstance());

                event = adminFacade.insertEvent(event, betType, options);

                betType.setBetTypeID(event.getBetTypeID());

                options = searchFacade.findBetOptions(betType.getBetTypeID(),
                                -1, -1);
                betOption1 = options.get(0);
                betOption2 = options.get(1);
                betOption3 = options.get(2);
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeEvent(event.getEventID());
                testFacade.removeAccount(account.getAccountID());
                testFacade.removeUser(user.getLogin());
        }

        public void testBetOutOfTime() throws Exception {
                /* Create bet. */
                Calendar date = event.getDate();
                date.add(Calendar.HOUR_OF_DAY, 1);

                BetTO originalBet = new BetTO(new Long("-1"), betType
                                .getBetTypeID(), betOption1.getBetOptionID(),
                                account.getAccountID(), event.getEventID(),
                                new Double("1.35"), date, BetTO.PENDING);

                try {
                        /* Login user. */
                        userFacade.login(user.getLogin(), user.getPassword(),
                                        false);

                        /* Bet. */
                        BetTO bet = userFacade.bet(originalBet);

                        /* Remove bet. */
                        testFacade.removeBet(bet.getBetID());

                        fail("A bet was made when the bet time was over");
                } catch (BetOutOfTimeException e) {
                        /* This is the correct way to exit this test. */
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testBetWithInsufficientBalance() throws Exception {
                /* Create bet. */
                Calendar date = event.getDate();
                date.add(Calendar.HOUR_OF_DAY, -5);

                BetTO originalBet = new BetTO(new Long("-1"), betType
                                .getBetTypeID(), betOption1.getBetOptionID(),
                                account.getAccountID(), event.getEventID(),
                                account.getBalance() + 1, date, BetTO.PENDING);

                try {
                        /* Login user. */
                        userFacade.login(user.getLogin(), user.getPassword(),
                                        false);

                        /* Bet. */
                        BetTO bet = userFacade.bet(originalBet);

                        /* Remove bet. */
                        testFacade.removeBet(bet.getBetID());

                        fail("A bet was made when there is insufficient balance");
                } catch (InsufficientBalanceException e) {
                        /* This is the correct way to exit this test. */
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testBet() throws Exception {
                /* Create bet. */
                Calendar date = event.getDate();
                date.add(Calendar.HOUR_OF_DAY, -5);

                BetTO originalBet = new BetTO(new Long("-1"), betType
                                .getBetTypeID(), betOption1.getBetOptionID(),
                                account.getAccountID(), event.getEventID(),
                                new Double("1.35"), date, BetTO.PENDING);

                try {
                        /* Login user. */
                        userFacade.login(user.getLogin(), user.getPassword(),
                                        false);

                        /* Bet. */
                        BetTO bet = userFacade.bet(originalBet);

                        /* Remove bet. */
                        testFacade.removeBet(bet.getBetID());

                        /* Set the betID. */
                        originalBet.setBetID(bet.getBetID());

                        /* Comparation. */
                        assertEquals(originalBet, bet);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestBet.class);
        }

}
