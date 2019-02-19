package ubet.test.testfacade.plain.actions;

import java.sql.Connection;

import ubet.model.account.dao.SQLAccountDAO;
import ubet.model.account.dao.SQLAccountDAOFactory;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class RemoveAccountAction implements TransactionalPlainAction {

        private Long accountID;

        public RemoveAccountAction(Long accountID) {
                this.accountID = accountID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLAccountDAO accountDAO = SQLAccountDAOFactory.getDAO();

                accountDAO.remove(connection, accountID);

                return null;
        }

}
