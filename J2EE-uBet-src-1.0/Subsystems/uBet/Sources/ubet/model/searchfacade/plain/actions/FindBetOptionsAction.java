package ubet.model.searchfacade.plain.actions;

import java.sql.Connection;

import ubet.model.betoption.dao.SQLBetOptionDAO;
import ubet.model.betoption.dao.SQLBetOptionDAOFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindBetOptionsAction implements NonTransactionalPlainAction {

        private Long betTypeID;

        private int startIndex;

        private int count;

        public FindBetOptionsAction(Long betTypeID, int startIndex, int count) {
                this.betTypeID = betTypeID;
                this.startIndex = startIndex;
                this.count = count;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLBetOptionDAO betOptionDAO = SQLBetOptionDAOFactory.getDAO();

                return betOptionDAO.findByBetType(connection, betTypeID,
                                startIndex, count);
        }

}
