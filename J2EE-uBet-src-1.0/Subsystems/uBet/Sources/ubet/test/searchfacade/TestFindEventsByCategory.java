package ubet.test.searchfacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;
import ubet.http.controller.caches.CategoryCache;
import ubet.http.controller.caches.QuestionCache;
import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.adminfacade.delegate.AdminFacadeDelegateFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import ubet.model.searchfacade.to.BetTypeResultTO;
import ubet.model.searchfacade.to.CategoryChunkTO;
import ubet.model.searchfacade.to.EventChunkTO;
import ubet.model.searchfacade.to.EventResultTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestFindEventsByCategory extends TestCase {

        private TestFacadeDelegate testFacade;

        private AdminFacadeDelegate adminFacade;

        private SearchFacadeDelegate searchFacade;

        private List<BetOptionTO> options1;

        private List<BetOptionTO> options2;

        private List<BetOptionTO> options3;

        private BetOptionTO betOption1;

        private BetOptionTO betOption2;

        private BetOptionTO betOption3;

        private BetTypeTO betType1;

        private BetTypeTO betType2;

        private BetTypeTO betType3;

        private EventTO event1;

        private EventTO event2;

        private EventTO event3;

        public TestFindEventsByCategory(String name) {
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

                options1 = new ArrayList<BetOptionTO>();
                options1.add(betOption1);
                options1.add(betOption2);
                options1.add(betOption3);

                betType1 = new BetTypeTO(new Long("-1"), new Long("-1"),
                                new Long("1"));

                event1 = new EventTO(new Long("-1"), "Deportivo - Real Madrid",
                                Calendar.getInstance(), "Root", new Long("-1"),
                                EventTO.NO, Calendar.getInstance());

                event1 = adminFacade.insertEvent(event1, betType1, options1);

                betType1.setBetTypeID(event1.getBetTypeID());

                betType1.setEventID(event1.getEventID());

                options1 = searchFacade.findBetOptions(betType1.getBetTypeID(),
                                -1, -1);

                betOption1 = new BetOptionTO(new Long("-1"), "Deportivo",
                                new Double("1.05"), new Long("-1"),
                                BetOptionTO.PENDING);
                betOption2 = new BetOptionTO(new Long("-1"), "X", new Double(
                                "5.15"), new Long("-1"), BetOptionTO.PENDING);
                betOption3 = new BetOptionTO(new Long("-1"), "Celta",
                                new Double("13.53"), new Long("-1"),
                                BetOptionTO.PENDING);

                options2 = new ArrayList<BetOptionTO>();
                options2.add(betOption1);
                options2.add(betOption2);
                options2.add(betOption3);

                betType2 = new BetTypeTO(new Long("-1"), new Long("-1"),
                                new Long("1"));

                event2 = new EventTO(new Long("-1"), "Deportivo - Celta",
                                Calendar.getInstance(), "Root", new Long("-1"),
                                EventTO.NO, Calendar.getInstance());

                event2 = adminFacade.insertEvent(event2, betType2, options2);

                betType2.setBetTypeID(event2.getBetTypeID());

                betType2.setEventID(event2.getEventID());

                options2 = searchFacade.findBetOptions(betType2.getBetTypeID(),
                                -1, -1);

                betOption1 = new BetOptionTO(new Long("-1"), "Barcelona",
                                new Double("1.01"), new Long("-1"),
                                BetOptionTO.PENDING);
                betOption2 = new BetOptionTO(new Long("-1"), "X", new Double(
                                "7.25"), new Long("-1"), BetOptionTO.PENDING);
                betOption3 = new BetOptionTO(new Long("-1"), "Real Madrid",
                                new Double("12.53"), new Long("-1"),
                                BetOptionTO.PENDING);

                options3 = new ArrayList<BetOptionTO>();
                options3.add(betOption1);
                options3.add(betOption2);
                options3.add(betOption3);

                betType3 = new BetTypeTO(new Long("-1"), new Long("-1"),
                                new Long("1"));

                event3 = new EventTO(new Long("-1"), "Barcelona - Real Madrid",
                                Calendar.getInstance(), "Root", new Long("-1"),
                                EventTO.NO, Calendar.getInstance());

                event3 = adminFacade.insertEvent(event3, betType3, options3);

                betType3.setBetTypeID(event3.getBetTypeID());

                betType3.setEventID(event3.getEventID());

                options3 = searchFacade.findBetOptions(betType3.getBetTypeID(),
                                -1, -1);

        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeEvent(event1.getEventID());
                testFacade.removeEvent(event2.getEventID());
                testFacade.removeEvent(event3.getEventID());
        }

        public void testFindAllEvents() throws Exception {
                try {
                        /* Find events. */
                        EventChunkTO results = searchFacade
                                        .findEventsByCategory("Root", -1, -1,
                                                        null, null);
                        List<EventResultTO> eventTOs = results.getEventTOs();

                        if (eventTOs.size() != 3)
                                fail("Incorrect number of events found (Expected = 3 Found = "
                                                + eventTOs.size() + ")");

                        /* Comparations. */

                        /* Create the correct event result. */
                        CategoryChunkTO category = CategoryCache
                                        .getCategoryByIdentifier("Root");
                        BetTypeResultTO betTypeResult = new BetTypeResultTO(
                                        betType1.getBetTypeID(), betType1
                                                        .getEventID(),
                                        QuestionCache.getQuestion(betType1
                                                        .getQuestionID()),
                                        options1);

                        EventResultTO originalEventResult = new EventResultTO(
                                        event1.getEventID(), event1
                                                        .getDescription(),
                                        event1.getDate(), category,
                                        betTypeResult, event1.getHighlighted());
                        EventResultTO eventResultFound = eventTOs.get(0);

                        /* Comparation. */
                        assertEquals(originalEventResult, eventResultFound);

                        /* Create the correct event result. */
                        category = CategoryCache
                                        .getCategoryByIdentifier("Root");
                        betTypeResult = new BetTypeResultTO(betType2
                                        .getBetTypeID(), betType2.getEventID(),
                                        QuestionCache.getQuestion(betType2
                                                        .getQuestionID()),
                                        options2);

                        originalEventResult = new EventResultTO(event2
                                        .getEventID(), event2.getDescription(),
                                        event2.getDate(), category,
                                        betTypeResult, event2.getHighlighted());
                        eventResultFound = eventTOs.get(1);

                        /* Comparation. */
                        assertEquals(originalEventResult, eventResultFound);

                        /* Create the correct event result. */
                        category = CategoryCache
                                        .getCategoryByIdentifier("Root");
                        betTypeResult = new BetTypeResultTO(betType3
                                        .getBetTypeID(), betType3.getEventID(),
                                        QuestionCache.getQuestion(betType3
                                                        .getQuestionID()),
                                        options3);

                        originalEventResult = new EventResultTO(event3
                                        .getEventID(), event3.getDescription(),
                                        event3.getDate(), category,
                                        betTypeResult, event3.getHighlighted());
                        eventResultFound = eventTOs.get(2);

                        /* Comparation. */
                        assertEquals(originalEventResult, eventResultFound);

                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testFindEventsPageByPage() throws Exception {
                try {
                        /* Find events. */
                        EventChunkTO results = searchFacade
                                        .findEventsByCategory("Root", 1, 2,
                                                        null, null);
                        List<EventResultTO> eventTOs = results.getEventTOs();

                        if (eventTOs.size() != 2)
                                fail("Incorrect number of events found (Expected = 2 Found = "
                                                + eventTOs.size() + ")");

                        /* Comparations. */

                        /* Create the correct event result. */
                        CategoryChunkTO category = CategoryCache
                                        .getCategoryByIdentifier("Root");
                        BetTypeResultTO betTypeResult = new BetTypeResultTO(
                                        betType1.getBetTypeID(), betType1
                                                        .getEventID(),
                                        QuestionCache.getQuestion(betType1
                                                        .getQuestionID()),
                                        options1);

                        EventResultTO originalEventResult = new EventResultTO(
                                        event1.getEventID(), event1
                                                        .getDescription(),
                                        event1.getDate(), category,
                                        betTypeResult, event1.getHighlighted());
                        EventResultTO eventResultFound = eventTOs.get(0);

                        /* Comparation. */
                        assertEquals(originalEventResult, eventResultFound);

                        /* Create the correct event result. */
                        category = CategoryCache
                                        .getCategoryByIdentifier("Root");
                        betTypeResult = new BetTypeResultTO(betType2
                                        .getBetTypeID(), betType2.getEventID(),
                                        QuestionCache.getQuestion(betType2
                                                        .getQuestionID()),
                                        options2);

                        originalEventResult = new EventResultTO(event2
                                        .getEventID(), event2.getDescription(),
                                        event2.getDate(), category,
                                        betTypeResult, event2.getHighlighted());
                        eventResultFound = eventTOs.get(1);

                        /* Comparation. */
                        assertEquals(originalEventResult, eventResultFound);

                        /* Page 2. */
                        results = searchFacade.findEventsByCategory("Root", 3,
                                        2, null, null);
                        eventTOs = results.getEventTOs();

                        if (eventTOs.size() != 1)
                                fail("Incorrect number of events found (Expected = 1 Found = "
                                                + eventTOs.size() + ")");

                        /* Comparations. */

                        /* Create the correct event result. */
                        category = CategoryCache
                                        .getCategoryByIdentifier("Root");
                        betTypeResult = new BetTypeResultTO(betType3
                                        .getBetTypeID(), betType3.getEventID(),
                                        QuestionCache.getQuestion(betType3
                                                        .getQuestionID()),
                                        options3);

                        originalEventResult = new EventResultTO(event3
                                        .getEventID(), event3.getDescription(),
                                        event3.getDate(), category,
                                        betTypeResult, event3.getHighlighted());
                        eventResultFound = eventTOs.get(0);

                        /* Comparation. */
                        assertEquals(originalEventResult, eventResultFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testFindEventsByCategoryWithNoEvents() throws Exception {
                try {
                        /* Find events. */
                        EventChunkTO results = searchFacade
                                        .findEventsByCategory("No category",
                                                        -1, -1, null, null);

                        if (!results.getEventTOs().isEmpty())
                                fail("Events were found where there should be none");
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestFindEventsByCategory.class);
        }

}
