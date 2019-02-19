package ubet.model.userfacade.plain.actions;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ubet.model.bet.dao.SQLBetDAO;
import ubet.model.bet.dao.SQLBetDAOFactory;
import ubet.model.bet.to.BetTO;
import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;
import ubet.model.event.to.EventTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindFavoritesAction implements NonTransactionalPlainAction {

        private Long accountID;

        public FindFavoritesAction(Long accountID) {
                this.accountID = accountID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                Calendar date = Calendar.getInstance();
                date.add(Calendar.MONTH, -1);

                SQLBetDAO betDAO = SQLBetDAOFactory.getDAO();
                List<BetTO> bets = betDAO.findByAccount(connection, accountID,
                                -1, -1, date, null);

                List<Long> eventIDs = new ArrayList<Long>();
                for (BetTO betTO : bets)
                        eventIDs.add(betTO.getEventID());

                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();
                Map<Long, EventTO> events = eventDAO.findByIDs(connection,
                                eventIDs);

                List<String> categoryIDs = new ArrayList<String>();
                for (EventTO eventTO : events.values())
                        categoryIDs.add(eventTO.getCategoryID());

                return categoryIDs;
        }

}
