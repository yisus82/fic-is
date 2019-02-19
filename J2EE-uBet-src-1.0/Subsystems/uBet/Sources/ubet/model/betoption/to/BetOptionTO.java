package ubet.model.betoption.to;

import java.io.Serializable;

/**
 * A transfer object representing the information of a bet option.
 * <p>
 * NOTES:
 * <ul>
 * <li> The <code>status</code> of the bet option may be one of the following:
 * <ul>
 * <li><code>WINNER</code></li>
 * <li><code>PENDING</code></li>
 * <li><code>LOSER</code></li>
 * </ul>
 * </li>
 * </ul>
 */
public class BetOptionTO implements Serializable {

        /**
         * A constant used when the bet option is a winner option
         */
        public static String WINNER = "W";

        /**
         * A constant used when the bet option is a pending option
         */
        public static String PENDING = "P";

        /**
         * A constant used when the bet option is a loser option
         */
        public static String LOSER = "L";

        /**
         * The bet option identifier
         */
        private Long betOptionID;

        /**
         * The description of the bet option
         */
        private String description;

        /**
         * The ratio by which one better's wager is greater than that of another
         */
        private Double odds;

        /**
         * The identifier of the bet type associated with the bet option
         */
        private Long betTypeID;

        /**
         * The current status of the bet option. It could be <code>WINNER</code>,
         * <code>PENDING</code> or <code>LOSER</code>
         */
        private String status;

        /**
         * A constructor to create <code>BetOptionTO</code> objects
         * 
         * @param betOptionID
         *                the bet option identifier
         * @param description
         *                the description of the bet option
         * @param odds
         *                the ratio by which one better's wager is greater than
         *                that of another
         * @param betTypeID
         *                the identifier of the bet type associated with the bet
         *                option
         * @param status
         *                the current status of the bet option. It could be
         *                <code>WINNER</code>, <code>PENDING</code> or
         *                <code>LOSER</code>
         */
        public BetOptionTO(Long betOptionID, String description, Double odds,
                        Long betTypeID, String status) {
                this.betOptionID = betOptionID;
                this.description = description;
                this.odds = odds;
                this.betTypeID = betTypeID;
                this.status = status;
        }

        /**
         * @return the <code>betOptionID</code>
         */
        public Long getBetOptionID() {
                return this.betOptionID;
        }

        /**
         * @param betOptionID
         *                the betOption to set
         */
        public void setBetOptionID(Long betOptionID) {
                this.betOptionID = betOptionID;
        }

        /**
         * @return the <code>description</code>
         */
        public String getDescription() {
                return this.description;
        }

        /**
         * @param description
         *                the description to set
         */
        public void setDescription(String description) {
                this.description = description;
        }

        /**
         * @return the <code>odds</code>
         */
        public Double getOdds() {
                return this.odds;
        }

        /**
         * @param odds
         *                the odds to set
         */
        public void setOdds(Double odds) {
                this.odds = odds;
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
         * @return the <code>status</code>
         */
        public String getStatus() {
                return this.status;
        }

        /**
         * @param status
         *                the status to set. It could be <code>WINNER</code>,
         *                <code>PENDING</code> or <code>LOSER</code>
         */
        public void setStatus(String status) {
                this.status = status;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof BetOptionTO) {
                        BetOptionTO option = (BetOptionTO) object;
                        return (option.getBetOptionID()
                                        .equals(this.betOptionID)
                                        && option.getDescription().equals(
                                                        this.description)
                                        && option.getOdds().equals(this.odds)
                                        && option.getBetTypeID().equals(
                                                        this.betTypeID) && option
                                        .getStatus().equals(this.status));
                }

                return false;
        }

        @Override
        public String toString() {
                return ("\nBet option ID = " + this.betOptionID
                                + "\nDescription = " + this.description
                                + "\nOdds = " + this.odds + "\nStatus = "
                                + this.status + "\nBetType ID = "
                                + this.betTypeID + "\n");
        }

}
