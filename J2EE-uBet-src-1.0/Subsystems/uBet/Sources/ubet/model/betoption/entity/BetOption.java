package ubet.model.betoption.entity;

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

import ubet.model.bet.entity.Bet;
import ubet.model.bettype.entity.BetType;

/**
 * An entity representing the information of a bet option.
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
@Entity
public class BetOption {

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
         * The bet type associated with the bet option
         */
        private BetType betType;

        /**
         * The current status of the bet option. It could be <code>WINNER</code>,
         * <code>PENDING</code> or <code>LOSER</code>
         */
        private String status;

        /**
         * The bets associated with the bet option
         */
        private List<Bet> bets;

        private long version;

        public BetOption() {
                bets = new ArrayList<Bet>();
        }

        /**
         * A constructor to create <code>BetOption</code> objects
         * 
         * @param description
         *                the description of the bet option
         * @param odds
         *                the ratio by which one better's wager is greater than
         *                that of another
         * @param betType
         *                the bet type associated with the bet option
         * @param status
         *                the current status of the bet option. It could be
         *                <code>WINNER</code>, <code>PENDING</code> or
         *                <code>LOSER</code>
         */
        public BetOption(String description, Double odds, BetType betType,
                        String status) {
                /**
                 * NOTE: "betOptionID" *must* be left as "null" since its value
                 * is automatically generated (otherwise,
                 * "EntityManager.persist" may throw an exception indicating the
                 * entity to persist is detached).
                 */
                this.description = description;
                this.odds = odds;
                this.betType = betType;
                this.status = status;
        }

        /*
         * It only takes effect for databases providing identifier generators.
         */
        @SequenceGenerator(name = "BetOptionIdentifierGenerator", sequenceName = "BetOptionSeq")
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "BetOptionIdentifierGenerator")
        public Long getBetOptionID() {
                return betOptionID;
        }

        public void setBetOptionID(Long betOptionID) {
                this.betOptionID = betOptionID;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public Double getOdds() {
                return odds;
        }

        public void setOdds(Double odds) {
                this.odds = odds;
        }

        @ManyToOne(optional = false)
        @JoinColumn(name = "betTypeID")
        public BetType getBetType() {
                return betType;
        }

        public void setBetType(BetType betType) {
                this.betType = betType;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        @OneToMany(mappedBy = "betOption", cascade = CascadeType.REMOVE)
        public List<Bet> getBets() {
                return bets;
        }

        public void setBets(List<Bet> bets) {
                this.bets = bets;
        }

        @Version
        public long getVersion() {
                return version;
        }

        public void setVersion(long version) {
                this.version = version;
        }

}
