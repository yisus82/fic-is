package ubet.model.account.entity;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import ubet.model.accountoperation.entity.AccountOperation;
import ubet.model.bet.entity.Bet;
import ubet.model.userprofile.entity.UserProfile;

/**
 * An entity representing the information of an account.
 */
@Entity
public class Account {

        /**
         * The account identifier
         */
        private Long accountID;

        /**
         * The user profile of the account owner
         */
        private UserProfile user;

        /**
         * The credit card number associated with the account
         */
        private String creditCardNumber;

        /**
         * The expiration date of the credit card
         */
        private Calendar expirationDate;

        /**
         * The balance of the account
         */
        private Double balance;

        /**
         * The account operations associated with the account
         */
        private List<AccountOperation> accountOperations;

        /**
         * The bets associated with the account
         */
        private List<Bet> bets;

        private long version;

        public Account() {
                accountOperations = new ArrayList<AccountOperation>();
        }

        /**
         * A constructor to create <code>Account</code> objects
         * 
         * @param user
         *                the user profile of the account owner
         * @param creditCardNumber
         *                the credit card number associated with the account
         * @param expirationDate
         *                the expiration date of the credit card
         * @param balance
         *                the balance of the account
         */
        public Account(UserProfile user, String creditCardNumber,
                        Calendar expirationDate, Double balance) {
                /**
                 * NOTE: "accountID" *must* be left as "null" since its value is
                 * automatically generated (otherwise, "EntityManager.persist"
                 * may throw an exception indicating the entity to persist is
                 * detached).
                 */
                this.user = user;
                this.creditCardNumber = creditCardNumber;
                this.expirationDate = expirationDate;
                this.balance = balance;
        }

        /*
         * It only takes effect for databases providing identifier generators.
         */
        @SequenceGenerator(name = "AccountIdentifierGenerator", sequenceName = "AccountSeq")
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "AccountIdentifierGenerator")
        public Long getAccountID() {
                return accountID;
        }

        public void setAccountID(Long accountID) {
                this.accountID = accountID;
        }

        @ManyToOne(optional = false)
        @JoinColumn(name = "userID")
        public UserProfile getUser() {
                return user;
        }

        public void setUser(UserProfile user) {
                this.user = user;
        }

        public String getCreditCardNumber() {
                return creditCardNumber;
        }

        public void setCreditCardNumber(String creditCardNumber) {
                this.creditCardNumber = creditCardNumber;
        }

        public Calendar getExpirationDate() {
                return expirationDate;
        }

        public void setExpirationDate(Calendar expirationDate) {
                this.expirationDate = expirationDate;
        }

        public Double getBalance() {
                return balance;
        }

        public void setBalance(Double balance) {
                this.balance = balance;
        }

        @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
        public List<AccountOperation> getAccountOperations() {
                return accountOperations;
        }

        public void setAccountOperations(
                        List<AccountOperation> accountOperations) {
                this.accountOperations = accountOperations;
        }

        @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
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
