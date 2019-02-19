package ibei.test.testfacade.plain.actions;

import ibei.model.bid.dao.SQLBidDAO;
import ibei.model.bid.dao.SQLBidDAOFactory;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class FindBidAction implements TransactionalPlainAction {

        private Long bidID;

        public FindBidAction(Long bidID) {
                this.bidID = bidID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLBidDAO dao = SQLBidDAOFactory.getDAO();
                return dao.find(connection, bidID);

        }

}
