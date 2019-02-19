package ubet.model.userfacade.plain.actions;

import java.sql.Connection;

import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.util.PasswordEncrypter;
import ubet.model.userprofile.dao.SQLUserProfileDAO;
import ubet.model.userprofile.dao.SQLUserProfileDAOFactory;
import ubet.model.userprofile.to.UserProfileTO;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class ChangePasswordAction implements TransactionalPlainAction {

        private String login;

        private String oldEncryptedPassword;

        private String newEncryptedPassword;

        public ChangePasswordAction(String login, String oldClearPassword,
                        String newClearPassword) {
                this.login = login;
                this.oldEncryptedPassword = PasswordEncrypter
                                .crypt(oldClearPassword);
                this.newEncryptedPassword = PasswordEncrypter
                                .crypt(newClearPassword);
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLUserProfileDAO userDAO = SQLUserProfileDAOFactory.getDAO();
                UserProfileTO userTO = userDAO.find(connection, login);

                if (!userTO.getPassword().equals(oldEncryptedPassword)) {
                        throw new IncorrectPasswordException(login);
                }

                userTO.setPassword(newEncryptedPassword);
                userDAO.update(connection, login, userTO);

                return null;
        }

}
