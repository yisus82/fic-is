package ubet.model.userfacade.ejb.actions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

public class FindFavoritesAction {

        private EntityManager entityManager;

        private Long accountID;

        public FindFavoritesAction(EntityManager entityManager, Long accountID) {
                this.entityManager = entityManager;
                this.accountID = accountID;
        }

        public List<String> execute() {
                String queryString = "SELECT b.event.category.categoryID, count(*) FROM Bet b WHERE b.account.accountID = :accountID"
                                + " AND b.date >= :date GROUP BY b.event.category.categoryID ORDER BY 2 DESC";
                Calendar date = Calendar.getInstance();
                date.add(Calendar.MONTH, -1);
                List<Object[]> results = entityManager.createQuery(queryString)
                                .setParameter("accountID", accountID)
                                .setParameter("date", date,
                                                TemporalType.TIMESTAMP)
                                .getResultList();
                List<String> categoryIDs = new ArrayList<String>();
                for (Object[] result : results)
                        categoryIDs.add((String) result[0]);

                return categoryIDs;
        }

}
