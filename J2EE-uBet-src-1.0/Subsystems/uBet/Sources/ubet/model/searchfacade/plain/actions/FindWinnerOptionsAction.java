package ubet.model.searchfacade.plain.actions;

import java.sql.Connection;

import ubet.model.betoption.dao.SQLBetOptionDAO;
import ubet.model.betoption.dao.SQLBetOptionDAOFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindWinnerOptionsAction implements NonTransactionalPlainAction {

        private Long betTypeID;

        public FindWinnerOptionsAction(Long betTypeID) {
                this.betTypeID = betTypeID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLBetOptionDAO betOptionDAO = SQLBetOptionDAOFactory.getDAO();

                return betOptionDAO.findWinners(connection, betTypeID);
        }

}
