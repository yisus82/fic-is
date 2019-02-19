package ubet.model.userfacade.ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJBException;
import javax.persistence.EntityManager;

import ubet.model.account.entity.Account;
import ubet.model.account.to.AccountTO;
import ubet.model.accountoperation.entity.AccountOperation;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.bet.entity.Bet;
import ubet.model.bet.to.BetTO;
import ubet.model.betoption.entity.BetOption;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.entity.BetType;
import ubet.model.country.entity.Country;
import ubet.model.event.entity.Event;
import ubet.model.userfacade.to.BetStatusTO;
import ubet.model.userprofile.entity.UserProfile;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;

public class UserFacadeHelper {

        private UserFacadeHelper() {
        }

        /**
         * 
         * @throws EJBException
         *                 if <code>UserProfile</code> does not exist for
         *                 <code>login</code> (thus the transaction will be
         *                 rollbacked)
         */
        public static UserProfile getUserProfile(EntityManager entityManager,
                        String login) {
                UserProfile userProfile = entityManager.find(UserProfile.class,
                                login);

                return userProfile;
        }

        public static UserProfileTO toUserProfileTO(UserProfile userProfile) {
                return new UserProfileTO(userProfile.getLogin(), userProfile
                                .getPassword(), new UserProfileDetailsTO(
                                userProfile.getFirstName(), userProfile
                                                .getSurname(), userProfile
                                                .getEmail(), userProfile
                                                .getCountry().getCountryID()));
        }

        public static UserProfile toUserProfile(EntityManager entityManager,
                        UserProfileTO userProfileTO) {
                UserProfileDetailsTO details = userProfileTO.getUserDetails();
                return new UserProfile(userProfileTO.getLogin(), userProfileTO
                                .getPassword(), details.getFirstName(), details
                                .getSurname(), details.getEmail(),
                                entityManager.find(Country.class, details
                                                .getCountryID()));
        }

        public static Account findAccount(EntityManager entityManager,
                        Long accountID) throws InstanceNotFoundException {

                Account account = entityManager.find(Account.class, accountID);

                if (account == null) {
                        throw new InstanceNotFoundException(accountID,
                                        Account.class.getName());
                }

                return account;
        }

        public static AccountTO toAccountTO(Account account) {
                return new AccountTO(account.getAccountID(), account.getUser()
                                .getLogin(), account.getCreditCardNumber(),
                                account.getExpirationDate(), account
                                                .getBalance());
        }

        public static Account toAccount(EntityManager entityManager,
                        AccountTO accountTO) {
                return new Account(entityManager.find(UserProfile.class,
                                accountTO.getUserID()), accountTO
                                .getCreditCardNumber(), accountTO
                                .getExpirationDate(), accountTO.getBalance());
        }

        public static List<AccountTO> toAccountTOs(List<Account> accounts) {
                List<AccountTO> accountTOs = new ArrayList<AccountTO>();

                for (Account account : accounts)
                        accountTOs.add(toAccountTO(account));

                return accountTOs;
        }

        public static AccountOperationTO toAccountOperationTO(
                        AccountOperation accountOperation) {
                if (accountOperation.getBet() == null)
                        return new AccountOperationTO(accountOperation
                                        .getAccountOperationID(),
                                        accountOperation.getAccount()
                                                        .getAccountID(),
                                        accountOperation.getAmount(),
                                        accountOperation.getType(),
                                        accountOperation.getDescription(),
                                        accountOperation.getDate(), null);
                return new AccountOperationTO(accountOperation
                                .getAccountOperationID(), accountOperation
                                .getAccount().getAccountID(), accountOperation
                                .getAmount(), accountOperation.getType(),
                                accountOperation.getDescription(),
                                accountOperation.getDate(), accountOperation
                                                .getBet().getBetID());
        }

        /**
         * 
         * @throws EJBException
         *                 if <code>Account</code> does not exist for
         *                 <code>accountID</code> (thus the transaction will
         *                 be rollbacked)
         */
        public static AccountOperation toAccountOperation(
                        EntityManager entityManager,
                        AccountOperationTO accountOperationTO) {
                try {
                        return new AccountOperation(
                                        findAccount(
                                                        entityManager,
                                                        accountOperationTO
                                                                        .getAccountID()),
                                        accountOperationTO.getAmount(),
                                        accountOperationTO.getType(),
                                        accountOperationTO.getDescription(),
                                        accountOperationTO.getDate(),
                                        entityManager
                                                        .find(
                                                                        Bet.class,
                                                                        accountOperationTO
                                                                                        .getBetID()));
                } catch (InstanceNotFoundException e) {
                        throw new EJBException(e);
                }
        }

        public static List<AccountOperationTO> toAccountOperationTOs(
                        List<AccountOperation> accountOperations) {
                List<AccountOperationTO> accountOperationTOs = new ArrayList<AccountOperationTO>();

                for (AccountOperation operation : accountOperations)
                        accountOperationTOs
                                        .add(toAccountOperationTO(operation));

                return accountOperationTOs;
        }

        public static void createAccountOperation(EntityManager entityManager,
                        Account account, Double amount, String type,
                        String description, Calendar date, Bet bet) {

                AccountOperation accountOperation = new AccountOperation(
                                account, amount, type, description, date, bet);

                entityManager.persist(accountOperation);

        }

        public static BetTO toBetTO(Bet bet) {
                return new BetTO(bet.getBetID(), bet.getBetType()
                                .getBetTypeID(), bet.getBetOption()
                                .getBetOptionID(), bet.getAccount()
                                .getAccountID(), bet.getEvent().getEventID(),
                                bet.getAmount(), bet.getDate(), bet.getStatus());
        }

        public static Bet toBet(EntityManager entityManager, BetTO betTO) {
                return new Bet(entityManager.find(BetType.class, betTO
                                .getBetTypeID()), entityManager.find(
                                BetOption.class, betTO.getBetOptionID()),
                                entityManager.find(Account.class, betTO
                                                .getAccountID()), entityManager
                                                .find(Event.class, betTO
                                                                .getEventID()),
                                betTO.getAmount(), betTO.getDate(), betTO
                                                .getStatus());
        }

        public static BetStatusTO toBetStatusTO(EntityManager entityManager,
                        Bet bet) {
                List<BetOption> winnerOptions = entityManager
                                .createQuery(
                                                "SELECT o FROM BetOption o "
                                                                + "WHERE o.betType.betTypeID = :betTypeID AND "
                                                                + "o.status = :status ORDER BY o.betOptionID")
                                .setParameter("betTypeID",
                                                bet.getBetType().getBetTypeID())
                                .setParameter("status", BetOptionTO.WINNER)
                                .getResultList();
                List<String> winners = new ArrayList<String>();
                for (BetOption option : winnerOptions)
                        winners.add(option.getDescription());
                return new BetStatusTO(bet.getBetID(), bet.getDate(), bet
                                .getEvent().getDescription(), bet.getEvent()
                                .getDate(), bet.getBetType().getQuestion()
                                .getDescription(), bet.getBetOption()
                                .getDescription(),
                                bet.getBetOption().getOdds(), winners, bet
                                                .getStatus());
        }

        public static List<BetStatusTO> toBetStatusTOs(
                        EntityManager entityManager, List<Bet> bets) {
                List<BetStatusTO> betStatusTOs = new ArrayList<BetStatusTO>();

                for (Bet bet : bets)
                        betStatusTOs.add(toBetStatusTO(entityManager, bet));

                return betStatusTOs;
        }

}
