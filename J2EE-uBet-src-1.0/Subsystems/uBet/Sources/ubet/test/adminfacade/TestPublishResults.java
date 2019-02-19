package ubet.test.adminfacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;
import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.adminfacade.delegate.AdminFacadeDelegateFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestPublishResults extends TestCase {

        private TestFacadeDelegate testFacade;

        private AdminFacadeDelegate adminFacade;

        private SearchFacadeDelegate searchFacade;

        private List<BetOptionTO> options;

        private BetOptionTO betOption1;

        private BetOptionTO betOption2;

        private BetOptionTO betOption3;

        private BetTypeTO betType;

        private EventTO event;

        public TestPublishResults(String name) {
                super(name);

                try {
                        this.testFacade = TestFacadeDelegateFactory
                                        .getDelegate();
                        this.adminFacade = AdminFacadeDelegateFactory
                                        .getDelegate();
                        this.searchFacade = SearchFacadeDelegateFactory
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
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeEvent(event.getEventID());
        }

        public void testPublishResults() throws Exception {
                try {
                        /* Get all the options. */
                        options = searchFacade.findBetOptions(betType
                                        .getBetTypeID(), -1, -1);

                        /* Set the results. */
                        options.get(0).setStatus(BetOptionTO.WINNER);
                        options.get(1).setStatus(BetOptionTO.LOSER);
                        options.get(2).setStatus(BetOptionTO.LOSER);

                        /* Publish results. */
                        List<Long> winnerOptions = new ArrayList<Long>();
                        winnerOptions.add(options.get(0).getBetOptionID());

                        adminFacade.publishResults(winnerOptions);
                        /* Find all the winner options. */
                        List<BetOptionTO> winnerOptionTOs = searchFacade
                                        .findWinnerOptions(betType
                                                        .getBetTypeID());

                        if (winnerOptions.size() != 1)
                                fail("Incorrect number of winner bet options found (Expected = 1 Found = "
                                                + winnerOptions.size() + ")");

                        /* Comparation. */
                        assertEquals(options.get(0), winnerOptionTOs.get(0));
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestPublishResults.class);
        }

}
