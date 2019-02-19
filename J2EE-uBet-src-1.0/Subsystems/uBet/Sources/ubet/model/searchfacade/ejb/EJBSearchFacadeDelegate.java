package ubet.model.searchfacade.ejb;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import ubet.model.betoption.to.BetOptionTO;
import ubet.model.category.to.CategoryTO;
import ubet.model.country.to.CountryTO;
import ubet.model.event.to.EventTO;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.to.BetTypeChunkTO;
import ubet.model.searchfacade.to.EventChunkTO;
import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.jndi.ServiceLocator;

public class EJBSearchFacadeDelegate implements SearchFacadeDelegate {

        private final static String SEARCH_FACADE_JNDI_NAME_PARAMETER = "EJBSearchFacadeDelegate/searchFacadeJNDIName";

        private static String searchFacadeJNDIName;

        private SearchFacade searchFacade;

        static {

                try {

                        /* Initialize "userFacadeJNDIName". */
                        searchFacadeJNDIName = ConfigurationParametersManager
                                        .getParameter(SEARCH_FACADE_JNDI_NAME_PARAMETER);

                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        public EJBSearchFacadeDelegate() throws InternalErrorException {
                try {

                        searchFacade = ServiceLocator.findService(
                                        SearchFacade.class,
                                        searchFacadeJNDIName);

                } catch (NamingException e) {
                        throw new InternalErrorException(e);
                }
        }

        public BetTypeChunkTO findBetTypesByEvent(Long eventID, int startIndex,
                        int count) throws InternalErrorException {
                try {
                        return searchFacade.findBetTypesByEvent(eventID,
                                        startIndex, count);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventChunkTO findEventsByCategory(String categoryID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException {
                try {
                        return searchFacade.findEventsByCategory(categoryID,
                                        startIndex, count, startDate, endDate);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventTO findEvent(Long eventID) throws InternalErrorException {
                try {
                        return searchFacade.findEvent(eventID);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<BetOptionTO> findBetOptions(Long betTypeID, int startIndex,
                        int count) throws InternalErrorException {
                try {
                        return searchFacade.findBetOptions(betTypeID,
                                        startIndex, count);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<BetOptionTO> findWinnerOptions(Long betTypeID)
                        throws InternalErrorException {
                try {
                        return searchFacade.findWinnerOptions(betTypeID);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventChunkTO findAllHighlightedEvents()
                        throws InternalErrorException {
                try {
                        return searchFacade.findAllHighlightedEvents();
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventChunkTO findHighlightedEvents()
                        throws InternalErrorException {
                try {
                        return searchFacade.findHighlightedEvents();
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void updateHighlightedEvents() throws InternalErrorException {
                try {
                        searchFacade.updateHighlightedEvents();
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<CountryTO> getAllCountries() throws InternalErrorException {
                try {
                        return searchFacade.getAllCountries();
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<CategoryTO> getAllCategories()
                        throws InternalErrorException {
                try {
                        return searchFacade.getAllCategories();
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public List<QuestionTO> getAllQuestions() throws InternalErrorException {
                try {
                        return searchFacade.getAllQuestions();
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public Map<String, List<QuestionTO>> getAllCategoryQuestions()
                        throws InternalErrorException {
                try {
                        return searchFacade.getAllCategoryQuestions();
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventChunkTO findRecentEvents(List<String> categoryIDs,
                        int startIndex, int count)
                        throws InternalErrorException {
                try {
                        return searchFacade.findRecentEvents(categoryIDs,
                                        startIndex, count);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

}
