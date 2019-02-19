package ubet.test.testfacade.plain.actions;

import java.sql.Connection;

import ubet.model.userprofile.dao.SQLUserProfileDAO;
import ubet.model.userprofile.dao.SQLUserProfileDAOFactory;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class RemoveUserAction implements TransactionalPlainAction {

        private String login;

        public RemoveUserAction(String login) {
                this.login = login;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLUserProfileDAO userDAO = SQLUserProfileDAOFactory.getDAO();

                userDAO.remove(connection, login);

                return null;
        }

}
