package ubet.model.searchfacade.plain.actions;

import java.sql.Connection;

import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class UpdateHighlightedEventsAction implements TransactionalPlainAction {

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();

                eventDAO.updateHighlighted(connection);

                return null;
        }

}
