package ubet.model.country.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import ubet.model.userprofile.entity.UserProfile;

/**
 * An entity representing the information of a country.
 */
@Entity
public class Country {
        /**
         * NOTE: this entity class does not contain a "version" property since
         * its instances are never updated after being persisted.
         */

        /**
         * The country identifier
         */
        private String countryID;

        /**
         * The name of the country
         */
        private String name;

        /**
         * The language of the country
         */
        private String language;

        /**
         * The identifier of the language
         */
        private String languageID;

        private List<UserProfile> users;

        public Country() {
                users = new ArrayList<UserProfile>();
        }

        /**
         * A constructor to create <code>Country</code> objects
         * 
         * @param countryID
         *                the country identifier
         * @param name
         *                the name of the country
         * @param language
         *                the language of the country
         * @param languageID
         *                the identifier of the language
         */
        public Country(String countryID, String name, String language,
                        String languageID) {
                this.countryID = countryID;
                this.name = name;
                this.language = language;
                this.languageID = languageID;
        }

        @Id
        public String getCountryID() {
                return countryID;
        }

        public void setCountryID(String countryID) {
                this.countryID = countryID;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getLanguage() {
                return language;
        }

        public void setLanguage(String language) {
                this.language = language;
        }

        public String getLanguageID() {
                return languageID;
        }

        public void setLanguageID(String languageID) {
                this.languageID = languageID;
        }

        @OneToMany(mappedBy = "country", cascade = CascadeType.REMOVE)
        public List<UserProfile> getUsers() {
                return users;
        }

        public void setUsers(List<UserProfile> users) {
                this.users = users;
        }

}
