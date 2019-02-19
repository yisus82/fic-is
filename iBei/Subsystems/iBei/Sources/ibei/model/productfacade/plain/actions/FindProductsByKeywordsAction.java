package ibei.model.productfacade.plain.actions;

import ibei.model.product.dao.SQLProductDAO;
import ibei.model.product.dao.SQLProductDAOFactory;

import java.sql.Connection;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindProductsByKeywordsAction implements
                NonTransactionalPlainAction {

        private Vector<String> keywords;

        private int startIndex;

        private int count;

        public FindProductsByKeywordsAction(Vector<String> keywords,
                        int startIndex, int count) {
                this.keywords = keywords;
                this.startIndex = startIndex;
                this.count = count;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLProductDAO productDAO = SQLProductDAOFactory.getDAO();

                return productDAO.findByKeywords(connection, keywords,
                                startIndex, count);

        }

}
