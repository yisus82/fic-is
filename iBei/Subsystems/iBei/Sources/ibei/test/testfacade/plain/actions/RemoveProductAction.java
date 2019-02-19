package ibei.test.testfacade.plain.actions;

import ibei.model.product.dao.SQLProductDAO;
import ibei.model.product.dao.SQLProductDAOFactory;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class RemoveProductAction implements TransactionalPlainAction {

        private Long productID;

        public RemoveProductAction(Long productID) {
                this.productID = productID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLProductDAO dao = SQLProductDAOFactory.getDAO();
                dao.remove(connection, productID);

                return null;

        }

}
