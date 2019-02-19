package ubet.model.userfacade.plain.actions;

import java.sql.Connection;

import ubet.model.userprofile.dao.SQLUserProfileDAO;
import ubet.model.userprofile.dao.SQLUserProfileDAOFactory;
import ubet.model.userprofile.to.UserProfileTO;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class UpdateUserDetailsAction implements TransactionalPlainAction {

        private String login;

        private UserProfileTO userTO;

        public UpdateUserDetailsAction(String login, UserProfileTO userTO) {
                this.login = login;
                this.userTO = userTO;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLUserProfileDAO userDAO = SQLUserProfileDAOFactory.getDAO();

                userDAO.update(connection, login, userTO);

                return null;
        }

}
