package ibei.model.userfacade.plain.actions;

import ibei.model.userfacade.exceptions.IncorrectPasswordException;
import ibei.model.userfacade.to.LoginResultTO;
import ibei.model.userfacade.util.PasswordEncrypter;
import ibei.model.userprofile.dao.SQLUserProfileDAO;
import ibei.model.userprofile.dao.SQLUserProfileDAOFactory;
import ibei.model.userprofile.to.UserProfileTO;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class LoginAction implements NonTransactionalPlainAction {

        private String login;

        private String encryptedPassword;

        public LoginAction(String login, String password,
                        boolean passwordIsEncrypted) {

                this.login = login;

                if (passwordIsEncrypted) {
                        encryptedPassword = password;
                } else {
                        encryptedPassword = PasswordEncrypter.crypt(password);
                }

        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLUserProfileDAO userProfileDAO = SQLUserProfileDAOFactory
                                .getDAO();
                UserProfileTO userProfileTO = userProfileDAO.find(connection,
                                login);

                if (!userProfileTO.getPassword().equals(encryptedPassword))
                        throw new IncorrectPasswordException(login);

                return new LoginResultTO(login, encryptedPassword,
                                userProfileTO.getType(), userProfileTO
                                                .getUserProfileDetails()
                                                .getCountryID());

        }

}
