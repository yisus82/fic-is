package ubet.model.adminfacade.plain.actions;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import ubet.model.account.dao.SQLAccountDAO;
import ubet.model.account.dao.SQLAccountDAOFactory;
import ubet.model.account.to.AccountTO;
import ubet.model.accountoperation.dao.SQLAccountOperationDAO;
import ubet.model.accountoperation.dao.SQLAccountOperationDAOFactory;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.bet.dao.SQLBetDAO;
import ubet.model.bet.dao.SQLBetDAOFactory;
import ubet.model.bet.to.BetTO;
import ubet.model.betoption.dao.SQLBetOptionDAO;
import ubet.model.betoption.dao.SQLBetOptionDAOFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class PublishResultsAction implements TransactionalPlainAction {

        private List<Long> winnerOptions;

        public PublishResultsAction(List<Long> winnerOptions) {
                this.winnerOptions = winnerOptions;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                for (Long optionID : winnerOptions) {
                        SQLBetOptionDAO betOptionDAO = SQLBetOptionDAOFactory
                                        .getDAO();
                        SQLBetDAO betDAO = SQLBetDAOFactory.getDAO();

                        BetOptionTO betOptionTO = betOptionDAO.find(connection,
                                        optionID);
                        betOptionTO.setStatus(BetOptionTO.WINNER);
                        betOptionDAO.update(connection, betOptionTO);

                        List<BetTO> betTOs = betDAO.findByBetOption(connection,
                                        optionID, -1, -1, null, null);
                        Double odds = betOptionTO.getOdds();

                        for (BetTO bet : betTOs) {
                                bet.setStatus(BetTO.GAINED);
                                betDAO.update(connection, bet);

                                Double amount = bet.getAmount();
                                SQLAccountDAO accountDAO = SQLAccountDAOFactory
                                                .getDAO();
                                AccountTO account = accountDAO.find(connection,
                                                bet.getAccountID());
                                Double newBalance = account.getBalance()
                                                + (amount * odds);
                                account.setBalance(newBalance);
                                accountDAO.update(connection, account);

                                /* Register account operation. */
                                SQLEventDAO eventDAO = SQLEventDAOFactory
                                                .getDAO();
                                String description = eventDAO.find(connection,
                                                bet.getEventID())
                                                .getDescription();
                                SQLAccountOperationDAO accountOperationDAO = SQLAccountOperationDAOFactory
                                                .getDAO();
                                AccountOperationTO accountOperationTO = new AccountOperationTO(
                                                new Long("-1"),
                                                account.getAccountID(),
                                                amount * odds,
                                                AccountOperationTO.ADD_OPERATION,
                                                "Gained Bet : " + description,
                                                Calendar.getInstance(), bet
                                                                .getBetID());
                                try {
                                        accountOperationDAO.create(connection,
                                                        accountOperationTO);
                                } catch (DuplicateInstanceException e) {
                                        throw new InternalErrorException(e);
                                }
                        }

                        List<BetOptionTO> betOptions = betOptionDAO
                                        .findByBetType(connection, betOptionTO
                                                        .getBetTypeID(), -1, -1);

                        for (BetOptionTO betOption : betOptions) {
                                if (!winnerOptions.contains(betOption
                                                .getBetOptionID())) {

                                        betOption.setStatus(BetOptionTO.LOSER);
                                        betOptionDAO.update(connection,
                                                        betOption);

                                        betTOs = betDAO
                                                        .findByBetOption(
                                                                        connection,
                                                                        betOption
                                                                                        .getBetOptionID(),
                                                                        -1, -1,
                                                                        null,
                                                                        null);

                                        for (BetTO bet : betTOs) {
                                                bet.setStatus(BetTO.LOST);
                                                betDAO.update(connection, bet);
                                        }
                                }
                        }
                }
                return null;
        }

}
