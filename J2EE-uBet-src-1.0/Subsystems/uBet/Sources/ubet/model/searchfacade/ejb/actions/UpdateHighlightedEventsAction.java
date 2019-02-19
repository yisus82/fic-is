package ubet.model.searchfacade.ejb.actions;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import ubet.model.event.entity.Event;
import ubet.model.event.to.EventTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class UpdateHighlightedEventsAction {

        private EntityManager entityManager;

        public UpdateHighlightedEventsAction(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public void execute() throws InternalErrorException {
                List<Event> events = entityManager.createQuery(
                                "SELECT e FROM Event e ORDER BY e.date")
                                .getResultList();

                int i = 0;

                for (Event event : events) {
                        if (event.getDate().before(Calendar.getInstance()))
                                event.setHighlighted(EventTO.NO);
                        if (event.getHighlighted().equals(EventTO.YES))
                                event.setHighlighted(EventTO.WAITING);
                        if (event.getHighlighted().equals(EventTO.READY)) {
                                if (i < 3) {
                                        event.setHighlighted(EventTO.YES);
                                        i++;
                                }
                        }
                }

                if (i < 3)
                        for (Event event : events) {
                                if (event.getHighlighted().equals(
                                                EventTO.WAITING))
                                        if (i < 3) {
                                                event
                                                                .setHighlighted(EventTO.YES);
                                                i++;
                                        } else
                                                event
                                                                .setHighlighted(EventTO.READY);
                        }
        }

}
