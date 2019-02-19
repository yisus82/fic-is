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

public class FindHighlightedEventsAction implements NonTransactionalPlainAction {

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLEventDAO eventDAO = SQLEventDAOFactory.getDAO();

                List<EventTO> events = eventDAO.findHighlighted(connection);

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

                return new EventChunkTO(eventResults, false);
        }

}
