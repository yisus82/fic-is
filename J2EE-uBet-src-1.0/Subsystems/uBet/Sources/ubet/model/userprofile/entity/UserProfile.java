package ubet.model.userprofile.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import ubet.model.account.entity.Account;
import ubet.model.country.entity.Country;

/**
 * An entity representing the information of an user.
 */
@Entity
public class UserProfile {

        /**
         * The user login (identifier)
         */
        private String login;

        /**
         * The password (normally encrypted) of the user
         */
        private String password;

        /**
         * The first name of the user
         */
        private String firstName;

        /**
         * The surname of the user
         */
        private String surname;

        /**
         * The e-mail of the user
         */
        private String email;

        /**
         * The country where the user lives in
         */
        private Country country;

        /**
         * The accounts associated with the user
         */
        private List<Account> accounts;

        private long version;

        public UserProfile() {
                accounts = new ArrayList<Account>();
        }

        /**
         * A constructor to create <code>UserProfile</code> objects
         * 
         * @param login
         *                the user login (identifier)
         * @param password
         *                the password (normally encrypted) of the user
         * @param firstName
         *                the first name of the user
         * @param surname
         *                the surname of the user
         * @param email
         *                the e-mail of the user
         * @param country
         *                the country where the user lives in
         */
        public UserProfile(String login, String password, String firstName,
                        String surname, String email, Country country) {
                this.login = login;
                this.password = password;
                this.firstName = firstName;
                this.surname = surname;
                this.email = email;
                this.country = country;
        }

        @Id
        public String getLogin() {
                return login;
        }

        public void setLogin(String login) {
                this.login = login;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getSurname() {
                return surname;
        }

        public void setSurname(String surname) {
                this.surname = surname;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        @ManyToOne
        @JoinColumn(name = "countryID")
        public Country getCountry() {
                return country;
        }

        public void setCountry(Country country) {
                this.country = country;
        }

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
        public List<Account> getAccounts() {
                return accounts;
        }

        public void setAccounts(List<Account> accounts) {
                this.accounts = accounts;
        }

        @Version
        public long getVersion() {
                return version;
        }

        public void setVersion(long version) {
                this.version = version;
        }

}
