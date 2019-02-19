package ubet.model.searchfacade.ejb.actions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import ubet.model.event.entity.Event;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;
import ubet.model.searchfacade.to.EventChunkTO;
import ubet.model.searchfacade.to.EventResultTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class FindRecentEventsAction {

        private EntityManager entityManager;

        private List<String> categoryIDs;

        private int startIndex;

        private int count;

        public FindRecentEventsAction(EntityManager entityManager,
                        List<String> categoryIDs, int startIndex, int count) {
                this.entityManager = entityManager;
                this.categoryIDs = categoryIDs;
                this.startIndex = startIndex;
                this.count = count;
        }

        public EventChunkTO execute() throws InternalErrorException {
                if (categoryIDs.isEmpty())
                        return new EventChunkTO(new ArrayList<EventResultTO>(),
                                        false);
                /*
                 * Find count+1 events to determine if there exist more events
                 * above the specified range.
                 */
                String queryString = "SELECT e1 FROM Event e1 WHERE insertionDate IN"
                                + " (SELECT MAX(insertionDate) FROM Event e2 WHERE e1.category.categoryID = e2.category.categoryID"
                                + " GROUP BY e2.category.categoryID) AND e1.category.categoryID IN (:categoryIDs) AND e1.date > :date";
                Query query = entityManager.createQuery(queryString)
                                .setParameter("categoryIDs", categoryIDs)
                                .setParameter(
                                                "date",
                                                Calendar.getInstance(),
                                                TemporalType.TIMESTAMP);
                if (startIndex > 0)
                        query = query.setFirstResult(startIndex - 1);
                if (count > 0)
                        query = query.setMaxResults(count + 1);
                List<Event> events = query.getResultList();

                boolean existMoreEvents = (events.size() == (count + 1) && !events
                                .isEmpty());

                /*
                 * Remove the last event from the returned list if there exist
                 * more events above the specified range.
                 */
                if (existMoreEvents)
                        events.remove(events.size() - 1);

                List<EventResultTO> eventResults = SearchFacadeHelper
                                .toEventResultTOs(entityManager, events);

                return new EventChunkTO(eventResults, existMoreEvents);
        }

}
