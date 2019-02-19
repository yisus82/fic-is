package ubet.model.bettype.to;

import java.io.Serializable;

/**
 * A transfer object representing the information of a bet type.
 */
public class BetTypeTO implements Serializable {

        /**
         * The bet type identifier
         */
        private Long betTypeID;

        /**
         * The identifier of the event associated with the bet type
         */
        private Long eventID;

        /**
         * The identifier of the question associated with the bet type
         */
        private Long questionID;

        /**
         * A constructor to create <code>BetTypeTO</code> objects
         * 
         * @param betTypeID
         *                the bet type identifier
         * @param eventID
         *                the identifier of the event associated with the bet
         *                type
         * @param questionID
         *                the identifier of the question associated with the bet
         *                type
         */
        public BetTypeTO(Long betTypeID, Long eventID, Long questionID) {
                this.betTypeID = betTypeID;
                this.eventID = eventID;
                this.questionID = questionID;
        }

        /**
         * @return the <code>betTypeID</code>
         */
        public Long getBetTypeID() {
                return this.betTypeID;
        }

        /**
         * @param betTypeID
         *                the betTypeID to set
         */
        public void setBetTypeID(Long betTypeID) {
                this.betTypeID = betTypeID;
        }

        /**
         * @return the <code>eventID</code>
         */
        public Long getEventID() {
                return this.eventID;
        }

        /**
         * @param eventID
         *                the eventID to set
         */
        public void setEventID(Long eventID) {
                this.eventID = eventID;
        }

        /**
         * @return the <code>questionID</code>
         */
        public Long getQuestionID() {
                return this.questionID;
        }

        /**
         * @param questionID
         *                the questionID to set
         */
        public void setQuestionID(Long questionID) {
                this.questionID = questionID;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof BetTypeTO) {
                        BetTypeTO type = (BetTypeTO) object;
                        return (type.getBetTypeID().equals(this.betTypeID)
                                        && type.getEventID().equals(
                                                        this.eventID) && type
                                        .getQuestionID()
                                        .equals(this.questionID));
                }

                return false;
        }

        @Override
        public String toString() {
                return ("\nBet type ID = " + this.betTypeID + "\nEventID = "
                                + this.eventID + "\nQuestion ID = "
                                + this.questionID + "\n");
        }

}
