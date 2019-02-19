package ubet.model.event.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import ubet.model.bet.entity.Bet;
import ubet.model.bettype.entity.BetType;
import ubet.model.category.entity.Category;

/**
 * An entity representing the information of an event.
 * <p>
 * NOTES:
 * <ul>
 * <li> The <code>highlighted</code> status of the event may be one of the
 * following:
 * <ul>
 * <li><code>YES</code> (It is one of the three highlighted events at this
 * moment)</li>
 * <li><code>NO</code> (It will never be one of the three highlighted events)</li>
 * <li><code>READY</code> (It is ready to become one of the three highlighted
 * events)</li>
 * <li><code>WAITING</code> (It is waiting to become one of the three
 * highlighted events again)</li>
 * </ul>
 * </li>
 * </ul>
 */
@Entity
public class Event {

        /**
         * The event identifier
         */
        private Long eventID;

        /**
         * The description of the event
         */
        private String description;

        /**
         * The date when the event starts
         */
        private Calendar date;

        /**
         * The category associated with the event
         */
        private Category category;

        /**
         * The default bet type associated with the event
         */
        private BetType betType;

        /**
         * Indicates if the event is highlighted. It could be <code>YES</code>,
         * <code>NO</code>, <code>READY</code> or <code>WAITING</code>
         */
        private String highlighted;

        /**
         * The date when the event was inserted into the DB
         */
        private Calendar insertionDate;

        /**
         * The bets associated with the event
         */
        private List<Bet> bets;

        /**
         * The bet types associated with the event
         */
        private List<BetType> betTypes;

        private long version;

        public Event() {
                bets = new ArrayList<Bet>();
                betTypes = new ArrayList<BetType>();
        }

        /**
         * A constructor to create <code>Event</code> objects
         * 
         * @param description
         *                the description of the event
         * @param date
         *                the date when the event starts
         * @param category
         *                the category associated with the event
         * @param betType
         *                the default bet type associated with the event
         * @param highlighted
         *                <code>YES</code>, <code>NO</code>,
         *                <code>READY</code> or <code>WAITING</code>
         * @param insertionDate
         *                the date when the event was inserted into the DB
         */
        public Event(String description, Calendar date, Category category,
                        BetType betType, String highlighted,
                        Calendar insertionDate) {
                /**
                 * NOTE: "eventID" *must* be left as "null" since its value is
                 * automatically generated (otherwise, "EntityManager.persist"
                 * may throw an exception indicating the entity to persist is
                 * detached).
                 */
                this.description = description;
                this.date = date;
                this.category = category;
                this.betType = betType;
                this.highlighted = highlighted;
                this.insertionDate = insertionDate;
        }

        /*
         * It only takes effect for databases providing identifier generators.
         */
        @SequenceGenerator(name = "EventIdentifierGenerator", sequenceName = "EventSeq")
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "EventIdentifierGenerator")
        public Long getEventID() {
                return eventID;
        }

        public void setEventID(Long eventID) {
                this.eventID = eventID;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public Calendar getDate() {
                return date;
        }

        public void setDate(Calendar date) {
                this.date = date;
        }

        @ManyToOne
        @JoinColumn(name = "categoryID")
        public Category getCategory() {
                return category;
        }

        public void setCategory(Category category) {
                this.category = category;
        }

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "betTypeID")
        public BetType getBetType() {
                return betType;
        }

        public void setBetType(BetType betType) {
                this.betType = betType;
        }

        public String getHighlighted() {
                return highlighted;
        }

        public void setHighlighted(String highlighted) {
                this.highlighted = highlighted;
        }

        public Calendar getInsertionDate() {
                return insertionDate;
        }

        public void setInsertionDate(Calendar insertionDate) {
                this.insertionDate = insertionDate;
        }

        @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
        public List<Bet> getBets() {
                return bets;
        }

        public void setBets(List<Bet> bets) {
                this.bets = bets;
        }

        @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE)
        public List<BetType> getBetTypes() {
                return betTypes;
        }

        public void setBetTypes(List<BetType> betTypes) {
                this.betTypes = betTypes;
        }

        @Version
        public long getVersion() {
                return version;
        }

        public void setVersion(long version) {
                this.version = version;
        }

}
