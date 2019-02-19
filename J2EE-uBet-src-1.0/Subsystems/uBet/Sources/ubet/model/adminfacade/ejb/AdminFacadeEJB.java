package ubet.model.adminfacade.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ubet.model.adminfacade.ejb.actions.PublishResultsAction;
import ubet.model.betoption.entity.BetOption;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.entity.BetType;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.entity.Event;
import ubet.model.event.to.EventTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;
import ubet.model.util.GlobalNames;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

@Stateless
public class AdminFacadeEJB implements LocalAdminFacade, RemoteAdminFacade {

        @PersistenceContext(unitName = GlobalNames.PERSISTENCE_UNIT)
        private EntityManager entityManager;

        public AdminFacadeEJB() {
        }

        public EventTO insertEvent(EventTO eventTO, BetTypeTO betTypeTO,
                        List<BetOptionTO> options)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                Event event = SearchFacadeHelper
                                .toEvent(entityManager, eventTO);

                entityManager.persist(event);

                betTypeTO.setEventID(event.getEventID());

                BetType betType = SearchFacadeHelper.toBetType(entityManager,
                                betTypeTO);

                entityManager.persist(betType);

                List<BetOption> betOptions = new ArrayList<BetOption>();

                for (BetOptionTO option : options) {
                        option.setBetTypeID(betType.getBetTypeID());
                        betOptions.add(SearchFacadeHelper.toBetOption(
                                        entityManager, option));
                }

                betType.setBetOptions(betOptions);

                event.setBetType(betType);

                return SearchFacadeHelper.toEventTO(event);
        }

        public BetTypeTO insertBetType(BetTypeTO betTypeTO,
                        List<BetOptionTO> options)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                BetType betType = SearchFacadeHelper.toBetType(entityManager,
                                betTypeTO);

                entityManager.persist(betType);

                List<BetOption> betOptions = new ArrayList<BetOption>();

                for (BetOptionTO option : options) {
                        option.setBetTypeID(betType.getBetTypeID());
                        betOptions.add(SearchFacadeHelper.toBetOption(
                                        entityManager, option));
                }

                betType.setBetOptions(betOptions);

                return SearchFacadeHelper.toBetTypeTO(betType);
        }

        public void publishResults(List<Long> winnerOptions)
                        throws InternalErrorException {
                PublishResultsAction action = new PublishResultsAction(
                                entityManager, winnerOptions);

                action.execute();
        }

        public void setHighlighted(Long eventID, boolean highlighted)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                Event event = SearchFacadeHelper.findEvent(entityManager,
                                eventID);

                if (highlighted)
                        event.setHighlighted(EventTO.READY);
                else
                        event.setHighlighted(EventTO.NO);
        }

}
