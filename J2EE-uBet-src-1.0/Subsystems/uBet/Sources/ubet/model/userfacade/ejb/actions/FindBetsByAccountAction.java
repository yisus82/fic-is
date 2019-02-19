package ubet.model.userfacade.ejb.actions;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import ubet.model.bet.entity.Bet;
import ubet.model.userfacade.ejb.UserFacadeHelper;
import ubet.model.userfacade.to.BetChunkTO;

public class FindBetsByAccountAction {

        private EntityManager entityManager;

        private Long accountID;

        private int startIndex;

        private int count;

        private Calendar startDate;

        private Calendar endDate;

        public FindBetsByAccountAction(EntityManager entityManager,
                        Long accountID, int startIndex, int count,
                        Calendar startDate, Calendar endDate) {
                this.entityManager = entityManager;
                this.accountID = accountID;
                this.startIndex = startIndex;
                this.count = count;
                this.startDate = startDate;
                this.endDate = endDate;
        }

        public BetChunkTO execute() {
                /*
                 * Find count+1 bets to determine if there exist more bets above
                 * the specified range.
                 */
                String queryString = "SELECT b FROM Bet b WHERE b.account.accountID = :accountID";
                if (startDate != null)
                        queryString += " AND b.date >= :startDate";
                if (endDate != null)
                        queryString += " AND b.date <= :endDate";
                queryString += " ORDER BY b.date DESC";
                Query query = entityManager.createQuery(queryString)
                                .setParameter("accountID", accountID);
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
                List<Bet> bets = query.getResultList();
                boolean existMoreBets = (bets.size() == (count + 1) && !bets
                                .isEmpty());

                /*
                 * Remove the last bet from the returned list if there exist
                 * more bets above the specified range.
                 */
                if (existMoreBets) {
                        bets.remove(bets.size() - 1);
                }

                /* Return BetChunkTO. */
                return new BetChunkTO(UserFacadeHelper.toBetStatusTOs(
                                entityManager, bets), existMoreBets);
        }

}
