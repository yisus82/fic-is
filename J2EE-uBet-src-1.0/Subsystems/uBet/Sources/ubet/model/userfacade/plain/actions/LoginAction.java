package ubet.model.userfacade.plain.actions;

import java.sql.Connection;

import ubet.model.userfacade.to.LoginResultTO;
import ubet.model.userfacade.util.PasswordEncrypter;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userprofile.dao.SQLUserProfileDAO;
import ubet.model.userprofile.dao.SQLUserProfileDAOFactory;
import ubet.model.userprofile.to.UserProfileTO;

public class LoginAction implements NonTransactionalPlainAction {

        private String login;

        private String encryptedPassword;

        public LoginAction(String login, String password,
                        boolean passwordIsEncrypted) {
                this.login = login;
                if (passwordIsEncrypted) {
                        this.encryptedPassword = password;
                } else {
                        this.encryptedPassword = PasswordEncrypter
                                        .crypt(password);
                }
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLUserProfileDAO userDAO = SQLUserProfileDAOFactory.getDAO();
                UserProfileTO userTO = userDAO.find(connection, login);

                if (!userTO.getPassword().equals(encryptedPassword))
                        throw new IncorrectPasswordException(login);

                return new LoginResultTO(login, encryptedPassword, userTO
                                .getUserDetails().getCountryID());
        }

}
