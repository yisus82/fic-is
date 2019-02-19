package ubet.test.searchfacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;
import ubet.http.controller.caches.QuestionCache;
import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.adminfacade.delegate.AdminFacadeDelegateFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import ubet.model.searchfacade.to.BetTypeChunkTO;
import ubet.model.searchfacade.to.BetTypeResultTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import ubet.test.userfacade.TestFindBetsByAccount;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestFindBetTypesByEvent extends TestCase {

        private TestFacadeDelegate testFacade;

        private AdminFacadeDelegate adminFacade;

        private SearchFacadeDelegate searchFacade;

        private List<BetOptionTO> options;

        private BetOptionTO betOption1;

        private BetOptionTO betOption2;

        private BetOptionTO betOption3;

        private EventTO event;

        private BetTypeTO betType1;

        private BetTypeTO betType2;

        private BetTypeTO betType3;

        public TestFindBetTypesByEvent(String name) {
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
                options = new ArrayList<BetOptionTO>();

                betOption1 = new BetOptionTO(new Long("-1"), "Deportivo",
                                new Double("1.05"), new Long("-1"),
                                BetOptionTO.PENDING);

                betOption2 = new BetOptionTO(new Long("-1"), "X", new Double(
                                "2.34"), new Long("-1"), BetOptionTO.PENDING);

                betOption3 = new BetOptionTO(new Long("-1"), "Real Madrid",
                                new Double("12.53"), new Long("-1"),
                                BetOptionTO.PENDING);

                options.add(betOption1);
                options.add(betOption2);
                options.add(betOption3);

                betType1 = new BetTypeTO(new Long("-1"), new Long("-1"),
                                new Long("1"));

                event = new EventTO(new Long("-1"), "Deportivo - Real Madrid",
                                Calendar.getInstance(), "Root", new Long("-1"),
                                EventTO.NO, Calendar.getInstance());

                event = adminFacade.insertEvent(event, betType1, options);

                betType1.setBetTypeID(event.getBetTypeID());

                betType1.setEventID(event.getEventID());

                options = new ArrayList<BetOptionTO>();

                betOption1 = new BetOptionTO(new Long("-1"), "Deportivo",
                                new Double("1.35"), new Long("-1"),
                                BetOptionTO.PENDING);

                betOption2 = new BetOptionTO(new Long("-1"), "Real Madrid",
                                new Double("2.53"), new Long("-1"),
                                BetOptionTO.PENDING);

                options.add(betOption1);
                options.add(betOption2);

                betType2 = new BetTypeTO(new Long("-1"), event.getEventID(),
                                new Long("4"));

                betType2 = adminFacade.insertBetType(betType2, options);

                options = new ArrayList<BetOptionTO>();

                betOption1 = new BetOptionTO(new Long("-1"), "3 - 0",
                                new Double("1.05"), new Long("-1"),
                                BetOptionTO.PENDING);

                betOption2 = new BetOptionTO(new Long("-1"), "2 - 1",
                                new Double("1.64"), new Long("-1"),
                                BetOptionTO.PENDING);

                betOption3 = new BetOptionTO(new Long("-1"), "0 - 1",
                                new Double("21.53"), new Long("-1"),
                                BetOptionTO.PENDING);

                options.add(betOption1);
                options.add(betOption2);
                options.add(betOption3);

                betType3 = new BetTypeTO(new Long("-1"), event.getEventID(),
                                new Long("3"));

                betType3 = adminFacade.insertBetType(betType3, options);
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeEvent(event.getEventID());
        }

        public void testFindAllBetTypes() throws Exception {
                try {
                        /* Find bet types. */
                        BetTypeChunkTO results = searchFacade
                                        .findBetTypesByEvent(
                                                        event.getEventID(), -1,
                                                        -1);
                        List<BetTypeResultTO> betTypeTOs = results
                                        .getBetTypeTOs();

                        if (betTypeTOs.size() != 3)
                                fail("Incorrect number of bet types found (Expected = 3 Found = "
                                                + betTypeTOs.size() + ")");

                        /* Comparations. */

                        /* Create the correct bet type result. */
                        QuestionTO question = QuestionCache
                                        .getQuestion(betType1.getQuestionID());
                        List<BetOptionTO> options = searchFacade
                                        .findBetOptions(
                                                        betType1.getBetTypeID(),
                                                        -1, -1);
                        BetTypeResultTO originalBetTypeResult = new BetTypeResultTO(
                                        betType1.getBetTypeID(), betType1
                                                        .getEventID(),
                                        question, options);
                        BetTypeResultTO betTypeResultFound = betTypeTOs.get(0);

                        /* Comparation. */
                        assertEquals(originalBetTypeResult, betTypeResultFound);

                        /* Create the correct bet type result. */
                        question = QuestionCache.getQuestion(betType2
                                        .getQuestionID());
                        options = searchFacade.findBetOptions(betType2
                                        .getBetTypeID(), -1, -1);
                        originalBetTypeResult = new BetTypeResultTO(betType2
                                        .getBetTypeID(), betType2.getEventID(),
                                        question, options);
                        betTypeResultFound = betTypeTOs.get(1);

                        /* Comparation. */
                        assertEquals(originalBetTypeResult, betTypeResultFound);

                        /* Create the correct bet type result. */
                        question = QuestionCache.getQuestion(betType3
                                        .getQuestionID());
                        options = searchFacade.findBetOptions(betType3
                                        .getBetTypeID(), -1, -1);
                        originalBetTypeResult = new BetTypeResultTO(betType3
                                        .getBetTypeID(), betType3.getEventID(),
                                        question, options);
                        betTypeResultFound = betTypeTOs.get(2);

                        /* Comparation. */
                        assertEquals(originalBetTypeResult, betTypeResultFound);

                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public void testFindBetTypesPageByPage() throws Exception {
                try {
                        /* Find bet types. */
                        BetTypeChunkTO results = searchFacade
                                        .findBetTypesByEvent(
                                                        event.getEventID(), 1,
                                                        2);

                        List<BetTypeResultTO> betTypeTOs = results
                                        .getBetTypeTOs();

                        if (betTypeTOs.size() != 2)
                                fail("Incorrect number of bet types found (Expected = 2 Found = "
                                                + betTypeTOs.size() + ")");

                        /* Comparations. */

                        /* Create the correct bet type result. */
                        QuestionTO question = QuestionCache
                                        .getQuestion(betType1.getQuestionID());
                        List<BetOptionTO> options = searchFacade
                                        .findBetOptions(
                                                        betType1.getBetTypeID(),
                                                        -1, -1);
                        BetTypeResultTO originalBetTypeResult = new BetTypeResultTO(
                                        betType1.getBetTypeID(), betType1
                                                        .getEventID(),
                                        question, options);
                        BetTypeResultTO betTypeResultFound = betTypeTOs.get(0);

                        /* Comparation. */
                        assertEquals(originalBetTypeResult, betTypeResultFound);

                        /* Create the correct bet type result. */
                        question = QuestionCache.getQuestion(betType2
                                        .getQuestionID());
                        options = searchFacade.findBetOptions(betType2
                                        .getBetTypeID(), -1, -1);
                        originalBetTypeResult = new BetTypeResultTO(betType2
                                        .getBetTypeID(), betType2.getEventID(),
                                        question, options);
                        betTypeResultFound = betTypeTOs.get(1);

                        /* Comparation. */
                        assertEquals(originalBetTypeResult, betTypeResultFound);

                        /* Page 2. */
                        results = searchFacade.findBetTypesByEvent(event
                                        .getEventID(), 3, 2);

                        betTypeTOs = results.getBetTypeTOs();

                        if (betTypeTOs.size() != 1)
                                fail("Incorrect number of bet types found (Expected = 1 Found = "
                                                + betTypeTOs.size() + ")");

                        /* Create the correct bet type result. */
                        question = QuestionCache.getQuestion(betType3
                                        .getQuestionID());
                        options = searchFacade.findBetOptions(betType3
                                        .getBetTypeID(), -1, -1);
                        originalBetTypeResult = new BetTypeResultTO(betType3
                                        .getBetTypeID(), betType3.getEventID(),
                                        question, options);
                        betTypeResultFound = betTypeTOs.get(0);

                        /* Comparation. */
                        assertEquals(originalBetTypeResult, betTypeResultFound);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestFindBetsByAccount.class);
        }

}
