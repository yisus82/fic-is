package ubet.test.testfacade.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ubet.model.account.entity.Account;
import ubet.model.account.to.AccountTO;
import ubet.model.bet.entity.Bet;
import ubet.model.bet.to.BetTO;
import ubet.model.bettype.entity.BetType;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.entity.Event;
import ubet.model.event.to.EventTO;
import ubet.model.userprofile.entity.UserProfile;
import ubet.model.userprofile.to.UserProfileTO;
import ubet.model.util.GlobalNames;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

@Stateless
public class TestFacadeEJB implements LocalTestFacade, RemoteTestFacade {

        @PersistenceContext(unitName = GlobalNames.PERSISTENCE_UNIT)
        private EntityManager entityManager;

        public TestFacadeEJB() {
        }

        public void removeUser(String login) throws InstanceNotFoundException,
                        InternalErrorException {
                UserProfile user = TestFacadeHelper.findUser(entityManager,
                                login);

                if (user == null)
                        throw new InstanceNotFoundException(login,
                                        UserProfileTO.class.getName());

                entityManager.remove(user);
        }

        public void removeBetType(Long betTypeID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                BetType betType = TestFacadeHelper.findBetType(entityManager,
                                betTypeID);

                if (betType == null)
                        throw new InstanceNotFoundException(betTypeID,
                                        BetTypeTO.class.getName());

                entityManager.remove(betType);
        }

        public void removeEvent(Long eventID) throws InstanceNotFoundException,
                        InternalErrorException {
                Event event = TestFacadeHelper
                                .findEvent(entityManager, eventID);

                if (event == null)
                        throw new InstanceNotFoundException(eventID,
                                        EventTO.class.getName());

                entityManager.remove(event);
        }

        public void removeBet(Long betID) throws InstanceNotFoundException,
                        InternalErrorException {
                Bet bet = TestFacadeHelper.findBet(entityManager, betID);

                if (bet == null)
                        throw new InstanceNotFoundException(betID, BetTO.class
                                        .getName());

                entityManager.remove(bet);
        }

        public void removeAccount(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                Account account = TestFacadeHelper.findAccount(entityManager,
                                accountID);

                if (account == null)
                        throw new InstanceNotFoundException(accountID,
                                        AccountTO.class.getName());

                entityManager.remove(account);
        }

}
