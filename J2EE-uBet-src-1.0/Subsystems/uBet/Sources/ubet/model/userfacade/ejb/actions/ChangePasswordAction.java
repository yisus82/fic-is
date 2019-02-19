package ubet.model.userfacade.ejb.actions;

import javax.persistence.EntityManager;

import ubet.model.userfacade.ejb.UserFacadeHelper;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.util.PasswordEncrypter;
import ubet.model.userprofile.entity.UserProfile;

public class ChangePasswordAction {

        private EntityManager entityManager;

        private String login;

        private String oldEncryptedPassword;

        private String newEncryptedPassword;

        public ChangePasswordAction(EntityManager entityManager, String login,
                        String oldClearPassword, String newClearPassword) {
                this.entityManager = entityManager;
                this.login = login;
                this.oldEncryptedPassword = PasswordEncrypter
                                .crypt(oldClearPassword);
                this.newEncryptedPassword = PasswordEncrypter
                                .crypt(newClearPassword);
        }

        public void execute() throws IncorrectPasswordException {
                UserProfile userProfile = UserFacadeHelper.getUserProfile(
                                entityManager, login);

                if (!userProfile.getPassword().equals(oldEncryptedPassword)) {
                        throw new IncorrectPasswordException(userProfile
                                        .getLogin());
                }

                userProfile.setPassword(newEncryptedPassword);
        }

}
