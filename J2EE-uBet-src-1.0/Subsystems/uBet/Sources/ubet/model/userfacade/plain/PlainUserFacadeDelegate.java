package ubet.model.userfacade.plain;

import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import ubet.model.account.to.AccountTO;
import ubet.model.bet.to.BetTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.exceptions.BetOutOfTimeException;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;
import ubet.model.userfacade.plain.actions.AddToAccountAction;
import ubet.model.userfacade.plain.actions.BetAction;
import ubet.model.userfacade.plain.actions.ChangePasswordAction;
import ubet.model.userfacade.plain.actions.CreateAccountAction;
import ubet.model.userfacade.plain.actions.FindAccountAction;
import ubet.model.userfacade.plain.actions.FindAccountOperationsAction;
import ubet.model.userfacade.plain.actions.FindAccountsByUserAction;
import ubet.model.userfacade.plain.actions.FindBetsByAccountAction;
import ubet.model.userfacade.plain.actions.FindFavoritesAction;
import ubet.model.userfacade.plain.actions.FindUserAction;
import ubet.model.userfacade.plain.actions.LoginAction;
import ubet.model.userfacade.plain.actions.RegisterUserAction;
import ubet.model.userfacade.plain.actions.UpdateAccountDetailsAction;
import ubet.model.userfacade.plain.actions.UpdateUserDetailsAction;
import ubet.model.userfacade.plain.actions.WithdrawFromAccountAction;
import ubet.model.userfacade.to.AccountChunkTO;
import ubet.model.userfacade.to.AccountOperationChunkTO;
import ubet.model.userfacade.to.BetChunkTO;
import ubet.model.userfacade.to.LoginResultTO;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.PlainActionProcessor;

public class PlainUserFacadeDelegate implements UserFacadeDelegate {

        private String login;

        public PlainUserFacadeDelegate() {
                this.login = null;
        }

        private DataSource getDataSource() throws InternalErrorException {
                return DataSourceLocator
                                .getDataSource(GlobalNames.UBET_DATA_SOURCE);
        }

        public AccountTO registerUser(UserProfileTO userTO,
                        String creditCardNumber, Calendar expirationDate,
                        Double balance) throws DuplicateInstanceException,
                        InternalErrorException {
                try {

                        RegisterUserAction action = new RegisterUserAction(
                                        userTO, creditCardNumber,
                                        expirationDate, balance);

                        login = userTO.getLogin();
                        return (AccountTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (DuplicateInstanceException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public LoginResultTO login(String login, String password,
                        boolean passwordIsEncrypted)
                        throws InstanceNotFoundException,
                        IncorrectPasswordException, InternalErrorException {
                try {

                        LoginAction action = new LoginAction(login, password,
                                        passwordIsEncrypted);

                        LoginResultTO result = (LoginResultTO) PlainActionProcessor
                                        .process(getDataSource(), action);
                        this.login = login;
                        return result;

                } catch (IncorrectPasswordException e) {
                        throw e;
                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void updateUserDetails(UserProfileTO userTO)
                        throws InternalErrorException,
                        DuplicateInstanceException {
                try {

                        UpdateUserDetailsAction action = new UpdateUserDetailsAction(
                                        login, userTO);

                        PlainActionProcessor.process(getDataSource(), action);
                        this.login = userTO.getLogin();

                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (DuplicateInstanceException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void changePassword(String oldClearPassword,
                        String newClearPassword)
                        throws IncorrectPasswordException,
                        InternalErrorException {
                try {

                        ChangePasswordAction action = new ChangePasswordAction(
                                        login, oldClearPassword,
                                        newClearPassword);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (IncorrectPasswordException e) {
                        throw e;
                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public BetTO bet(BetTO betTO) throws InternalErrorException,
                        BetOutOfTimeException, InsufficientBalanceException {
                try {

                        BetAction action = new BetAction(betTO);

                        return (BetTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (BetOutOfTimeException e) {
                        throw e;
                } catch (InsufficientBalanceException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public UserProfileTO findUser() throws InternalErrorException {
                try {

                        FindUserAction action = new FindUserAction(login);

                        return (UserProfileTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public AccountTO createAccount(AccountTO accountTO)
                        throws InternalErrorException {
                try {

                        CreateAccountAction action = new CreateAccountAction(
                                        accountTO);

                        return (AccountTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void updateAccountDetails(AccountTO accountTO)
                        throws InternalErrorException,
                        InstanceNotFoundException {
                try {

                        UpdateAccountDetailsAction action = new UpdateAccountDetailsAction(
                                        accountTO);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public AccountTO findAccount(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        FindAccountAction action = new FindAccountAction(
                                        accountID);

                        return (AccountTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public AccountOperationChunkTO findAccountOperations(Long accountID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        FindAccountOperationsAction action = new FindAccountOperationsAction(
                                        accountID, startIndex, count,
                                        startDate, endDate);

                        return (AccountOperationChunkTO) PlainActionProcessor
                                        .process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public BetChunkTO findBetsByAccount(Long accountID, int startIndex,
                        int count, Calendar startDate, Calendar endDate)
                        throws InternalErrorException {
                try {

                        FindBetsByAccountAction action = new FindBetsByAccountAction(
                                        accountID, startIndex, count,
                                        startDate, endDate);

                        return (BetChunkTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        e.printStackTrace();
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public AccountChunkTO findAccountsByUser(int startIndex, int count)
                        throws InternalErrorException {
                try {

                        FindAccountsByUserAction action = new FindAccountsByUserAction(
                                        login, startIndex, count);

                        return (AccountChunkTO) PlainActionProcessor.process(
                                        getDataSource(), action);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void addToAccount(Long accountID, Double amount)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        AddToAccountAction action = new AddToAccountAction(
                                        accountID, amount);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void withdrawFromAccount(Long accountID, Double amount)
                        throws InstanceNotFoundException,
                        InsufficientBalanceException, InternalErrorException {
                try {

                        WithdrawFromAccountAction action = new WithdrawFromAccountAction(
                                        accountID, amount);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InsufficientBalanceException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<String> findFavorites(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        FindFavoritesAction action = new FindFavoritesAction(
                                        accountID);

                        return (List<String>) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
