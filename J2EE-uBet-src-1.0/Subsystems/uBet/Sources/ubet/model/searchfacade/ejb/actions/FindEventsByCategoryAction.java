package ubet.model.searchfacade.ejb.actions;

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

public class FindEventsByCategoryAction {

        private EntityManager entityManager;

        private String categoryID;

        private int startIndex;

        private int count;

        private Calendar startDate;

        private Calendar endDate;

        public FindEventsByCategoryAction(EntityManager entityManager,
                        String categoryID, int startIndex, int count,
                        Calendar startDate, Calendar endDate) {
                this.entityManager = entityManager;
                this.categoryID = categoryID;
                this.startIndex = startIndex;
                this.count = count;
                this.startDate = startDate;
                this.endDate = endDate;
        }

        public EventChunkTO execute() throws InternalErrorException {
                /*
                 * Find count+1 events to determine if there exist more events
                 * above the specified range.
                 */
                String queryString = "SELECT e FROM Event e WHERE e.category.categoryID = :categoryID";
                if (startDate != null)
                        queryString += " AND e.date >= :startDate";
                if (endDate != null)
                        queryString += " AND e.date <= :endDate";
                queryString += " ORDER BY e.date";
                Query query = entityManager.createQuery(queryString)
                                .setParameter("categoryID", categoryID);
                if (startDate != null)
                        query = query.setParameter("startDate", startDate,
                                        TemporalType.TIMESTAMP);
                if (endDate != null)
                        query = query.setParameter("endDate", endDate,
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
