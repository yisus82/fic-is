package ubet.model.accountoperation.entity;

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
import ubet.model.bet.entity.Bet;

/**
 * An entity representing the information of an account.
 * <p>
 * NOTES:
 * <ul>
 * <li> The <code>type</code> of the operation may be one of the following:
 * <ul>
 * <li><code>ADD_OPERATION</code></li>
 * <li><code>WITHDRAW_OPERATION</code></li>
 * </ul>
 * </li>
 * </ul>
 */
@Entity
public class AccountOperation {

        /**
         * The account operation identifier
         */
        private Long accountOperationID;

        /**
         * The account associated with the account operation
         */
        private Account account;

        /**
         * The amount of the account operation
         */
        private Double amount;

        /**
         * The type of the operation. It could be <code>ADD_OPERATION</code>
         * or <code>WITHDRAW_OPERATION</code>
         */
        private String type;

        /**
         * The description of the account operation
         */
        private String description;

        /**
         * The date when the account operation was made
         */
        private Calendar date;

        /**
         * The bet associated with the account operation. It may be null
         */
        private Bet bet;

        private long version;

        public AccountOperation() {
        }

        /**
         * A constructor to create <code>AccountOperation</code> objects
         * 
         * @param account
         *                the account associated with the account operation
         * @param amount
         *                the amount of the account operation
         * @param type
         *                the type of the operation. It could be
         *                <code>ADD_OPERATION</code> or
         *                <code>WITHDRAW_OPERATION</code>
         * @param description
         *                the description of the account operation
         * @param date
         *                the date when the account operation was made
         * @param bet
         *                the bet associated with the account operation. It may
         *                be null
         */
        public AccountOperation(Account account, Double amount, String type,
                        String description, Calendar date, Bet bet) {
                /**
                 * NOTE: "accountOperationID" *must* be left as "null" since its
                 * value is automatically generated (otherwise,
                 * "EntityManager.persist" may throw an exception indicating the
                 * entity to persist is detached).
                 */
                this.account = account;
                this.amount = amount;
                this.type = type;
                this.description = description;
                this.date = date;
                this.bet = bet;
        }

        /*
         * It only takes effect for databases providing identifier generators.
         */
        @SequenceGenerator(name = "AccountOperationIdentifierGenerator", sequenceName = "AccountOperationSeq")
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "AccountOperationIdentifierGenerator")
        public Long getAccountOperationID() {
                return accountOperationID;
        }

        public void setAccountOperationID(Long accountOperationID) {
                this.accountOperationID = accountOperationID;
        }

        @ManyToOne(optional = false)
        @JoinColumn(name = "accountID")
        public Account getAccount() {
                return account;
        }

        public void setAccount(Account account) {
                this.account = account;
        }

        public Double getAmount() {
                return amount;
        }

        public void setAmount(Double amount) {
                this.amount = amount;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
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
        @JoinColumn(name = "betID")
        public Bet getBet() {
                return bet;
        }

        public void setBet(Bet bet) {
                this.bet = bet;
        }

        @Version
        public long getVersion() {
                return version;
        }

        public void setVersion(long version) {
                this.version = version;
        }

}
