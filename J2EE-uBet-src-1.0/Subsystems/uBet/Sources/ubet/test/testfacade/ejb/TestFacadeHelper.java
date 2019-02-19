package ubet.test.testfacade.ejb;

import javax.persistence.EntityManager;

import ubet.model.account.entity.Account;
import ubet.model.bet.entity.Bet;
import ubet.model.bettype.entity.BetType;
import ubet.model.event.entity.Event;
import ubet.model.userprofile.entity.UserProfile;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;

public class TestFacadeHelper {

        private TestFacadeHelper() {
        }

        public static UserProfile findUser(EntityManager entityManager,
                        String login) throws InstanceNotFoundException {
                UserProfile user = entityManager.find(UserProfile.class, login);

                if (user == null)
                        throw new InstanceNotFoundException(login,
                                        UserProfile.class.getName());

                return user;
        }

        public static BetType findBetType(EntityManager entityManager,
                        Long betTypeID) throws InstanceNotFoundException {
                BetType betType = entityManager.find(BetType.class, betTypeID);

                if (betType == null)
                        throw new InstanceNotFoundException(betTypeID,
                                        BetType.class.getName());

                return betType;

        }

        public static Event findEvent(EntityManager entityManager, Long eventID)
                        throws InstanceNotFoundException {
                Event event = entityManager.find(Event.class, eventID);

                if (event == null)
                        throw new InstanceNotFoundException(eventID,
                                        Event.class.getName());

                return event;
        }

        public static Bet findBet(EntityManager entityManager, Long betID)
                        throws InstanceNotFoundException {
                Bet bet = entityManager.find(Bet.class, betID);

                if (bet == null)
                        throw new InstanceNotFoundException(betID, Bet.class
                                        .getName());

                return bet;
        }

        public static Account findAccount(EntityManager entityManager,
                        Long accountID) throws InstanceNotFoundException {
                Account account = entityManager.find(Account.class, accountID);

                if (account == null)
                        throw new InstanceNotFoundException(accountID,
                                        Account.class.getName());

                return account;
        }

}
