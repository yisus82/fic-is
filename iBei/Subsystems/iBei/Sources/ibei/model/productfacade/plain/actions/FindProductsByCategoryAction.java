package ibei.model.productfacade.plain.actions;

import ibei.model.product.dao.SQLProductDAO;
import ibei.model.product.dao.SQLProductDAOFactory;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindProductsByCategoryAction implements
                NonTransactionalPlainAction {

        private String categoryID;

        private int startIndex;

        private int count;

        public FindProductsByCategoryAction(String categoryID, int startIndex,
                        int count) {
                this.categoryID = categoryID;
                this.startIndex = startIndex;
                this.count = count;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLProductDAO productDAO = SQLProductDAOFactory.getDAO();

                return productDAO.findByCategory(connection, categoryID,
                                startIndex, count);

        }

}
