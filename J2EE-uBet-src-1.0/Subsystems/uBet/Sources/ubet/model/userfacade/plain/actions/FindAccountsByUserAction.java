package ubet.model.userfacade.plain.actions;

import java.sql.Connection;
import java.util.List;

import ubet.model.account.dao.SQLAccountDAO;
import ubet.model.account.dao.SQLAccountDAOFactory;
import ubet.model.account.to.AccountTO;
import ubet.model.userfacade.to.AccountChunkTO;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindAccountsByUserAction implements NonTransactionalPlainAction {

        private String userID;

        private int startIndex;

        private int count;

        public FindAccountsByUserAction(String userID, int startIndex, int count) {
                this.userID = userID;
                this.startIndex = startIndex;
                this.count = count;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                /*
                 * Find count+1 bet types to determine if there exist more bet
                 * types above the specified range.
                 */
                SQLAccountDAO accountDAO = SQLAccountDAOFactory.getDAO();

                List<AccountTO> accounts = accountDAO.findByUser(connection,
                                userID, startIndex, count + 1);
                boolean existMoreAccounts = (accounts.size() == (count + 1) && !accounts
                                .isEmpty());

                /*
                 * Remove the last account from the returned list if there exist
                 * more bet above the specified range.
                 */
                if (existMoreAccounts)
                        accounts.remove(accounts.size() - 1);

                return new AccountChunkTO(accounts, existMoreAccounts);
        }

}
