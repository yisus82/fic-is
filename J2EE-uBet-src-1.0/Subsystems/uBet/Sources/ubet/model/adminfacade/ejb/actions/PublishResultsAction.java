package ubet.model.adminfacade.ejb.actions;

import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;

import ubet.model.account.entity.Account;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.adminfacade.ejb.AdminFacadeHelper;
import ubet.model.bet.entity.Bet;
import ubet.model.bet.to.BetTO;
import ubet.model.betoption.entity.BetOption;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.event.entity.Event;
import ubet.model.userfacade.ejb.UserFacadeHelper;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class PublishResultsAction {

        private EntityManager entityManager;

        private List<Long> winnerOptions;

        public PublishResultsAction(EntityManager entityManager,
                        List<Long> winnerOptions) {
                this.entityManager = entityManager;
                this.winnerOptions = winnerOptions;
        }

        public void execute() throws InternalErrorException {
                for (Long optionID : winnerOptions) {
                        BetOption betOption = AdminFacadeHelper.findBetOption(
                                        entityManager, optionID);
                        betOption.setStatus(BetOptionTO.WINNER);

                        List<Bet> bets = betOption.getBets();
                        Double odds = betOption.getOdds();

                        for (Bet bet : bets) {
                                bet.setStatus(BetTO.GAINED);

                                Double amount = bet.getAmount();

                                Account account = bet.getAccount();
                                Double newBalance = account.getBalance()
                                                + (amount * odds);
                                account.setBalance(newBalance);

                                /* Register account operation. */
                                Event event = bet.getEvent();

                                UserFacadeHelper
                                                .createAccountOperation(
                                                                entityManager,
                                                                account,
                                                                amount * odds,
                                                                AccountOperationTO.ADD_OPERATION,
                                                                "Gained bet : "
                                                                                + event
                                                                                                .getDescription(),
                                                                Calendar
                                                                                .getInstance(),
                                                                bet);
                        }

                        List<BetOption> options = betOption.getBetType()
                                        .getBetOptions();

                        for (BetOption option : options) {
                                if (!winnerOptions.contains(option
                                                .getBetOptionID())) {

                                        option.setStatus(BetOptionTO.LOSER);

                                        bets = option.getBets();

                                        for (Bet bet : bets)
                                                bet.setStatus(BetTO.LOST);

                                }
                        }
                }
        }

}
