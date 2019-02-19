package ubet.model.adminfacade.ejb;

import javax.persistence.EntityManager;

import ubet.model.betoption.entity.BetOption;

public class AdminFacadeHelper {

        private AdminFacadeHelper() {
        }

        public static BetOption findBetOption(EntityManager entityManager,
                        Long betOptionID) {
                return entityManager.find(BetOption.class, betOptionID);
        }

}
