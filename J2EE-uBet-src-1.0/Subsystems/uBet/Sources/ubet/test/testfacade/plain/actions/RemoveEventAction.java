package ubet.test.testfacade.plain.actions;

import java.sql.Connection;

import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class RemoveEventAction implements TransactionalPlainAction {

        private Long eventID;

        public RemoveEventAction(Long eventID) {
                this.eventID = eventID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();

                eventDAO.remove(connection, eventID);

                return null;
        }

}
