package ubet.model.searchfacade.ejb.actions;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ubet.model.bettype.entity.BetType;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;
import ubet.model.searchfacade.to.BetTypeChunkTO;
import ubet.model.searchfacade.to.BetTypeResultTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class FindBetTypesByEventAction {

        private EntityManager entityManager;

        private Long eventID;

        private int startIndex;

        private int count;

        public FindBetTypesByEventAction(EntityManager entityManager,
                        Long eventID, int startIndex, int count) {
                this.entityManager = entityManager;
                this.eventID = eventID;
                this.startIndex = startIndex;
                this.count = count;
        }

        public BetTypeChunkTO execute() throws InternalErrorException {
                /*
                 * Find count+1 bet types to determine if there exist more bet
                 * types above the specified range.
                 */
                Query query = entityManager
                                .createQuery(
                                                "SELECT t FROM BetType t "
                                                                + "WHERE t.event.eventID = :eventID "
                                                                + "ORDER BY t.betTypeID")
                                .setParameter("eventID", eventID);
                if (startIndex > 0)
                        query = query.setFirstResult(startIndex - 1);
                if (count > 0)
                        query = query.setMaxResults(count + 1);
                List<BetType> betTypes = query.getResultList();

                boolean existMoreBetTypes = (betTypes.size() == (count + 1) && !betTypes
                                .isEmpty());

                /*
                 * Remove the last bet type from the returned list if there
                 * exist more bet types above the specified range.
                 */
                if (existMoreBetTypes)
                        betTypes.remove(betTypes.size() - 1);

                List<BetTypeResultTO> betTypeResults = SearchFacadeHelper
                                .toBetTypeResultTOs(entityManager, betTypes);

                return new BetTypeChunkTO(betTypeResults, existMoreBetTypes);
        }

}
