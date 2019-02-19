package ibei.model.productfacade.plain.actions;

import ibei.model.bid.dao.SQLBidDAO;
import ibei.model.bid.dao.SQLBidDAOFactory;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindBidsByProductAction implements NonTransactionalPlainAction {

        private Long productID;

        private int startIndex;

        private int count;

        public FindBidsByProductAction(Long productID, int startIndex, int count) {
                this.productID = productID;
                this.startIndex = startIndex;
                this.count = count;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLBidDAO bidDAO = SQLBidDAOFactory.getDAO();

                return bidDAO.findByProduct(connection, productID, startIndex,
                                count);

        }

}
