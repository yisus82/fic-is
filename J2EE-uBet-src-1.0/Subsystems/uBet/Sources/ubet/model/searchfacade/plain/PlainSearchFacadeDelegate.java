package ubet.model.searchfacade.plain;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import ubet.model.betoption.to.BetOptionTO;
import ubet.model.category.dao.SQLCategoryDAO;
import ubet.model.category.dao.SQLCategoryDAOFactory;
import ubet.model.category.to.CategoryTO;
import ubet.model.categoryquestion.dao.SQLCategoryQuestionDAO;
import ubet.model.categoryquestion.dao.SQLCategoryQuestionDAOFactory;
import ubet.model.country.dao.SQLCountryDAO;
import ubet.model.country.dao.SQLCountryDAOFactory;
import ubet.model.country.to.CountryTO;
import ubet.model.event.to.EventTO;
import ubet.model.question.dao.SQLQuestionDAO;
import ubet.model.question.dao.SQLQuestionDAOFactory;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.plain.actions.FindAllHighlightedEventsAction;
import ubet.model.searchfacade.plain.actions.FindBetOptionsAction;
import ubet.model.searchfacade.plain.actions.FindBetTypesByEventAction;
import ubet.model.searchfacade.plain.actions.FindEventAction;
import ubet.model.searchfacade.plain.actions.FindEventsByCategoryAction;
import ubet.model.searchfacade.plain.actions.FindHighlightedEventsAction;
import ubet.model.searchfacade.plain.actions.FindRecentEventsAction;
import ubet.model.searchfacade.plain.actions.FindWinnerOptionsAction;
import ubet.model.searchfacade.plain.actions.UpdateHighlightedEventsAction;
import ubet.model.searchfacade.to.BetTypeChunkTO;
import ubet.model.searchfacade.to.EventChunkTO;
import ubet.model.util.GlobalNames;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.PlainActionProcessor;

public class PlainSearchFacadeDelegate implements SearchFacadeDelegate {

        private DataSource getDataSource() throws InternalErrorException {
                return DataSourceLocator
                                .getDataSource(GlobalNames.UBET_DATA_SOURCE);
        }

        public BetTypeChunkTO findBetTypesByEvent(Long eventID, int startIndex,
                        int count) throws InternalErrorException {
                try {

                        FindBetTypesByEventAction action = new FindBetTypesByEventAction(
                                        eventID, startIndex, count);

                        return (BetTypeChunkTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventChunkTO findEventsByCategory(String categoryID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException {
                try {

                        FindEventsByCategoryAction action = new FindEventsByCategoryAction(
                                        categoryID, startIndex, count,
                                        startDate, endDate);

                        return (EventChunkTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventTO findEvent(Long eventID) throws InternalErrorException {
                try {

                        FindEventAction action = new FindEventAction(eventID);

                        return (EventTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<BetOptionTO> findBetOptions(Long betTypeID, int startIndex,
                        int count) throws InternalErrorException {
                try {

                        FindBetOptionsAction action = new FindBetOptionsAction(
                                        betTypeID, startIndex, count);

                        return (List<BetOptionTO>) PlainActionProcessor
                                        .process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<BetOptionTO> findWinnerOptions(Long betTypeID)
                        throws InternalErrorException {
                try {

                        FindWinnerOptionsAction action = new FindWinnerOptionsAction(
                                        betTypeID);

                        return (List<BetOptionTO>) PlainActionProcessor
                                        .process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventChunkTO findAllHighlightedEvents()
                        throws InternalErrorException {
                try {

                        FindAllHighlightedEventsAction action = new FindAllHighlightedEventsAction();

                        return (EventChunkTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventChunkTO findHighlightedEvents()
                        throws InternalErrorException {
                try {

                        FindHighlightedEventsAction action = new FindHighlightedEventsAction();

                        return (EventChunkTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void updateHighlightedEvents() throws InternalErrorException {
                try {

                        UpdateHighlightedEventsAction action = new UpdateHighlightedEventsAction();

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<CountryTO> getAllCountries() throws InternalErrorException {
                try {
                        /* Applying fast-lane reader pattern. */
                        SQLCountryDAO dao = SQLCountryDAOFactory.getDAO();
                        return dao.getAll(getDataSource().getConnection());
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<CategoryTO> getAllCategories()
                        throws InternalErrorException {
                try {
                        /* Applying fast-lane reader pattern. */
                        SQLCategoryDAO dao = SQLCategoryDAOFactory.getDAO();
                        return dao.getAll(getDataSource().getConnection());
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<QuestionTO> getAllQuestions() throws InternalErrorException {
                try {
                        /* Applying fast-lane reader pattern. */
                        SQLQuestionDAO dao = SQLQuestionDAOFactory.getDAO();
                        return dao.getAll(getDataSource().getConnection());
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public Map<String, List<QuestionTO>> getAllCategoryQuestions()
                        throws InternalErrorException {
                try {
                        /* Applying fast-lane reader pattern. */
                        SQLCategoryQuestionDAO dao = SQLCategoryQuestionDAOFactory
                                        .getDAO();
                        return dao.getAll(getDataSource().getConnection());
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventChunkTO findRecentEvents(List<String> categoryIDs,
                        int startIndex, int count)
                        throws InternalErrorException {
                try {
                        FindRecentEventsAction action = new FindRecentEventsAction(
                                        categoryIDs, startIndex, count);

                        return (EventChunkTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
