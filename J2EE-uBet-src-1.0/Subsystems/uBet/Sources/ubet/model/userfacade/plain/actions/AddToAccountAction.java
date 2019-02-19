package ubet.model.userfacade.plain.actions;

import java.sql.Connection;
import java.util.Calendar;

import ubet.model.account.dao.SQLAccountDAO;
import ubet.model.account.dao.SQLAccountDAOFactory;
import ubet.model.account.to.AccountTO;
import ubet.model.accountoperation.dao.SQLAccountOperationDAO;
import ubet.model.accountoperation.dao.SQLAccountOperationDAOFactory;
import ubet.model.accountoperation.to.AccountOperationTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class AddToAccountAction implements TransactionalPlainAction {

        private Long accountID;

        private Double amount;

        public AddToAccountAction(Long accountID, Double amount) {
                this.accountID = accountID;
                this.amount = amount;
        }

        public Object execute(Connection connection)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                /* Find account. */
                SQLAccountDAO accountDAO = SQLAccountDAOFactory.getDAO();
                AccountTO accountTO = accountDAO.find(connection, accountID);

                /* Update account. */
                accountTO.setBalance(accountTO.getBalance() + amount);
                accountDAO.update(connection, accountTO);

                /* Register account operation. */
                SQLAccountOperationDAO accountOperationDAO = SQLAccountOperationDAOFactory
                                .getDAO();
                AccountOperationTO accountOperationTO = new AccountOperationTO(
                                new Long("-1"), accountTO.getAccountID(),
                                amount, AccountOperationTO.ADD_OPERATION,
                                "User deposit", Calendar.getInstance(), null);
                try {
                        accountOperationDAO.create(connection,
                                        accountOperationTO);
                } catch (DuplicateInstanceException e) {
                        throw new InternalErrorException(e);
                }

                return null;
        }

}
