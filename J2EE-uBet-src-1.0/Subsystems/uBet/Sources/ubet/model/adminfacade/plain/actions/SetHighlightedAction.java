package ubet.model.adminfacade.plain.actions;

import java.sql.Connection;

import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class SetHighlightedAction implements TransactionalPlainAction {

        private Long eventID;

        private boolean highlighted;

        public SetHighlightedAction(Long eventID, boolean highlighted) {
                this.eventID = eventID;
                this.highlighted = highlighted;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();

                eventDAO.setHighlighted(connection, eventID, highlighted);

                return null;
        }

}
