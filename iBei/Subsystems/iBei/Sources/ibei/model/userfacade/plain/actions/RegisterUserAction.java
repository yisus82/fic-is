package ibei.model.userfacade.plain.actions;

import ibei.model.userfacade.util.PasswordEncrypter;
import ibei.model.userprofile.dao.SQLUserProfileDAO;
import ibei.model.userprofile.dao.SQLUserProfileDAOFactory;
import ibei.model.userprofile.to.UserProfileTO;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class RegisterUserAction implements TransactionalPlainAction {

        private UserProfileTO userProfileTO;

        public RegisterUserAction(UserProfileTO userProfileTO) {
                this.userProfileTO = userProfileTO;
                userProfileTO.setPassword(PasswordEncrypter.crypt(userProfileTO
                                .getPassword()));
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLUserProfileDAO userProfileDAO = SQLUserProfileDAOFactory
                                .getDAO();

                return userProfileDAO.create(connection, userProfileTO);

        }

}
