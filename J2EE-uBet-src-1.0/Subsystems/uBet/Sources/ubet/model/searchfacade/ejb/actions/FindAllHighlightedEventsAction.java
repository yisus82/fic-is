package ubet.model.searchfacade.ejb.actions;

import java.util.List;

import javax.persistence.EntityManager;

import ubet.model.event.entity.Event;
import ubet.model.event.to.EventTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;
import ubet.model.searchfacade.to.EventChunkTO;
import ubet.model.searchfacade.to.EventResultTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class FindAllHighlightedEventsAction {

        private EntityManager entityManager;

        public FindAllHighlightedEventsAction(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public EventChunkTO execute() throws InternalErrorException {
                List<Event> events = entityManager
                                .createQuery(
                                                "SELECT e FROM Event e "
                                                                + "WHERE e.highlighted <> :highlighted ORDER BY e.date")
                                .setParameter("highlighted", EventTO.NO)
                                .getResultList();

                List<EventResultTO> eventResults = SearchFacadeHelper
                                .toEventResultTOs(entityManager, events);

                return new EventChunkTO(eventResults, false);
        }

}
