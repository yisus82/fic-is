package ubet.model.searchfacade.to;

import java.io.Serializable;
import java.util.List;

import ubet.model.betoption.to.BetOptionTO;
import ubet.model.question.to.QuestionTO;

/**
 * A custom transfer object representing the information of a bet type.
 */
public class BetTypeResultTO implements Serializable {

        /**
         * The bet type identifier
         */
        private Long betTypeID;

        /**
         * The identifier of the event associated with the bet type
         */
        private Long eventID;

        /**
         * The question associated with the bet type
         */
        private QuestionTO question;

        /**
         * A collection of <code>BetOptionTO</code> with the data of the bet
         * options associated with the bet type
         */
        private List<BetOptionTO> options;

        /**
         * A constructor to create <code>BetTypeTO</code> objects
         * 
         * @param betTypeID
         *                the bet type identifier
         * @param eventID
         *                the identifier of the event associated with the bet
         *                type
         * @param question
         *                the question associated with the bet type
         * @param options
         *                a collection of <code>BetOptionTO</code> with the
         *                data of the bet options associated with the bet type
         */
        public BetTypeResultTO(Long betTypeID, Long eventID,
                        QuestionTO question, List<BetOptionTO> options) {
                this.betTypeID = betTypeID;
                this.eventID = eventID;
                this.question = question;
                this.options = options;
        }

        /**
         * @return the <code>betTypeID</code>
         */
        public Long getBetTypeID() {
                return this.betTypeID;
        }

        /**
         * @return the <code>eventID</code>
         */
        public Long getEventID() {
                return this.eventID;
        }

        /**
         * @return the <code>question</code>
         */
        public QuestionTO getQuestion() {
                return this.question;
        }

        /**
         * @return the <code>options</code>
         */
        public List<BetOptionTO> getOptions() {
                return this.options;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof BetTypeResultTO) {
                        BetTypeResultTO type = (BetTypeResultTO) object;
                        return (type.getBetTypeID().equals(this.betTypeID)
                                        && type.getEventID().equals(
                                                        this.eventID)
                                        && type.getQuestion().equals(
                                                        this.question) && type
                                        .getOptions().equals(this.options));
                }

                return false;
        }

        @Override
        public String toString() {
                return ("\nBet type ID = " + this.betTypeID + "\nEventID = "
                                + this.eventID + "\nQuestion = "
                                + this.question + "\nOptions = " + options + "\n");
        }

}
