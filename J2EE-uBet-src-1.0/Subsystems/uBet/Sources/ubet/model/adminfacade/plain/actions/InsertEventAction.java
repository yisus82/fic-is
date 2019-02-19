package ubet.model.adminfacade.plain.actions;

import java.sql.Connection;
import java.util.List;

import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;
import ubet.model.event.to.EventTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class InsertEventAction implements TransactionalPlainAction {

        private EventTO eventTO;

        private BetTypeTO betTypeTO;

        private List<BetOptionTO> options;

        public InsertEventAction(EventTO eventTO, BetTypeTO betTypeTO,
                        List<BetOptionTO> options) {
                this.eventTO = eventTO;
                this.betTypeTO = betTypeTO;
                this.options = options;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();

                eventTO = eventDAO.create(connection, eventTO);

                betTypeTO.setEventID(eventTO.getEventID());

                betTypeTO = (BetTypeTO) new InsertBetTypeAction(betTypeTO,
                                options).execute(connection);

                eventTO.setBetTypeID(betTypeTO.getBetTypeID());

                eventDAO.update(connection, eventTO);

                return eventTO;
        }

}
