package ubet.model.searchfacade.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import ubet.model.betoption.entity.BetOption;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.entity.BetType;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.category.entity.Category;
import ubet.model.category.to.CategoryTO;
import ubet.model.country.entity.Country;
import ubet.model.country.to.CountryTO;
import ubet.model.event.entity.Event;
import ubet.model.event.to.EventTO;
import ubet.model.question.entity.Question;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.to.BetTypeResultTO;
import ubet.model.searchfacade.to.CategoryChunkTO;
import ubet.model.searchfacade.to.EventResultTO;

public class SearchFacadeHelper {

        private SearchFacadeHelper() {
        }

        public static Event findEvent(EntityManager entityManager, Long eventID) {
                return entityManager.find(Event.class, eventID);
        }

        public static EventTO toEventTO(Event event) {
                return new EventTO(event.getEventID(), event.getDescription(),
                                event.getDate(), event.getCategory()
                                                .getCategoryID(), event
                                                .getBetType().getBetTypeID(),
                                event.getHighlighted(), event
                                                .getInsertionDate());
        }

        public static Event toEvent(EntityManager entityManager, EventTO eventTO) {
                return new Event(eventTO.getDescription(), eventTO.getDate(),
                                entityManager.find(Category.class, eventTO
                                                .getCategoryID()),
                                entityManager.find(BetType.class, eventTO
                                                .getBetTypeID()), eventTO
                                                .getHighlighted(), eventTO
                                                .getInsertionDate());
        }

        public static List<EventTO> toEventTOs(List<Event> events) {
                List<EventTO> eventTOs = new ArrayList<EventTO>();

                for (Event event : events)
                        eventTOs.add(toEventTO(event));

                return eventTOs;
        }

        public static EventResultTO toEventResultTO(
                        EntityManager entityManager, Event event) {
                return new EventResultTO(event.getEventID(), event
                                .getDescription(), event.getDate(),
                                toCategoryChunkTO(entityManager, event
                                                .getCategory()),
                                toBetTypeResultTO(entityManager, event
                                                .getBetType()), event
                                                .getHighlighted());
        }

        public static List<EventResultTO> toEventResultTOs(
                        EntityManager entityManager, List<Event> events) {
                List<EventResultTO> eventResultTOs = new ArrayList<EventResultTO>();

                for (Event event : events)
                        eventResultTOs
                                        .add(toEventResultTO(entityManager,
                                                        event));

                return eventResultTOs;
        }

        public static BetTypeTO toBetTypeTO(BetType betType) {
                return new BetTypeTO(betType.getBetTypeID(), betType.getEvent()
                                .getEventID(), betType.getQuestion()
                                .getQuestionID());
        }

        public static BetType toBetType(EntityManager entityManager,
                        BetTypeTO betTypeTO) {
                return new BetType(entityManager.find(Event.class, betTypeTO
                                .getEventID()), entityManager.find(
                                Question.class, betTypeTO.getQuestionID()));
        }

        public static List<BetTypeTO> toBetTypeTOs(List<BetType> betTypes) {
                List<BetTypeTO> betTypeTOs = new ArrayList<BetTypeTO>();

                for (BetType betType : betTypes)
                        betTypeTOs.add(toBetTypeTO(betType));

                return betTypeTOs;
        }

        public static BetTypeResultTO toBetTypeResultTO(
                        EntityManager entityManager, BetType betType) {
                return new BetTypeResultTO(betType.getBetTypeID(), betType
                                .getEvent().getEventID(), toQuestionTO(betType
                                .getQuestion()), toBetOptionTOs(betType
                                .getBetOptions()));
        }

        public static List<BetTypeResultTO> toBetTypeResultTOs(
                        EntityManager entityManager, List<BetType> betTypes) {
                List<BetTypeResultTO> betTypeResultTOs = new ArrayList<BetTypeResultTO>();

                for (BetType betType : betTypes)
                        betTypeResultTOs.add(toBetTypeResultTO(entityManager,
                                        betType));

                return betTypeResultTOs;
        }

        public static BetOptionTO toBetOptionTO(BetOption betOption) {
                return new BetOptionTO(betOption.getBetOptionID(), betOption
                                .getDescription(), betOption.getOdds(),
                                betOption.getBetType().getBetTypeID(),
                                betOption.getStatus());
        }

        public static BetOption toBetOption(EntityManager entityManager,
                        BetOptionTO betOptionTO) {
                return new BetOption(betOptionTO.getDescription(), betOptionTO
                                .getOdds(), entityManager.find(BetType.class,
                                betOptionTO.getBetTypeID()), betOptionTO
                                .getStatus());
        }

        public static List<BetOptionTO> toBetOptionTOs(
                        List<BetOption> betOptions) {
                List<BetOptionTO> betOptionTOs = new ArrayList<BetOptionTO>();

                for (BetOption betOption : betOptions)
                        betOptionTOs.add(toBetOptionTO(betOption));

                return betOptionTOs;
        }

        public static QuestionTO toQuestionTO(Question question) {
                return new QuestionTO(question.getQuestionID(), question
                                .getDescription());
        }

        public static Question toQuestion(QuestionTO questionTO) {
                return new Question(questionTO.getQuestionID(), questionTO
                                .getDescription());
        }

        public static List<QuestionTO> toQuestionTOs(List<Question> questions) {
                List<QuestionTO> questionTOs = new ArrayList<QuestionTO>();

                for (Question question : questions)
                        questionTOs.add(toQuestionTO(question));

                return questionTOs;
        }

        public static CategoryTO toCategoryTO(Category category) {
                if (category == null)
                        return null;
                return new CategoryTO(category.getCategoryID(), category
                                .getName(), category.getParent()
                                .getCategoryID(), (category.getLeaf()
                                .equals("Y")), category.getQuestion()
                                .getQuestionID());
        }

        public static Category toCategory(EntityManager entityManager,
                        CategoryTO categoryTO) {
                if (categoryTO.isLeaf())
                        return new Category(
                                        categoryTO.getCategoryID(),
                                        categoryTO.getName(),
                                        entityManager
                                                        .find(
                                                                        Category.class,
                                                                        categoryTO
                                                                                        .getParentID()),
                                        "Y",
                                        entityManager
                                                        .find(
                                                                        Question.class,
                                                                        categoryTO
                                                                                        .getQuestionID()));
                return new Category(categoryTO.getCategoryID(), categoryTO
                                .getName(), entityManager.find(Category.class,
                                categoryTO.getParentID()), "N", entityManager
                                .find(Question.class, categoryTO
                                                .getQuestionID()));
        }

        public static List<CategoryTO> toCategoryTOs(List<Category> categories) {
                List<CategoryTO> categoryTOs = new ArrayList<CategoryTO>();

                for (Category category : categories)
                        categoryTOs.add(toCategoryTO(category));

                return categoryTOs;
        }

        public static CategoryChunkTO toCategoryChunkTO(
                        EntityManager entityManager, Category category) {
                if (category.getCategoryID().equals(
                                category.getParent().getCategoryID()))
                        return new CategoryChunkTO(category.getCategoryID(),
                                        category.getName(), (category.getLeaf()
                                                        .equals("Y")), category
                                                        .getQuestion()
                                                        .getQuestionID());
                return new CategoryChunkTO(category.getCategoryID(), category
                                .getName(), toCategoryChunkTO(entityManager,
                                category.getParent()), (category.getLeaf()
                                .equals("Y")), category.getQuestion()
                                .getQuestionID());
        }

        public static Country findCountry(EntityManager entityManager,
                        String countryID) {
                return entityManager.find(Country.class, countryID);
        }

        public static CountryTO toCountryTO(Country country) {
                return new CountryTO(country.getCountryID(), country.getName(),
                                country.getLanguage(), country.getLanguageID());
        }

        public static Country toCountry(CountryTO countryTO) {
                return new Country(countryTO.getCountryID(), countryTO
                                .getName(), countryTO.getLanguage(), countryTO
                                .getLanguageID());
        }

        public static List<CountryTO> toCountryTOs(List<Country> countries) {
                List<CountryTO> countryTOs = new ArrayList<CountryTO>();

                for (Country country : countries)
                        countryTOs.add(toCountryTO(country));

                return countryTOs;
        }

}
