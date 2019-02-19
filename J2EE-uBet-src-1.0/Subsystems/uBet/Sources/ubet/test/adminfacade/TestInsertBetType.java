package ubet.test.adminfacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;
import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.adminfacade.delegate.AdminFacadeDelegateFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestInsertBetType extends TestCase {

        private TestFacadeDelegate testFacade;

        private AdminFacadeDelegate adminFacade;

        private List<BetOptionTO> options;

        private BetOptionTO betOption1;

        private BetOptionTO betOption2;

        private BetOptionTO betOption3;

        private BetTypeTO betType;

        private EventTO event;

        public TestInsertBetType(String name) {
                super(name);

                try {
                        this.testFacade = TestFacadeDelegateFactory
                                        .getDelegate();
                        this.adminFacade = AdminFacadeDelegateFactory
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

                betType = new BetTypeTO(new Long("-1"), new Long("1"),
                                new Long("1"));

                event = new EventTO(new Long("-1"), "Deportivo - Real Madrid",
                                Calendar.getInstance(), "Root", new Long("-1"),
                                EventTO.NO, Calendar.getInstance());

                event = adminFacade.insertEvent(event, betType, options);

                betType.setBetTypeID(event.getBetTypeID());

                options = new Vector<BetOptionTO>();

                betOption1 = new BetOptionTO(new Long("-1"), "3", new Double(
                                "1.05"), new Long("-1"), BetOptionTO.PENDING);

                betOption2 = new BetOptionTO(new Long("-1"), "2", new Double(
                                "2.64"), new Long("-1"), BetOptionTO.PENDING);

                betOption3 = new BetOptionTO(new Long("-1"), "0", new Double(
                                "2.53"), new Long("-1"), BetOptionTO.PENDING);

                options.add(betOption1);
                options.add(betOption2);
                options.add(betOption3);
        }

        @Override
        public void tearDown() throws Exception {
                /* Leaving the DB as it was before. */
                testFacade.removeEvent(event.getEventID());
        }

        public void testInsertBetType() throws Exception {
                /* Create betType. */
                BetTypeTO originalBetType = new BetTypeTO(new Long("-1"), event
                                .getEventID(), new Long("3"));

                try {
                        /* Insert betType. */
                        BetTypeTO insertedBetType = adminFacade.insertBetType(
                                        originalBetType, options);

                        /* Remove betType. */
                        testFacade
                                        .removeBetType(insertedBetType
                                                        .getBetTypeID());

                        /* Set the betTypeID. */
                        originalBetType.setBetTypeID(insertedBetType
                                        .getBetTypeID());

                        /* Comparation. */
                        assertEquals(originalBetType, insertedBetType);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }

        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestInsertBetType.class);
        }

}
