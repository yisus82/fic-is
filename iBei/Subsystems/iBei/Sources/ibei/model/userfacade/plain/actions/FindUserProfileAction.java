package ibei.model.userfacade.plain.actions;

import ibei.model.userprofile.dao.SQLUserProfileDAO;
import ibei.model.userprofile.dao.SQLUserProfileDAOFactory;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindUserProfileAction implements NonTransactionalPlainAction {

        private String login;

        public FindUserProfileAction(String login) {
                this.login = login;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLUserProfileDAO userProfileDAO = SQLUserProfileDAOFactory
                                .getDAO();

                return userProfileDAO.find(connection, login);

        }

}
