package ibei.model.userfacade.plain.actions;

import ibei.model.product.dao.SQLProductDAO;
import ibei.model.product.dao.SQLProductDAOFactory;
import ibei.model.product.to.ProductTO;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class AnnounceAction implements TransactionalPlainAction {

        ProductTO productTO;

        public AnnounceAction(ProductTO productTO) {
                this.productTO = productTO;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLProductDAO productDAO = SQLProductDAOFactory.getDAO();
                return productDAO.create(connection, productTO);

        }

}
