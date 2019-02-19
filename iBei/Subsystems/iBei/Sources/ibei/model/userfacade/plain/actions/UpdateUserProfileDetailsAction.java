package ibei.model.userfacade.plain.actions;

import ibei.model.userprofile.dao.SQLUserProfileDAO;
import ibei.model.userprofile.dao.SQLUserProfileDAOFactory;
import ibei.model.userprofile.to.UserProfileTO;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class UpdateUserProfileDetailsAction implements TransactionalPlainAction {

        private String login;

        private UserProfileTO userProfileTO;

        public UpdateUserProfileDetailsAction(String login,
                        UserProfileTO userProfileTO) {

                this.login = login;
                this.userProfileTO = userProfileTO;

        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLUserProfileDAO userProfileDAO = SQLUserProfileDAOFactory
                                .getDAO();
                return userProfileDAO.update(connection, login, userProfileTO);

        }

}
