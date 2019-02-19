package ubet.model.searchfacade.ejb.actions;

import java.util.List;

import javax.persistence.EntityManager;

import ubet.model.betoption.entity.BetOption;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class FindWinnerOptionsAction {

        private EntityManager entityManager;

        private Long betTypeID;

        public FindWinnerOptionsAction(EntityManager entityManager,
                        Long betTypeID) {
                this.entityManager = entityManager;
                this.betTypeID = betTypeID;
        }

        public List<BetOptionTO> execute() throws InternalErrorException {
                List<BetOption> betOptions = entityManager
                                .createQuery(
                                                "SELECT o FROM BetOption o "
                                                                + "WHERE o.betType.betTypeID = :betTypeID AND o.status = :status "
                                                                + "ORDER BY o.betOptionID")
                                .setParameter("betTypeID", betTypeID)
                                .setParameter("status", BetOptionTO.WINNER)
                                .getResultList();

                return SearchFacadeHelper.toBetOptionTOs(betOptions);
        }

}
