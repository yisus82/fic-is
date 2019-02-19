package ubet.model.userfacade.ejb.actions;

import java.util.Calendar;

import javax.persistence.EntityManager;

import ubet.model.account.entity.Account;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.userfacade.ejb.UserFacadeHelper;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;

public class AddToAccountAction {

        private EntityManager entityManager;

        private Long accountID;

        private Double amount;

        public AddToAccountAction(EntityManager entityManager, Long accountID,
                        Double amount) {
                this.entityManager = entityManager;
                this.accountID = accountID;
                this.amount = amount;
        }

        public void execute() throws InstanceNotFoundException {
                /* Find account. */
                Account account = UserFacadeHelper.findAccount(entityManager,
                                accountID);

                /* Modify balance. */
                account.setBalance(account.getBalance() + amount);

                /* Register account operation. */
                UserFacadeHelper.createAccountOperation(entityManager, account,
                                amount, AccountOperationTO.ADD_OPERATION,
                                "User deposit", Calendar.getInstance(), null);
        }

}
