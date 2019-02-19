package ibei.model.productfacade.plain.actions;

import ibei.model.product.dao.SQLProductDAO;
import ibei.model.product.dao.SQLProductDAOFactory;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindProductAction implements NonTransactionalPlainAction {

        Long productID;

        public FindProductAction(Long productID) {
                this.productID = productID;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLProductDAO productDAO = SQLProductDAOFactory.getDAO();
                return productDAO.find(connection, productID);

        }

}
