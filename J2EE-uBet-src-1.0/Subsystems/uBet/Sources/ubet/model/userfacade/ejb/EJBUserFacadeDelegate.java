package ubet.model.userfacade.ejb;

import java.util.Calendar;
import java.util.List;

import javax.naming.NamingException;

import ubet.model.account.to.AccountTO;
import ubet.model.bet.to.BetTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.exceptions.BetOutOfTimeException;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;
import ubet.model.userfacade.to.AccountChunkTO;
import ubet.model.userfacade.to.AccountOperationChunkTO;
import ubet.model.userfacade.to.BetChunkTO;
import ubet.model.userfacade.to.LoginResultTO;
import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.jndi.ServiceLocator;

public class EJBUserFacadeDelegate implements UserFacadeDelegate {

        private final static String USER_FACADE_JNDI_NAME_PARAMETER = "EJBUserFacadeDelegate/userFacadeJNDIName";

        private static String userFacadeJNDIName;

        private UserFacade userFacade;

        static {

                try {

                        /* Initialize "userFacadeJNDIName". */
                        userFacadeJNDIName = ConfigurationParametersManager
                                        .getParameter(USER_FACADE_JNDI_NAME_PARAMETER);

                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        public EJBUserFacadeDelegate() throws InternalErrorException {
                try {

                        userFacade = ServiceLocator.findService(
                                        UserFacade.class, userFacadeJNDIName);

                } catch (NamingException e) {
                        throw new InternalErrorException(e);
                }
        }

        public AccountTO registerUser(UserProfileTO userTO,
                        String creditCardNumber, Calendar expirationDate,
                        Double balance) throws DuplicateInstanceException,
                        InternalErrorException {
                try {
                        AccountTO accountTO = userFacade.registerUser(userTO,
                                        creditCardNumber, expirationDate,
                                        balance);

                        return accountTO;
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public LoginResultTO login(String login, String password,
                        boolean passwordIsEncrypted)
                        throws InstanceNotFoundException,
                        IncorrectPasswordException, InternalErrorException {
                try {
                        LoginResultTO loginResultTO = userFacade.login(login,
                                        password, passwordIsEncrypted);

                        return loginResultTO;
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public UserProfileTO findUser() throws InternalErrorException {
                try {
                        return userFacade.findUser();
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void updateUserDetails(UserProfileTO userProfileTO)
                        throws InternalErrorException,
                        DuplicateInstanceException {
                try {
                        userFacade.updateUserDetails(userProfileTO);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void changePassword(String oldClearPassword,
                        String newClearPassword)
                        throws IncorrectPasswordException,
                        InternalErrorException {
                try {
                        userFacade.changePassword(oldClearPassword,
                                        newClearPassword);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public BetTO bet(BetTO betTO) throws InternalErrorException,
                        BetOutOfTimeException, InsufficientBalanceException {
                try {
                        return userFacade.bet(betTO);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public AccountTO createAccount(AccountTO accountTO)
                        throws InternalErrorException {
                try {
                        return userFacade.createAccount(accountTO);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void updateAccountDetails(AccountTO accountTO)
                        throws InternalErrorException,
                        InstanceNotFoundException {
                try {
                        userFacade.updateAccountDetails(accountTO);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public AccountTO findAccount(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        return userFacade.findAccount(accountID);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public AccountOperationChunkTO findAccountOperations(Long accountID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        return userFacade.findAccountOperations(accountID,
                                        startIndex, count, startDate, endDate);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public BetChunkTO findBetsByAccount(Long accountID, int startIndex,
                        int count, Calendar startDate, Calendar endDate)
                        throws InternalErrorException {
                try {
                        return userFacade.findBetsByAccount(accountID,
                                        startIndex, count, startDate, endDate);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public AccountChunkTO findAccountsByUser(int startIndex, int count)
                        throws InternalErrorException {
                try {
                        return userFacade.findAccountsByUser(startIndex, count);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void addToAccount(Long accountID, Double amount)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        userFacade.addToAccount(accountID, amount);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void withdrawFromAccount(Long accountID, Double amount)
                        throws InstanceNotFoundException,
                        InsufficientBalanceException, InternalErrorException {
                try {
                        userFacade.withdrawFromAccount(accountID, amount);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<String> findFavorites(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        return userFacade.findFavorites(accountID);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

}
