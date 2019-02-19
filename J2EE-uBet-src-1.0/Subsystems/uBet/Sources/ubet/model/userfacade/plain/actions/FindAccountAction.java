package ubet.model.userfacade.plain.actions;

import java.sql.Connection;

import ubet.model.account.dao.SQLAccountDAO;
import ubet.model.account.dao.SQLAccountDAOFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindAccountAction implements NonTransactionalPlainAction {

        private Long accountID;

        public FindAccountAction(Long accountID) {
                this.accountID = accountID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLAccountDAO accountDAO = SQLAccountDAOFactory.getDAO();

                return accountDAO.find(connection, accountID);
        }

}
