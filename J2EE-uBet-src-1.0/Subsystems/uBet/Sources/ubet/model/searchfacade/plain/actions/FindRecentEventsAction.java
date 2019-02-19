package ubet.model.searchfacade.plain.actions;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import ubet.http.controller.caches.CategoryCache;
import ubet.http.controller.caches.QuestionCache;
import ubet.model.betoption.dao.SQLBetOptionDAO;
import ubet.model.betoption.dao.SQLBetOptionDAOFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.dao.SQLBetTypeDAO;
import ubet.model.bettype.dao.SQLBetTypeDAOFactory;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.dao.SQLEventDAO;
import ubet.model.event.dao.SQLEventDAOFactory;
import ubet.model.event.to.EventTO;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.to.BetTypeResultTO;
import ubet.model.searchfacade.to.CategoryChunkTO;
import ubet.model.searchfacade.to.EventChunkTO;
import ubet.model.searchfacade.to.EventResultTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.NonTransactionalPlainAction;

public class FindRecentEventsAction implements NonTransactionalPlainAction {

        private List<String> categoryIDs;

        private int startIndex;

        private int count;

        public FindRecentEventsAction(List<String> categoryIDs, int startIndex,
                        int count) {
                this.categoryIDs = categoryIDs;
                this.startIndex = startIndex;
                this.count = count;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                /*
                 * Find count+1 events to determine if there exist more events
                 * above the specified range.
                 */
                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();

                List<EventTO> events = eventDAO.findRecent(connection,
                                categoryIDs, startIndex, count + 1);

                boolean existMoreEvents = ((events.size() == (count + 1)) && !events
                                .isEmpty());

                /*
                 * Remove the last event from the returned list if there exist
                 * more events above the specified range.
                 */
                if (existMoreEvents)
                        events.remove(events.size() - 1);

                List<EventResultTO> eventResults = new ArrayList<EventResultTO>();
                SQLBetTypeDAO betTypeDAO = SQLBetTypeDAOFactory.getDAO();
                SQLBetOptionDAO betOptionDAO = SQLBetOptionDAOFactory.getDAO();

                for (EventTO event : events) {
                        BetTypeTO betType = betTypeDAO.find(connection, event
                                        .getBetTypeID());
                        QuestionTO question = QuestionCache.getQuestion(betType
                                        .getQuestionID());
                        List<BetOptionTO> options = betOptionDAO.findByBetType(
                                        connection, betType.getBetTypeID(), -1,
                                        -1);
                        BetTypeResultTO betTypeResult = new BetTypeResultTO(
                                        betType.getBetTypeID(), betType
                                                        .getEventID(),
                                        question, options);
                        CategoryChunkTO category = CategoryCache
                                        .getCategoryByIdentifier(event
                                                        .getCategoryID());
                        EventResultTO eventResultTO = new EventResultTO(event
                                        .getEventID(), event.getDescription(),
                                        event.getDate(), category,
                                        betTypeResult, event.getHighlighted());
                        eventResults.add(eventResultTO);
                }

                return new EventChunkTO(eventResults, existMoreEvents);
        }

}
