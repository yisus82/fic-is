package ubet.model.adminfacade.plain.actions;

import java.sql.Connection;
import java.util.List;

import ubet.model.betoption.dao.SQLBetOptionDAO;
import ubet.model.betoption.dao.SQLBetOptionDAOFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.dao.SQLBetTypeDAO;
import ubet.model.bettype.dao.SQLBetTypeDAOFactory;
import ubet.model.bettype.to.BetTypeTO;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class InsertBetTypeAction implements TransactionalPlainAction {

        private BetTypeTO betTypeTO;

        private List<BetOptionTO> options;

        public InsertBetTypeAction(BetTypeTO betTypeTO,
                        List<BetOptionTO> options) {
                this.betTypeTO = betTypeTO;
                this.options = options;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLBetTypeDAO betTypeDAO = SQLBetTypeDAOFactory.getDAO();

                betTypeTO = betTypeDAO.create(connection, betTypeTO);

                SQLBetOptionDAO betOptionDAO = SQLBetOptionDAOFactory.getDAO();

                for (BetOptionTO option : options) {
                        option.setBetTypeID(betTypeTO.getBetTypeID());
                        betOptionDAO.create(connection, option);
                }

                return betTypeTO;
        }

}
