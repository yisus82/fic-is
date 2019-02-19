package ubet.model.userfacade.ejb;

import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ubet.model.account.entity.Account;
import ubet.model.account.to.AccountTO;
import ubet.model.bet.to.BetTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;
import ubet.model.userfacade.ejb.actions.AddToAccountAction;
import ubet.model.userfacade.ejb.actions.BetAction;
import ubet.model.userfacade.ejb.actions.ChangePasswordAction;
import ubet.model.userfacade.ejb.actions.FindAccountOperationsAction;
import ubet.model.userfacade.ejb.actions.FindAccountsByUserAction;
import ubet.model.userfacade.ejb.actions.FindBetsByAccountAction;
import ubet.model.userfacade.ejb.actions.FindFavoritesAction;
import ubet.model.userfacade.ejb.actions.LoginAction;
import ubet.model.userfacade.ejb.actions.RegisterUserAction;
import ubet.model.userfacade.ejb.actions.WithdrawFromAccountAction;
import ubet.model.userfacade.exceptions.BetOutOfTimeException;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;
import ubet.model.userfacade.to.AccountChunkTO;
import ubet.model.userfacade.to.AccountOperationChunkTO;
import ubet.model.userfacade.to.BetChunkTO;
import ubet.model.userfacade.to.LoginResultTO;
import ubet.model.userprofile.entity.UserProfile;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;

@Stateful
public class UserFacadeEJB implements LocalUserFacade, RemoteUserFacade {

        private String login;

        @PersistenceContext(unitName = GlobalNames.PERSISTENCE_UNIT)
        private EntityManager entityManager;

        public UserFacadeEJB() {
        }

        public AccountTO registerUser(UserProfileTO userTO,
                        String creditCardNumber, Calendar expirationDate,
                        Double balance) throws DuplicateInstanceException {
                RegisterUserAction action = new RegisterUserAction(
                                entityManager, userTO, creditCardNumber,
                                expirationDate, balance);

                this.login = userTO.getLogin();

                return (AccountTO) action.execute();
        }

        public LoginResultTO login(String login, String password,
                        boolean passwordIsEncrypted)
                        throws InstanceNotFoundException,
                        IncorrectPasswordException {
                LoginAction action = new LoginAction(entityManager, login,
                                password, passwordIsEncrypted);
                LoginResultTO loginResultTO = action.execute();

                this.login = login;

                return loginResultTO;
        }

        public UserProfileTO findUser() {
                UserProfile userProfile = UserFacadeHelper.getUserProfile(
                                entityManager, login);

                return UserFacadeHelper.toUserProfileTO(userProfile);
        }

        public void updateUserDetails(UserProfileTO userTO)
                        throws DuplicateInstanceException {
                if ((!login.equals(userTO.getLogin()))
                                && (UserFacadeHelper.getUserProfile(
                                                entityManager, userTO
                                                                .getLogin()) != null))
                        throw new DuplicateInstanceException(userTO.getLogin(),
                                        UserProfileTO.class.getName());

                UserProfile userProfile = UserFacadeHelper.getUserProfile(
                                entityManager, login);

                UserProfileDetailsTO details = userTO.getUserDetails();
                // userProfile.setLogin(userTO.getLogin());
                userProfile.setFirstName(details.getFirstName());
                userProfile.setSurname(details.getSurname());
                userProfile.setEmail(details.getEmail());
                userProfile.setCountry(SearchFacadeHelper.findCountry(
                                entityManager, details.getCountryID()));

                this.login = userProfile.getLogin();
        }

        public void changePassword(String oldClearPassword,
                        String newClearPassword)
                        throws IncorrectPasswordException {
                ChangePasswordAction action = new ChangePasswordAction(
                                entityManager, login, oldClearPassword,
                                newClearPassword);

                action.execute();
        }

        public BetTO bet(BetTO betTO) throws BetOutOfTimeException,
                        InsufficientBalanceException {
                BetAction action = new BetAction(entityManager, betTO);

                return action.execute();
        }

        public AccountTO createAccount(AccountTO accountTO) {
                Account account = UserFacadeHelper.toAccount(entityManager,
                                accountTO);

                entityManager.persist(account);

                return UserFacadeHelper.toAccountTO(account);
        }

        public void updateAccountDetails(AccountTO accountTO)
                        throws InstanceNotFoundException {
                Account account = UserFacadeHelper.findAccount(entityManager,
                                accountTO.getAccountID());

                account.setBalance(accountTO.getBalance());
                account.setCreditCardNumber(accountTO.getCreditCardNumber());
                account.setExpirationDate(accountTO.getExpirationDate());
                account.setUser(UserFacadeHelper.getUserProfile(entityManager,
                                login));
        }

        public AccountTO findAccount(Long accountID)
                        throws InstanceNotFoundException {
                Account account = UserFacadeHelper.findAccount(entityManager,
                                accountID);

                return UserFacadeHelper.toAccountTO(account);
        }

        public AccountOperationChunkTO findAccountOperations(Long accountID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InstanceNotFoundException {
                FindAccountOperationsAction action = new FindAccountOperationsAction(
                                entityManager, accountID, startIndex, count,
                                startDate, endDate);

                return action.execute();
        }

        public BetChunkTO findBetsByAccount(Long accountID, int startIndex,
                        int count, Calendar startDate, Calendar endDate) {
                FindBetsByAccountAction action = new FindBetsByAccountAction(
                                entityManager, accountID, startIndex, count,
                                startDate, endDate);

                return action.execute();
        }

        public AccountChunkTO findAccountsByUser(int startIndex, int count) {
                FindAccountsByUserAction action = new FindAccountsByUserAction(
                                entityManager, login, startIndex, count);

                return action.execute();
        }

        public void addToAccount(Long accountID, Double amount)
                        throws InstanceNotFoundException {
                AddToAccountAction action = new AddToAccountAction(
                                entityManager, accountID, amount);

                action.execute();
        }

        public void withdrawFromAccount(Long accountID, Double amount)
                        throws InstanceNotFoundException,
                        InsufficientBalanceException {
                WithdrawFromAccountAction action = new WithdrawFromAccountAction(
                                entityManager, accountID, amount);

                action.execute();
        }

        public List<String> findFavorites(Long accountID) {
                FindFavoritesAction action = new FindFavoritesAction(
                                entityManager, accountID);

                return action.execute();
        }

}
