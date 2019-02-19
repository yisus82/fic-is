package ubet.model.userfacade.plain.actions;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ubet.model.bet.dao.SQLBetDAO;
import ubet.model.bet.dao.SQLBetDAOFactory;
import ubet.model.bet.to.BetTO;
import ubet.model.betoption.dao.SQLBetOptionDAO;
import ubet.model.betoption.dao.SQLBetOptionDAOFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.dao.SQLBetTypeDAO;
import ubet.model.bettype.dao.SQLBetTypeDAOFactory;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;
import ubet.model.event.to.EventTO;
import ubet.model.question.dao.SQLQuestionDAO;
import ubet.model.question.dao.SQLQuestionDAOFactory;
import ubet.model.question.to.QuestionTO;
import ubet.model.userfacade.to.BetChunkTO;
import ubet.model.userfacade.to.BetStatusTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindBetsByAccountAction implements NonTransactionalPlainAction {

        private Long accountID;

        private int startIndex;

        private int count;

        private Calendar startDate;

        private Calendar endDate;

        public FindBetsByAccountAction(Long accountID, int startIndex,
                        int count, Calendar startDate, Calendar endDate) {
                this.accountID = accountID;
                this.startIndex = startIndex;
                this.count = count;
                this.startDate = startDate;
                this.endDate = endDate;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                /*
                 * Find count+1 bets to determine if there exist more bets above
                 * the specified range.
                 */
                List<BetTO> bets;
                List<BetStatusTO> betStatusTOs = new ArrayList<BetStatusTO>();
                SQLBetDAO betDAO = SQLBetDAOFactory.getDAO();

                bets = betDAO.findByAccount(connection, accountID, startIndex,
                                count + 1, startDate, endDate);
                boolean existMoreBetStatus = (bets.size() == (count + 1) && !bets
                                .isEmpty());

                /*
                 * Remove the last bet from the returned list if there exist
                 * more bet above the specified range.
                 */
                if (existMoreBetStatus)
                        bets.remove(bets.size() - 1);

                List<Long> eventIDs = new ArrayList<Long>();
                List<Long> betTypeIDs = new ArrayList<Long>();
                List<Long> betOptionIDs = new ArrayList<Long>();

                for (BetTO bet : bets) {
                        eventIDs.add(bet.getEventID());
                        betTypeIDs.add(bet.getBetTypeID());
                        betOptionIDs.add(bet.getBetOptionID());
                }

                SQLBetOptionDAO betOptionDAO = SQLBetOptionDAOFactory.getDAO();
                Map<Long, BetOptionTO> betOptionTOs = betOptionDAO.findByIDs(
                                connection, betOptionIDs);

                Map<Long, List<BetOptionTO>> winnerOptions = betOptionDAO
                                .findWinnersByIDs(connection, betTypeIDs);

                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();
                Map<Long, EventTO> eventTOs = eventDAO.findByIDs(connection,
                                eventIDs);

                SQLBetTypeDAO betTypeDAO = SQLBetTypeDAOFactory.getDAO();
                Map<Long, BetTypeTO> betTypeTOs = betTypeDAO.findByIDs(
                                connection, betTypeIDs);

                List<Long> questionIDs = new ArrayList<Long>();

                for (BetTypeTO betTypeTO : betTypeTOs.values()) {
                        questionIDs.add(betTypeTO.getQuestionID());
                }

                SQLQuestionDAO questionDAO = SQLQuestionDAOFactory.getDAO();
                Map<Long, QuestionTO> questionTOs = questionDAO.findByIDs(
                                connection, questionIDs);

                for (BetTO bet : bets) {
                        EventTO eventTO = eventTOs.get(bet.getEventID());
                        BetTypeTO betTypeTO = betTypeTOs
                                        .get(bet.getBetTypeID());
                        QuestionTO questionTO = questionTOs.get(betTypeTO
                                        .getQuestionID());
                        BetOptionTO betOptionTO = betOptionTOs.get(bet
                                        .getBetOptionID());
                        List<BetOptionTO> winners = winnerOptions.get(bet
                                        .getBetTypeID());
                        List<String> descriptions = new ArrayList<String>();
                        for (BetOptionTO winner : winners) {
                                descriptions.add(winner.getDescription());
                        }
                        betStatusTOs.add(new BetStatusTO(bet.getBetID(), bet
                                        .getDate(), eventTO.getDescription(),
                                        eventTO.getDate(), questionTO
                                                        .getDescription(),
                                        betOptionTO.getDescription(),
                                        betOptionTO.getOdds(), descriptions,
                                        bet.getStatus()));
                }

                return new BetChunkTO(betStatusTOs, existMoreBetStatus);
        }

}
