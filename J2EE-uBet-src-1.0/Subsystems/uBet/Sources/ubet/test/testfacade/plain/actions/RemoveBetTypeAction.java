package ubet.test.testfacade.plain.actions;

import java.sql.Connection;

import ubet.model.bettype.dao.SQLBetTypeDAO;
import ubet.model.bettype.dao.SQLBetTypeDAOFactory;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class RemoveBetTypeAction implements TransactionalPlainAction {

        private Long betTypeID;

        public RemoveBetTypeAction(Long betTypeID) {
                this.betTypeID = betTypeID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLBetTypeDAO betTypeDAO = SQLBetTypeDAOFactory.getDAO();

                betTypeDAO.remove(connection, betTypeID);

                return null;
        }

}
