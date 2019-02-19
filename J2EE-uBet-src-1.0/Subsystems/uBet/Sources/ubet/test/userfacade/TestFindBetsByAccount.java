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
import ubet.model.userfacade.to.BetChunkTO;
import ubet.model.userfacade.to.BetStatusTO;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestFindBetsByAccount extends TestCase {

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

        private BetTO bet1;

        private BetTO bet2;

        private BetTO bet3;

        public TestFindBetsByAccount(String name) {
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

                account = userFacade.registerUser(user, "creditCardNumber1",
                                date, new Double("12.00"));

                betOption1 = new BetOptionTO(new Long("-1"), "Deportivo",
                                new Double("1.05"), new Long("1"),
                                BetOptionTO.PENDING);
                betOption2 = new BetOptionTO(new Long("-1"), "X", new Double(
                                "2.15"), new Long("1"), BetOptionTO.PENDING);
                betOption3 = new BetOptionTO(new Long("-1"), "Real Madrid",
                                new Double("10.53"), new Long("1"),
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

                date = Calendar.getInstance();
                date.add(Calendar.HOUR_OF_DAY, -3);

                bet1 = new BetTO(new Long("-1"), betType.getBetTypeID(),
                                betOption1.getBetOptionID(), account
                                                .getAccountID(), event
                                                .getEventID(), new Double(
                                                "1.35"), date, BetTO.PENDING);

                date.add(Calendar.HOUR_OF_DAY, -2);

                bet2 = new BetTO(new Long("-1"), betType.getBetTypeID(),
                                betOption2.getBetOptionID(), account
                                                .getAccountID(), event
                                                .getEventID(), new Double(
                                                "2.35"), date, BetTO.PENDING);

                date.add(Calendar.HOUR_OF_DAY, -1);

                bet3 = new BetTO(new Long("-1"), betType.getBetTypeID(),
                                betOption3.getBetOptionID(), account
                                                .getAccountID(), event
                                                .getEventID(), new Double(
                                                "3.85"), date, BetTO.PENDING);

                bet1 = userFacade.bet(bet1);
                bet2 = userFacade.bet(bet2);
                bet3 = userFacade.bet(bet3);
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeBet(bet1.getBetID());
                testFacade.removeBet(bet2.getBetID());
                testFacade.removeBet(bet3.getBetID());
                testFacade.removeEvent(event.getEventID());
                testFacade.removeAccount(account.getAccountID());
                testFacade.removeUser(user.getLogin());
        }

        public void testFindAllBets() throws Exception {
                try {
                        /* Find bets. */
                        BetChunkTO results = userFacade.findBetsByAccount(
                                        account.getAccountID(), -1, -1, null,
                                        null);

                        List<BetStatusTO> bets = results.getBetStatusTOs();

                        if (bets.size() != 3)
                                fail("Incorrect number of bets found (Expected = 3 Found = "
                                                + bets.size() + ")");

                        /* Comparations. */

                        /* Create the correct bet status. */
                        BetStatusTO originalBetStatus = new BetStatusTO(bet1
                                        .getBetID(), bet1.getDate(), event
                                        .getDescription(), event.getDate(),
                                        "1X2", betOption1.getDescription(),
                                        betOption1.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        BetStatusTO betFound = bets.get(0);
                        assertEquals(originalBetStatus, betFound);

                        /* Create the correct bet status. */
                        originalBetStatus = new BetStatusTO(bet2.getBetID(),
                                        bet2.getDate(), event.getDescription(),
                                        event.getDate(), "1X2", betOption2
                                                        .getDescription(),
                                        betOption2.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        betFound = bets.get(1);
                        assertEquals(originalBetStatus, betFound);

                        /* Create the correct bet status. */
                        originalBetStatus = new BetStatusTO(bet3.getBetID(),
                                        bet3.getDate(), event.getDescription(),
                                        event.getDate(), "1X2", betOption3
                                                        .getDescription(),
                                        betOption3.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        betFound = bets.get(2);
                        assertEquals(originalBetStatus, betFound);

                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testFindBetsPageByPage() throws Exception {
                try {
                        /* Find bets. */
                        BetChunkTO results = userFacade.findBetsByAccount(
                                        account.getAccountID(), 1, 2, null,
                                        null);

                        List<BetStatusTO> bets = results.getBetStatusTOs();

                        if (bets.size() != 2)
                                fail("Incorrect number of bets found (Expected = 2 Found = "
                                                + bets.size() + ")");

                        /* Comparations. */

                        /* Create the correct bet status. */
                        BetStatusTO originalBetStatus = new BetStatusTO(bet1
                                        .getBetID(), bet1.getDate(), event
                                        .getDescription(), event.getDate(),
                                        "1X2", betOption1.getDescription(),
                                        betOption1.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        BetStatusTO betFound = bets.get(0);
                        assertEquals(originalBetStatus, betFound);

                        /* Create the correct bet status. */
                        originalBetStatus = new BetStatusTO(bet2.getBetID(),
                                        bet2.getDate(), event.getDescription(),
                                        event.getDate(), "1X2", betOption2
                                                        .getDescription(),
                                        betOption2.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        betFound = bets.get(1);
                        assertEquals(originalBetStatus, betFound);

                        /* Page 2. */
                        results = userFacade.findBetsByAccount(account
                                        .getAccountID(), 3, 2, null, null);

                        bets = results.getBetStatusTOs();

                        if (bets.size() != 1)
                                fail("Incorrect number of bets found (Expected = 2 Found = "
                                                + bets.size() + ")");

                        /* Comparation. */

                        /* Create the correct bet status. */
                        originalBetStatus = new BetStatusTO(bet3.getBetID(),
                                        bet3.getDate(), event.getDescription(),
                                        event.getDate(), "1X2", betOption3
                                                        .getDescription(),
                                        betOption3.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        betFound = bets.get(0);
                        assertEquals(originalBetStatus, betFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindAllBetsByDate() throws Exception {
                try {
                        /* Find bets. */
                        BetChunkTO results = userFacade.findBetsByAccount(
                                        account.getAccountID(), -1, -1, bet2
                                                        .getDate(), Calendar
                                                        .getInstance());

                        List<BetStatusTO> bets = results.getBetStatusTOs();

                        if (bets.size() != 2)
                                fail("Incorrect number of bets found (Expected = 2 Found = "
                                                + bets.size() + ")");

                        /* Comparations. */

                        /* Create the correct bet status. */
                        BetStatusTO originalBetStatus = new BetStatusTO(bet1
                                        .getBetID(), bet1.getDate(), event
                                        .getDescription(), event.getDate(),
                                        "1X2", betOption1.getDescription(),
                                        betOption1.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        BetStatusTO betFound = bets.get(0);
                        assertEquals(originalBetStatus, betFound);

                        /* Create the correct bet status. */
                        originalBetStatus = new BetStatusTO(bet2.getBetID(),
                                        bet2.getDate(), event.getDescription(),
                                        event.getDate(), "1X2", betOption2
                                                        .getDescription(),
                                        betOption2.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        betFound = bets.get(1);
                        assertEquals(originalBetStatus, betFound);

                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testFindBetsPageByPageByDate() throws Exception {
                try {
                        /* Find bets. */
                        BetChunkTO results = userFacade.findBetsByAccount(
                                        account.getAccountID(), 1, 1, bet2
                                                        .getDate(), Calendar
                                                        .getInstance());

                        List<BetStatusTO> bets = results.getBetStatusTOs();

                        if (bets.size() != 1)
                                fail("Incorrect number of bets found (Expected = 1 Found = "
                                                + bets.size() + ")");

                        /* Comparations. */

                        /* Create the correct bet status. */
                        BetStatusTO originalBetStatus = new BetStatusTO(bet1
                                        .getBetID(), bet1.getDate(), event
                                        .getDescription(), event.getDate(),
                                        "1X2", betOption1.getDescription(),
                                        betOption1.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        BetStatusTO betFound = bets.get(0);
                        assertEquals(originalBetStatus, betFound);

                        /* Page 2. */
                        results = userFacade.findBetsByAccount(account
                                        .getAccountID(), 2, 1, bet2.getDate(),
                                        Calendar.getInstance());

                        bets = results.getBetStatusTOs();

                        if (bets.size() != 1)
                                fail("Incorrect number of bets found (Expected = 1 Found = "
                                                + bets.size() + ")");

                        /* Comparation. */

                        /* Create the correct bet status. */
                        originalBetStatus = new BetStatusTO(bet2.getBetID(),
                                        bet2.getDate(), event.getDescription(),
                                        event.getDate(), "1X2", betOption2
                                                        .getDescription(),
                                        betOption2.getOdds(),
                                        new ArrayList<String>(), BetTO.PENDING);

                        betFound = bets.get(0);
                        assertEquals(originalBetStatus, betFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public void testFindBetsByAccountWithNoBets() throws Exception {
                try {
                        /* Find bets. */
                        BetChunkTO results = userFacade.findBetsByAccount(
                                        new Long("-2"), -1, -1, null, null);

                        if (!results.getBetStatusTOs().isEmpty())
                                fail("Bets were found where there should be none");
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestFindBetsByAccount.class);
        }

}
