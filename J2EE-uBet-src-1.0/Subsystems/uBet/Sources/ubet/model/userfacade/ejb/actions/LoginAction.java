package ubet.model.userfacade.ejb.actions;

import javax.persistence.EntityManager;

import ubet.model.userfacade.ejb.UserFacadeHelper;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.to.LoginResultTO;
import ubet.model.userfacade.util.PasswordEncrypter;
import ubet.model.userprofile.entity.UserProfile;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;

public class LoginAction {

        private EntityManager entityManager;

        private String login;

        private String encryptedPassword;

        public LoginAction(EntityManager entityManager, String login,
                        String password, boolean passwordIsEncrypted) {
                this.entityManager = entityManager;
                this.login = login;

                if (passwordIsEncrypted) {
                        encryptedPassword = password;
                } else {
                        encryptedPassword = PasswordEncrypter.crypt(password);
                }
        }

        public LoginResultTO execute() throws IncorrectPasswordException,
                        InstanceNotFoundException {
                UserProfile userProfile = UserFacadeHelper.getUserProfile(
                                entityManager, login);

                if (userProfile == null) {
                        throw new InstanceNotFoundException(login,
                                        UserProfile.class.getName());
                }

                if (!userProfile.getPassword().equals(encryptedPassword)) {
                        throw new IncorrectPasswordException(login);
                }

                return new LoginResultTO(userProfile.getLogin(), userProfile
                                .getPassword(), userProfile.getCountry()
                                .getCountryID());
        }

}
