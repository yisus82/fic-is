package ubet.model.userfacade.plain.actions;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import ubet.model.accountoperation.dao.SQLAccountOperationDAO;
import ubet.model.accountoperation.dao.SQLAccountOperationDAOFactory;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.userfacade.to.AccountOperationChunkTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindAccountOperationsAction implements NonTransactionalPlainAction {

        private Long accountID;

        private int startIndex;

        private int count;

        private Calendar startDate;

        private Calendar endDate;

        public FindAccountOperationsAction(Long accountID, int startIndex,
                        int count, Calendar startDate, Calendar endDate) {
                this.accountID = accountID;
                this.startIndex = startIndex;
                this.count = count;
                this.startDate = startDate;
                this.endDate = endDate;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                /*
                 * Find count+1 account operations to determine if there exist
                 * more account operations above the specified range.
                 */
                List<AccountOperationTO> accountOperations;
                SQLAccountOperationDAO accountOperationDAO = SQLAccountOperationDAOFactory
                                .getDAO();

                accountOperations = accountOperationDAO.findByAccount(
                                connection, accountID, startIndex, count + 1,
                                startDate, endDate);
                boolean existMoreAccountOperations = (accountOperations.size() == (count + 1) && !accountOperations
                                .isEmpty());

                /*
                 * Remove the last account operation from the returned list if
                 * there exist more account operation above the specified range.
                 */
                if (existMoreAccountOperations)
                        accountOperations.remove(accountOperations.size() - 1);

                return new AccountOperationChunkTO(accountOperations,
                                existMoreAccountOperations);
        }

}
