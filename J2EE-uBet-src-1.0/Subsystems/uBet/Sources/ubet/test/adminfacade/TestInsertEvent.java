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
import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.delegate.TestFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.SimpleDataSource;

public class TestInsertEvent extends TestCase {

        private TestFacadeDelegate testFacade;

        private AdminFacadeDelegate adminFacade;

        private List<BetOptionTO> options;

        private BetOptionTO betOption1;

        private BetOptionTO betOption2;

        private BetOptionTO betOption3;

        private BetTypeTO betType;

        public TestInsertEvent(String name) {
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

                betType = new BetTypeTO(new Long("-1"), new Long("-1"),
                                new Long("1"));
        }

        public void testInsertEvent() throws Exception {
                /* Create event. */
                EventTO originalEvent = new EventTO(new Long("-1"),
                                "Description", Calendar.getInstance(), "Root",
                                new Long("-1"), EventTO.NO, Calendar
                                                .getInstance());

                try {
                        /* Insert event. */
                        EventTO insertedEvent = adminFacade.insertEvent(
                                        originalEvent, betType, options);
                        originalEvent
                                        .setBetTypeID(insertedEvent
                                                        .getBetTypeID());

                        /* Remove event. */
                        testFacade.removeEvent(insertedEvent.getEventID());

                        /* Set the eventID. */
                        originalEvent.setEventID(insertedEvent.getEventID());

                        /* Comparation. */
                        assertEquals(originalEvent, insertedEvent);
                } catch (Exception e) {
                        e.printStackTrace();
                        throw e;
                }
        }

        public static void main(String[] args) {
                junit.textui.TestRunner.run(TestInsertEvent.class);
        }

}
