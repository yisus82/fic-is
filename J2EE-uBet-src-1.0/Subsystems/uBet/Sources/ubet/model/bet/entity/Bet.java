package ubet.model.bet.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import ubet.model.account.entity.Account;
import ubet.model.betoption.entity.BetOption;
import ubet.model.bettype.entity.BetType;
import ubet.model.event.entity.Event;

/**
 * An entity representing the information of a bet.
 * <p>
 * NOTES:
 * <ul>
 * <li> The <code>status</code> of the bet may be one of the following:
 * <ul>
 * <li><code>GAINED</code></li>
 * <li><code>PENDING</code></li>
 * <li><code>LOST</code></li>
 * </ul>
 * </li>
 * </ul>
 */
@Entity
public class Bet {

        /**
         * The bet identifier
         */
        private Long betID;

        /**
         * The bet type associated with the bet
         */
        private BetType betType;

        /**
         * The bet option associated with the bet
         */
        private BetOption betOption;

        /**
         * The account associated with the bet
         */
        private Account account;

        /**
         * The the event associated with the bet
         */
        private Event event;

        /**
         * The amount of the bet
         */
        private Double amount;

        /**
         * The date when the bet was made
         */
        private Calendar date;

        /**
         * The current status of the bet. It could be <code>GAINED</code>,
         * <code>PENDING</code> or <code>LOST</code>
         */
        private String status;

        private long version;

        public Bet() {
        }

        /**
         * A constructor to create <code>Bet</code> objects
         * 
         * @param betType
         *                the bet type associated with the bet
         * @param betOption
         *                the bet option associated with the bet
         * @param account
         *                the account associated with the bet
         * @param event
         *                the event associated with the bet
         * @param amount
         *                the amount of the bet
         * @param date
         *                the date when the bet was made
         * @param status
         *                the current status of the bet. It could be
         *                <code>GAINED</code>, <code>PENDING</code> or
         *                <code>LOST</code>
         */
        public Bet(BetType betType, BetOption betOption, Account account,
                        Event event, Double amount, Calendar date, String status) {
                /**
                 * NOTE: "betID" *must* be left as "null" since its value is
                 * automatically generated (otherwise, "EntityManager.persist"
                 * may throw an exception indicating the entity to persist is
                 * detached).
                 */
                this.betType = betType;
                this.betOption = betOption;
                this.account = account;
                this.event = event;
                this.amount = amount;
                this.date = date;
                this.status = status;
        }

        /*
         * It only takes effect for databases providing identifier generators.
         */
        @SequenceGenerator(name = "BetIdentifierGenerator", sequenceName = "BetSeq")
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "BetIdentifierGenerator")
        public Long getBetID() {
                return betID;
        }

        public void setBetID(Long betID) {
                this.betID = betID;
        }

        @ManyToOne
        @JoinColumn(name = "betTypeID")
        public BetType getBetType() {
                return betType;
        }

        public void setBetType(BetType betType) {
                this.betType = betType;
        }

        @ManyToOne
        @JoinColumn(name = "betOptionID")
        public BetOption getBetOption() {
                return betOption;
        }

        public void setBetOption(BetOption betOption) {
                this.betOption = betOption;
        }

        @ManyToOne
        @JoinColumn(name = "accountID")
        public Account getAccount() {
                return account;
        }

        public void setAccount(Account account) {
                this.account = account;
        }

        @ManyToOne
        @JoinColumn(name = "eventID")
        public Event getEvent() {
                return event;
        }

        public void setEvent(Event event) {
                this.event = event;
        }

        public Double getAmount() {
                return amount;
        }

        public void setAmount(Double amount) {
                this.amount = amount;
        }

        public Calendar getDate() {
                return date;
        }

        public void setDate(Calendar date) {
                this.date = date;
        }

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        @Version
        public long getVersion() {
                return version;
        }

        public void setVersion(long version) {
                this.version = version;
        }

}
