package ubet.model.searchfacade.ejb.actions;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ubet.model.betoption.entity.BetOption;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class FindBetOptionsAction {

        private EntityManager entityManager;

        private Long betTypeID;

        private int startIndex;

        private int count;

        public FindBetOptionsAction(EntityManager entityManager,
                        Long betTypeID, int startIndex, int count) {
                this.entityManager = entityManager;
                this.betTypeID = betTypeID;
                this.startIndex = startIndex;
                this.count = count;
        }

        public List<BetOptionTO> execute() throws InternalErrorException {
                Query query = entityManager
                                .createQuery(
                                                "SELECT o FROM BetOption o "
                                                                + "WHERE o.betType.betTypeID = :betTypeID "
                                                                + "ORDER BY o.betOptionID")
                                .setParameter("betTypeID", betTypeID);
                if (startIndex > 0)
                        query = query.setFirstResult(startIndex - 1);
                if (count > 0)
                        query = query.setMaxResults(count);
                List<BetOption> betOptions = query.getResultList();
                return SearchFacadeHelper.toBetOptionTOs(betOptions);
        }

}
