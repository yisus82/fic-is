package ubet.model.userfacade.ejb.actions;

import java.util.Calendar;

import javax.persistence.EntityManager;

import ubet.model.account.entity.Account;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.userfacade.ejb.UserFacadeHelper;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;

public class WithdrawFromAccountAction {

        private EntityManager entityManager;

        private Long accountID;

        private Double amount;

        public WithdrawFromAccountAction(EntityManager entityManager,
                        Long accountID, Double amount) {
                this.entityManager = entityManager;
                this.accountID = accountID;
                this.amount = amount;
        }

        public void execute() throws InstanceNotFoundException,
                        InsufficientBalanceException {
                /* Find account. */
                Account account = UserFacadeHelper.findAccount(entityManager,
                                accountID);

                /* Modify balance. */
                Double currentBalance = account.getBalance();

                if (currentBalance < amount) {
                        throw new InsufficientBalanceException(accountID,
                                        currentBalance, amount);
                }

                account.setBalance(currentBalance - amount);

                /* Register account operation. */
                UserFacadeHelper
                                .createAccountOperation(
                                                entityManager,
                                                account,
                                                amount,
                                                AccountOperationTO.WITHDRAW_OPERATION,
                                                "User withdrawal", Calendar
                                                                .getInstance(),
                                                null);
        }

}
