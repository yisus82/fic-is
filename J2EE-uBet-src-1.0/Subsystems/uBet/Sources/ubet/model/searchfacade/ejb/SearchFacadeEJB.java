package ubet.model.searchfacade.ejb;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ubet.model.betoption.to.BetOptionTO;
import ubet.model.category.to.CategoryTO;
import ubet.model.country.to.CountryTO;
import ubet.model.event.entity.Event;
import ubet.model.event.to.EventTO;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.ejb.actions.FindAllHighlightedEventsAction;
import ubet.model.searchfacade.ejb.actions.FindBetOptionsAction;
import ubet.model.searchfacade.ejb.actions.FindBetTypesByEventAction;
import ubet.model.searchfacade.ejb.actions.FindEventsByCategoryAction;
import ubet.model.searchfacade.ejb.actions.FindHighlightedEventsAction;
import ubet.model.searchfacade.ejb.actions.FindRecentEventsAction;
import ubet.model.searchfacade.ejb.actions.FindWinnerOptionsAction;
import ubet.model.searchfacade.ejb.actions.GetAllCategoriesAction;
import ubet.model.searchfacade.ejb.actions.GetAllCategoryQuestionsAction;
import ubet.model.searchfacade.ejb.actions.GetAllCountriesAction;
import ubet.model.searchfacade.ejb.actions.GetAllQuestionsAction;
import ubet.model.searchfacade.ejb.actions.UpdateHighlightedEventsAction;
import ubet.model.searchfacade.to.BetTypeChunkTO;
import ubet.model.searchfacade.to.EventChunkTO;
import ubet.model.util.GlobalNames;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

@Stateless
public class SearchFacadeEJB implements LocalSearchFacade, RemoteSearchFacade {

        @PersistenceContext(unitName = GlobalNames.PERSISTENCE_UNIT)
        private EntityManager entityManager;

        public SearchFacadeEJB() {
        }

        public BetTypeChunkTO findBetTypesByEvent(Long eventID, int startIndex,
                        int count) throws InternalErrorException {
                FindBetTypesByEventAction action = new FindBetTypesByEventAction(
                                entityManager, eventID, startIndex, count);

                return action.execute();
        }

        public EventChunkTO findEventsByCategory(String categoryID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException {
                FindEventsByCategoryAction action = new FindEventsByCategoryAction(
                                entityManager, categoryID, startIndex, count,
                                startDate, endDate);

                return action.execute();
        }

        public EventTO findEvent(Long eventID) throws InternalErrorException {
                Event event = SearchFacadeHelper.findEvent(entityManager,
                                eventID);

                return SearchFacadeHelper.toEventTO(event);
        }

        public List<BetOptionTO> findBetOptions(Long betTypeID, int startIndex,
                        int count) throws InternalErrorException {
                FindBetOptionsAction action = new FindBetOptionsAction(
                                entityManager, betTypeID, startIndex, count);

                return action.execute();
        }

        public List<BetOptionTO> findWinnerOptions(Long betTypeID)
                        throws InternalErrorException {
                FindWinnerOptionsAction action = new FindWinnerOptionsAction(
                                entityManager, betTypeID);

                return action.execute();
        }

        public EventChunkTO findAllHighlightedEvents()
                        throws InternalErrorException {
                FindAllHighlightedEventsAction action = new FindAllHighlightedEventsAction(
                                entityManager);

                return action.execute();
        }

        public EventChunkTO findHighlightedEvents()
                        throws InternalErrorException {
                FindHighlightedEventsAction action = new FindHighlightedEventsAction(
                                entityManager);

                return action.execute();
        }

        public void updateHighlightedEvents() throws InternalErrorException {
                UpdateHighlightedEventsAction action = new UpdateHighlightedEventsAction(
                                entityManager);

                action.execute();
        }

        public List<CountryTO> getAllCountries() throws InternalErrorException {
                GetAllCountriesAction action = new GetAllCountriesAction(
                                entityManager);

                return action.execute();
        }

        public List<CategoryTO> getAllCategories()
                        throws InternalErrorException {
                GetAllCategoriesAction action = new GetAllCategoriesAction(
                                entityManager);

                return action.execute();
        }

        public List<QuestionTO> getAllQuestions() throws InternalErrorException {
                GetAllQuestionsAction action = new GetAllQuestionsAction(
                                entityManager);

                return action.execute();
        }

        public Map<String, List<QuestionTO>> getAllCategoryQuestions()
                        throws InternalErrorException {
                GetAllCategoryQuestionsAction action = new GetAllCategoryQuestionsAction(
                                entityManager);

                return action.execute();
        }

        public EventChunkTO findRecentEvents(List<String> categoryIDs,
                        int startIndex, int count)
                        throws InternalErrorException {
                FindRecentEventsAction action = new FindRecentEventsAction(
                                entityManager, categoryIDs, startIndex, count);

                return action.execute();
        }

}
