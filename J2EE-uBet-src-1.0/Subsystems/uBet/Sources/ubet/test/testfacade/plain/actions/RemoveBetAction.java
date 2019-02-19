package ubet.test.testfacade.plain.actions;

import java.sql.Connection;

import ubet.model.bet.dao.SQLBetDAO;
import ubet.model.bet.dao.SQLBetDAOFactory;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class RemoveBetAction implements TransactionalPlainAction {

        private Long betID;

        public RemoveBetAction(Long betID) {
                this.betID = betID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLBetDAO betDAO = SQLBetDAOFactory.getDAO();

                betDAO.remove(connection, betID);

                return null;
        }

}
