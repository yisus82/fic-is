package ubet.model.userfacade.ejb.actions;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ubet.model.account.entity.Account;
import ubet.model.userfacade.ejb.UserFacadeHelper;
import ubet.model.userfacade.to.AccountChunkTO;

public class FindAccountsByUserAction {

        private EntityManager entityManager;

        private String userID;

        private int startIndex;

        private int count;

        public FindAccountsByUserAction(EntityManager entityManager,
                        String userID, int startIndex, int count) {
                this.entityManager = entityManager;
                this.userID = userID;
                this.startIndex = startIndex;
                this.count = count;
        }

        public AccountChunkTO execute() {
                /*
                 * Find count+1 accounts to determine if there exist more
                 * accounts above the specified range.
                 */
                Query query = entityManager
                                .createQuery(
                                                "SELECT a FROM Account a "
                                                                + "WHERE a.user.login = :userID "
                                                                + "ORDER BY a.accountID")
                                .setParameter("userID", userID);
                if (startIndex > 0)
                        query = query.setFirstResult(startIndex - 1);
                if (count > 0)
                        query = query.setMaxResults(count + 1);
                List<Account> accounts = query.getResultList();
                boolean existMoreAccounts = ((accounts.size() == (count + 1)) && !accounts
                                .isEmpty());

                /*
                 * Remove the last account from the returned list if there exist
                 * more accounts above the specified range.
                 */
                if (existMoreAccounts) {
                        accounts.remove(accounts.size() - 1);
                }

                /* Return AccountChunkTO. */
                return new AccountChunkTO(UserFacadeHelper
                                .toAccountTOs(accounts), existMoreAccounts);
        }

}
