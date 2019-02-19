package ibei.test.testfacade.plain.actions;

import ibei.model.userprofile.dao.SQLUserProfileDAO;
import ibei.model.userprofile.dao.SQLUserProfileDAOFactory;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class RemoveUserProfileAction implements TransactionalPlainAction {

        private String login;

        public RemoveUserProfileAction(String login) {
                this.login = login;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLUserProfileDAO dao = SQLUserProfileDAOFactory.getDAO();
                dao.remove(connection, login);

                return null;

        }

}
