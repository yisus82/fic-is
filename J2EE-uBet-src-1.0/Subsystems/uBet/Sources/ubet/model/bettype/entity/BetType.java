package ubet.model.bettype.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import ubet.model.betoption.entity.BetOption;
import ubet.model.event.entity.Event;
import ubet.model.question.entity.Question;

/**
 * An entity representing the information of a bet type.
 */
@Entity
public class BetType {

        /**
         * The bet type identifier
         */
        private Long betTypeID;

        /**
         * The event associated with the bet type
         */
        private Event event;

        /**
         * The question associated with the bet type
         */
        private Question question;

        /**
         * The bet options associated with the bet type
         */
        private List<BetOption> betOptions;

        private long version;

        public BetType() {
                betOptions = new ArrayList<BetOption>();
        }

        /**
         * A constructor to create <code>BetType</code> objects
         * 
         * @param event
         *                the event associated with the bet type
         * @param question
         *                the question associated with the bet type
         */
        public BetType(Event event, Question question) {
                /**
                 * NOTE: "betTypeID" *must* be left as "null" since its value is
                 * automatically generated (otherwise, "EntityManager.persist"
                 * may throw an exception indicating the entity to persist is
                 * detached).
                 */
                this.event = event;
                this.question = question;
        }

        /*
         * It only takes effect for databases providing identifier generators.
         */
        @SequenceGenerator(name = "BetTypeIdentifierGenerator", sequenceName = "BetTypeSeq")
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "BetTypeIdentifierGenerator")
        public Long getBetTypeID() {
                return betTypeID;
        }

        public void setBetTypeID(Long betTypeID) {
                this.betTypeID = betTypeID;
        }

        @ManyToOne(optional = false)
        @JoinColumn(name = "eventID")
        public Event getEvent() {
                return event;
        }

        public void setEvent(Event event) {
                this.event = event;
        }

        @ManyToOne
        @JoinColumn(name = "questionID")
        public Question getQuestion() {
                return question;
        }

        public void setQuestion(Question question) {
                this.question = question;
        }

        @OneToMany(mappedBy = "betType", cascade = CascadeType.ALL)
        public List<BetOption> getBetOptions() {
                return betOptions;
        }

        public void setBetOptions(List<BetOption> betOptions) {
                this.betOptions = betOptions;
        }

        @Version
        public long getVersion() {
                return version;
        }

        public void setVersion(long version) {
                this.version = version;
        }

}
