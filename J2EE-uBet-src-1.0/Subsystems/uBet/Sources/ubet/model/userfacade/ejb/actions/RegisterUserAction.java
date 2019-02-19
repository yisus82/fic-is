package ubet.model.userfacade.ejb.actions;

import java.util.Calendar;

import javax.persistence.EntityManager;

import ubet.model.account.entity.Account;
import ubet.model.account.to.AccountTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;
import ubet.model.userfacade.ejb.UserFacadeHelper;
import ubet.model.userfacade.util.PasswordEncrypter;
import ubet.model.userprofile.entity.UserProfile;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;

public class RegisterUserAction {

        private EntityManager entityManager;

        private UserProfile userProfile;

        private String creditCardNumber;

        private Calendar expirationDate;

        private Double balance;

        public RegisterUserAction(EntityManager entityManager,
                        UserProfileTO userTO, String creditCardNumber,
                        Calendar expirationDate, Double balance) {
                this.entityManager = entityManager;
                String encryptedPassword = PasswordEncrypter.crypt(userTO
                                .getPassword());
                UserProfileDetailsTO details = userTO.getUserDetails();
                userProfile = new UserProfile(userTO.getLogin(),
                                encryptedPassword, details.getFirstName(),
                                details.getSurname(), details.getEmail(),
                                SearchFacadeHelper.findCountry(entityManager,
                                                details.getCountryID()));
                this.creditCardNumber = creditCardNumber;
                this.expirationDate = expirationDate;
                this.balance = balance;
        }

        public AccountTO execute() throws DuplicateInstanceException {
                if (entityManager.find(UserProfile.class, userProfile
                                .getLogin()) == null) {
                        entityManager.persist(userProfile);
                        Account account = new Account(userProfile,
                                        creditCardNumber, expirationDate,
                                        balance);
                        entityManager.persist(account);

                        return UserFacadeHelper.toAccountTO(account);
                }
                throw new DuplicateInstanceException(userProfile.getLogin(),
                                UserProfile.class.getName());
        }

}
