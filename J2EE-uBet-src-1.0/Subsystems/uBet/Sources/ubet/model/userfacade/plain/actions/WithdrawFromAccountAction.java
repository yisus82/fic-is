package ubet.model.userfacade.plain.actions;

import java.sql.Connection;
import java.util.Calendar;

import ubet.model.account.dao.SQLAccountDAO;
import ubet.model.account.dao.SQLAccountDAOFactory;
import ubet.model.account.to.AccountTO;
import ubet.model.accountoperation.dao.SQLAccountOperationDAO;
import ubet.model.accountoperation.dao.SQLAccountOperationDAOFactory;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class WithdrawFromAccountAction implements TransactionalPlainAction {

        private Long accountID;

        private double amount;

        public WithdrawFromAccountAction(Long accountID, double amount) {
                this.accountID = accountID;
                this.amount = amount;
        }

        public Object execute(Connection connection)
                        throws InstanceNotFoundException,
                        InsufficientBalanceException, InternalErrorException {

                /* Find account. */
                SQLAccountDAO accountDAO = SQLAccountDAOFactory.getDAO();
                AccountTO accountTO = accountDAO.find(connection, accountID);

                /* Update account. */
                Double currentBalance = accountTO.getBalance();

                if (currentBalance < amount) {
                        throw new InsufficientBalanceException(accountID,
                                        currentBalance, amount);
                }

                accountTO.setBalance(currentBalance - amount);
                accountDAO.update(connection, accountTO);

                /* Register account operation. */
                SQLAccountOperationDAO accountOperationDAO = SQLAccountOperationDAOFactory
                                .getDAO();
                AccountOperationTO accountOperationTO = new AccountOperationTO(
                                new Long("-1"), accountTO.getAccountID(),
                                amount, AccountOperationTO.WITHDRAW_OPERATION,
                                "User withdrawal", Calendar.getInstance(), null);
                try {
                        accountOperationDAO.create(connection,
                                        accountOperationTO);
                } catch (DuplicateInstanceException e) {
                        throw new InternalErrorException(e);
                }

                return null;

        }

}
