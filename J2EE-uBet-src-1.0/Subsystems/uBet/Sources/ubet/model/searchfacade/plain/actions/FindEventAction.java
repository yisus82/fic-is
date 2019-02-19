package ubet.model.searchfacade.plain.actions;

import java.sql.Connection;

import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindEventAction implements NonTransactionalPlainAction {

        private Long eventID;

        public FindEventAction(Long eventID) {
                this.eventID = eventID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();

                return eventDAO.find(connection, eventID);
        }

}
