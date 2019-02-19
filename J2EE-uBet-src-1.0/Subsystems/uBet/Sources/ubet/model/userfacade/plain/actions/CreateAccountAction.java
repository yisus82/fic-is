package ubet.model.userfacade.plain.actions;

import java.sql.Connection;

import ubet.model.account.dao.SQLAccountDAO;
import ubet.model.account.dao.SQLAccountDAOFactory;
import ubet.model.account.to.AccountTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class CreateAccountAction implements TransactionalPlainAction {

        private AccountTO accountTO;

        public CreateAccountAction(AccountTO accountTO) {
                this.accountTO = accountTO;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLAccountDAO accountDAO = SQLAccountDAOFactory.getDAO();

                return accountDAO.create(connection, accountTO);
        }

}
