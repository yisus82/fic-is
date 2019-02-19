package ibei.model.userfacade.plain.actions;

import ibei.model.bid.dao.SQLBidDAO;
import ibei.model.bid.dao.SQLBidDAOFactory;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindBidsByUserAction implements NonTransactionalPlainAction {

        private String login;

        private int startIndex;

        private int count;

        public FindBidsByUserAction(String login, int startIndex, int count) {
                this.login = login;
                this.startIndex = startIndex;
                this.count = count;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLBidDAO bidDAO = SQLBidDAOFactory.getDAO();

                return bidDAO.findByUser(connection, login, startIndex, count);

        }

}
