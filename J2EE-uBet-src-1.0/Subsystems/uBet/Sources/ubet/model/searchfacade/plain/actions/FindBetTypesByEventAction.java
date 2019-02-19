package ubet.model.searchfacade.plain.actions;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import ubet.http.controller.caches.QuestionCache;
import ubet.model.betoption.dao.SQLBetOptionDAO;
import ubet.model.betoption.dao.SQLBetOptionDAOFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.dao.SQLBetTypeDAO;
import ubet.model.bettype.dao.SQLBetTypeDAOFactory;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.to.BetTypeChunkTO;
import ubet.model.searchfacade.to.BetTypeResultTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindBetTypesByEventAction implements NonTransactionalPlainAction {

        private Long eventID;

        private int startIndex;

        private int count;

        public FindBetTypesByEventAction(Long eventID, int startIndex, int count) {
                this.eventID = eventID;
                this.startIndex = startIndex;
                this.count = count;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                /*
                 * Find count+1 bet types to determine if there exist more bet
                 * types above the specified range.
                 */
                SQLBetTypeDAO betTypeDAO = SQLBetTypeDAOFactory.getDAO();

                List<BetTypeTO> betTypes = betTypeDAO.findByEvent(connection,
                                eventID, startIndex, count + 1);

                boolean existMoreBetTypes = ((betTypes.size() == (count + 1)) && !betTypes
                                .isEmpty());

                /*
                 * Remove the last bet type from the returned list if there
                 * exist more bet types above the specified range.
                 */
                if (existMoreBetTypes)
                        betTypes.remove(betTypes.size() - 1);

                List<BetTypeResultTO> betTypeResults = new ArrayList<BetTypeResultTO>();
                SQLBetOptionDAO betOptionDAO = SQLBetOptionDAOFactory.getDAO();

                for (BetTypeTO type : betTypes) {
                        List<BetOptionTO> options = betOptionDAO
                                        .findByBetType(connection, type
                                                        .getBetTypeID(), -1, -1);
                        QuestionTO question = QuestionCache.getQuestion(type
                                        .getQuestionID());
                        BetTypeResultTO betTypeResultTO = new BetTypeResultTO(
                                        type.getBetTypeID(), type.getEventID(),
                                        question, options);
                        betTypeResults.add(betTypeResultTO);
                }

                return new BetTypeChunkTO(betTypeResults, existMoreBetTypes);
        }

}
