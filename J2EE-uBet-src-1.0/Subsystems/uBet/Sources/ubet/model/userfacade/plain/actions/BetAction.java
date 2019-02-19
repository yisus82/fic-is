package ubet.model.userfacade.plain.actions;

import java.sql.Connection;
import java.util.Calendar;

import ubet.model.account.dao.SQLAccountDAO;
import ubet.model.account.dao.SQLAccountDAOFactory;
import ubet.model.account.to.AccountTO;
import ubet.model.accountoperation.dao.SQLAccountOperationDAO;
import ubet.model.accountoperation.dao.SQLAccountOperationDAOFactory;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.bet.dao.SQLBetDAO;
import ubet.model.bet.dao.SQLBetDAOFactory;
import ubet.model.bet.to.BetTO;
import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;
import ubet.model.event.to.EventTO;
import ubet.model.userfacade.exceptions.BetOutOfTimeException;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class BetAction implements TransactionalPlainAction {

        private BetTO betTO;

        public BetAction(BetTO betTO) {
                this.betTO = betTO;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();
                EventTO event = eventDAO.find(connection, betTO.getEventID());
                Calendar endDate = event.getDate();
                Calendar currentDate = betTO.getDate();
                if (currentDate.after(endDate))
                        throw new BetOutOfTimeException(endDate, currentDate);

                SQLAccountDAO accountDAO = SQLAccountDAOFactory.getDAO();
                AccountTO account = accountDAO.find(connection, betTO
                                .getAccountID());
                if (account.getBalance() < betTO.getAmount())
                        throw new InsufficientBalanceException(account
                                        .getAccountID(), account.getBalance(),
                                        betTO.getAmount());

                Double newBalance = account.getBalance() + betTO.getAmount();
                account.setBalance(newBalance);
                accountDAO.update(connection, account);

                SQLBetDAO betDAO = SQLBetDAOFactory.getDAO();

                BetTO bet = betDAO.create(connection, betTO);

                /* Register account operation. */
                SQLAccountOperationDAO accountOperationDAO = SQLAccountOperationDAOFactory
                                .getDAO();
                AccountOperationTO accountOperationTO = new AccountOperationTO(
                                new Long("-1"), account.getAccountID(), betTO
                                                .getAmount(),
                                AccountOperationTO.WITHDRAW_OPERATION, "Bet : "
                                                + event.getDescription(),
                                Calendar.getInstance(), bet.getBetID());
                try {
                        accountOperationDAO.create(connection,
                                        accountOperationTO);
                } catch (DuplicateInstanceException e) {
                        throw new InternalErrorException(e);
                }

                return betTO;
        }

}
