package ubet.model.searchfacade.delegate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ubet.model.betoption.to.BetOptionTO;
import ubet.model.category.to.CategoryTO;
import ubet.model.country.to.CountryTO;
import ubet.model.event.to.EventTO;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.to.BetTypeChunkTO;
import ubet.model.searchfacade.to.EventChunkTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A facade to model the searchs of the user within the portal.
 */
public interface SearchFacadeDelegate extends Serializable {

        /**
         * Returns a custom transfer object with a collection of
         * <code>BetTypeTO</code> of a given event. If the event has no bet
         * types, the returned collection has zero elements
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are equal
         * to -1 the collection returned has all of the bet types of the
         * specified event</li>
         * </ul>
         * 
         * @param eventID
         *                the event identifier
         * @param startIndex
         *                the index (starting from 1) of the first object to
         *                return
         * @param count
         *                the maximum number of objects to return
         * @return the custom transfer object with the collection of
         *         <code>BetTypeTO</code> with the data of the bet types of
         *         the specified event
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public BetTypeChunkTO findBetTypesByEvent(Long eventID, int startIndex,
                        int count) throws InternalErrorException;

        /**
         * Returns a custom transfer object with a collection of
         * <code>EventTO</code>s of a given category. If the category has no
         * events, the returned collection has zero elements
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are equal
         * to -1 the collection returned has all of the events of the specified
         * event</li>
         * </ul>
         * <ul>
         * <li> The <code>startDate</code> and the <code>endDate</code> may
         * be <code>null</code> </li>
         * </ul>
         * 
         * @param categoryID
         *                the category identifier
         * @param startIndex
         *                the index (starting from 1) of the first object to
         *                return
         * @param count
         *                the maximum number of objects to return
         * @param startDate
         *                the older date of the account operations to be
         *                returned (including this date)
         * @param endDate
         *                the newer date of the account operations to be
         *                returned (including this date)
         * @return the custom transfer object with the collection of
         *         <code>EventTO</code> with the data of the events of the
         *         specified category
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public EventChunkTO findEventsByCategory(String categoryID,
                        int startIndex, int count, Calendar startDate,
                        Calendar endDate) throws InternalErrorException;

        /**
         * Finds an event in the database
         * 
         * @param eventID
         *                the event identifier
         * @return a transfer object with the event data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public EventTO findEvent(Long eventID) throws InternalErrorException;

        /**
         * Returns a collection with the <code>BetOptionTO</code>s of a given
         * betType. If the betType has no bet options, the returned collection
         * has zero elements
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are equal
         * to -1 the collection returned has all of the bet options of the
         * specified bet type</li>
         * </ul>
         * 
         * @param betTypeID
         *                the bet type identifier
         * @param startIndex
         *                the index (starting from 1) of the first object to
         *                return
         * @param count
         *                the maximum number of objects to return
         * @return the custom transfer object with the collection of
         *         <code>BetTypeTO</code> with the data of the bet options of
         *         the specified bet type
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetOptionTO> findBetOptions(Long betTypeID, int startIndex,
                        int count) throws InternalErrorException;

        /**
         * Returns a collection with the winner <code>BetOptionTO</code>s of
         * a given betType. If the betType has no winner bet options, the
         * returned collection has zero elements
         * 
         * @param betTypeID
         *                the bet type identifier
         * @return the custom transfer object with the collection of
         *         <code>BetTypeTO</code> with the data of the winner bet
         *         options of the specified bet type
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<BetOptionTO> findWinnerOptions(Long betTypeID)
                        throws InternalErrorException;

        /**
         * Returns a collection with all the highlighted <code>EventTO</code>s
         * (not only the current three). If there is no highlighted events, the
         * returned collection has zero elements
         * 
         * @return the custom transfer object with the collection of
         *         <code>EventTO</code> with the data of the highlighted
         *         events
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public EventChunkTO findAllHighlightedEvents()
                        throws InternalErrorException;

        /**
         * Returns a collection with the highlighted <code>EventTO</code>s.
         * If there is no highlighted events, the returned collection has zero
         * elements
         * 
         * @return the custom transfer object with the collection of
         *         <code>EventTO</code> with the data of the highlighted
         *         events
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public EventChunkTO findHighlightedEvents()
                        throws InternalErrorException;

        /**
         * Updates highlighted events in the database
         * 
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void updateHighlightedEvents() throws InternalErrorException;

        /**
         * Finds all the countries in the database.
         * 
         * @return a collection of transfer objects with the countries data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<CountryTO> getAllCountries() throws InternalErrorException;

        /**
         * Finds all the categories in the database.
         * 
         * @return a collection of transfer objects with the categories data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<CategoryTO> getAllCategories()
                        throws InternalErrorException;

        /**
         * Finds all the questions in the database.
         * 
         * @return a collection of transfer objects with the questions data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<QuestionTO> getAllQuestions() throws InternalErrorException;

        /**
         * Finds all the category questions in the database.
         * 
         * @return a collection of transfer objects with the category questions
         *         data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public Map<String, List<QuestionTO>> getAllCategoryQuestions()
                        throws InternalErrorException;

        /**
         * Returns a custom transfer object with a collection of recent
         * <code>EventTO</code>s of the given categories. If the categories
         * have no recent unstarted events, the returned collection has zero
         * elements
         * <p>
         * NOTES:
         * <ul>
         * <li> If <code>startIndex</code> and <code>count</code> are equal
         * to -1 the collection returned has all of the events of the specified
         * event</li>
         * </ul>
         * <ul>
         * <li> The <code>startDate</code> and the <code>endDate</code> may
         * be <code>null</code> </li>
         * </ul>
         * 
         * @param categoryIDs
         *                a collection of category identifiers
         * @param startIndex
         *                the index (starting from 1) of the first object to
         *                return
         * @param count
         *                the maximum number of objects to return
         * @return the custom transfer object with the collection of
         *         <code>EventTO</code> with the data of the recent events of
         *         the specified categories
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public EventChunkTO findRecentEvents(List<String> categoryIDs,
                        int startIndex, int count)
                        throws InternalErrorException;

}
