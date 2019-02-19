package ibei.model.userfacade.plain.actions;

import ibei.model.product.dao.SQLProductDAO;
import ibei.model.product.dao.SQLProductDAOFactory;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindProductsByUserAction implements NonTransactionalPlainAction {

        private String login;

        private int startIndex;

        private int count;

        public FindProductsByUserAction(String login, int startIndex, int count) {
                this.login = login;
                this.startIndex = startIndex;
                this.count = count;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLProductDAO productDAO = SQLProductDAOFactory.getDAO();

                return productDAO.findByUser(connection, login, startIndex,
                                count);

        }

}
