package ubet.model.userfacade.ejb.actions;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import ubet.model.accountoperation.entity.AccountOperation;
import ubet.model.userfacade.ejb.UserFacadeHelper;
import ubet.model.userfacade.to.AccountOperationChunkTO;

public class FindAccountOperationsAction {

        private EntityManager entityManager;

        private Long accountID;

        private int startIndex;

        private int count;

        private Calendar startDate;

        private Calendar endDate;

        public FindAccountOperationsAction(EntityManager entityManager,
                        Long accountID, int startIndex, int count,
                        Calendar startDate, Calendar endDate) {
                this.entityManager = entityManager;
                this.accountID = accountID;
                this.startIndex = startIndex;
                this.count = count;
                this.startDate = startDate;
                this.endDate = endDate;
        }

        public AccountOperationChunkTO execute() {
                /*
                 * Find count+1 account operations to determine if there exist
                 * more account operations above the specified range.
                 */
                String queryString = "SELECT o FROM AccountOperation o WHERE o.account.accountID = :accountID";
                if (startDate != null)
                        queryString += " AND o.date >= :startDate";
                if (endDate != null)
                        queryString += " AND o.date <= :endDate";
                queryString += " ORDER BY o.date DESC";
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
                List<AccountOperation> accountOperations = query
                                .getResultList();
                boolean existMoreAccountOperations = (accountOperations.size() == (count + 1) && !accountOperations
                                .isEmpty());

                /*
                 * Remove the last account operation from the returned list if
                 * there exist more account operations above the specified
                 * range.
                 */
                if (existMoreAccountOperations) {
                        accountOperations.remove(accountOperations.size() - 1);
                }

                /* Return AccountOperationChunkTO. */
                return new AccountOperationChunkTO(UserFacadeHelper
                                .toAccountOperationTOs(accountOperations),
                                existMoreAccountOperations);
        }

}
