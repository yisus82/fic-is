package ibei.model.userfacade.plain.actions;

import ibei.model.userfacade.exceptions.IncorrectPasswordException;
import ibei.model.userfacade.util.PasswordEncrypter;
import ibei.model.userprofile.dao.SQLUserProfileDAO;
import ibei.model.userprofile.dao.SQLUserProfileDAOFactory;
import ibei.model.userprofile.to.UserProfileTO;

import java.sql.Connection;

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

                SQLUserProfileDAO userProfileDAO = SQLUserProfileDAOFactory
                                .getDAO();
                UserProfileTO userProfileTO = userProfileDAO.find(connection,
                                login);

                if (!userProfileTO.getPassword().equals(oldEncryptedPassword)) {
                        throw new IncorrectPasswordException(login);
                }

                userProfileTO.setPassword(newEncryptedPassword);
                userProfileDAO.update(connection, login, userProfileTO);

                return null;

        }

}
