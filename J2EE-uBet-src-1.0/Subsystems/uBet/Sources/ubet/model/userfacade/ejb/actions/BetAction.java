package ubet.model.userfacade.ejb.actions;

import java.util.Calendar;

import javax.persistence.EntityManager;

import ubet.model.account.entity.Account;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.bet.entity.Bet;
import ubet.model.bet.to.BetTO;
import ubet.model.event.entity.Event;
import ubet.model.userfacade.ejb.UserFacadeHelper;
import ubet.model.userfacade.exceptions.BetOutOfTimeException;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;

public class BetAction {

        private EntityManager entityManager;

        private Bet bet;

        public BetAction(EntityManager entityManager, BetTO betTO) {
                this.entityManager = entityManager;
                this.bet = UserFacadeHelper.toBet(entityManager, betTO);
        }

        public BetTO execute() throws BetOutOfTimeException,
                        InsufficientBalanceException {

                Event event = bet.getEvent();
                Calendar endDate = event.getDate();
                Calendar currentDate = bet.getDate();
                if (currentDate.after(endDate))
                        throw new BetOutOfTimeException(endDate, currentDate);

                Account account = bet.getAccount();
                if (account.getBalance() < bet.getAmount())
                        throw new InsufficientBalanceException(account
                                        .getAccountID(), account.getBalance(),
                                        bet.getAmount());

                /* Modify balance. */
                Double newBalance = account.getBalance() + bet.getAmount();
                account.setBalance(newBalance);

                entityManager.persist(bet);

                /* Register account operation. */
                UserFacadeHelper.createAccountOperation(entityManager, account,
                                bet.getAmount(),
                                AccountOperationTO.WITHDRAW_OPERATION, "Bet : "
                                                + event.getDescription(),
                                Calendar.getInstance(), bet);

                return UserFacadeHelper.toBetTO(bet);
        }

}
