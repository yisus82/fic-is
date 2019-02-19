package ibei.model.country.to;

import java.io.Serializable;

/**
 * A transfer object representing the information of a country.
 */
public class CountryTO implements Serializable {

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

        /**
         * A constructor to create <code>CountryTO</code> objects
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
        public CountryTO(String countryID, String name, String language,
                        String languageID) {
                this.countryID = countryID;
                this.name = name;
                this.language = language;
                this.languageID = languageID;
        }

        /**
         * @return the <code>countryID</code>
         */
        public String getCountryID() {
                return this.countryID;
        }

        /**
         * @return the <code>name</code>
         */
        public String getName() {
                return this.name;
        }

        /**
         * @return the <code>language</code>
         */
        public String getLanguage() {
                return this.language;
        }

        /**
         * @return the <code>languageID</code>
         */
        public String getLanguageID() {
                return this.languageID;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof CountryTO) {
                        CountryTO country = (CountryTO) object;
                        return (country.getCountryID().equals(this.countryID)
                                        && country.getLanguage().equals(
                                                        this.language)
                                        && country.getLanguageID().equals(
                                                        this.languageID) && country
                                        .getName().equals(this.name));
                }

                return false;
        }

        @Override
        public String toString() {
                return ("\nCountry ID = " + this.countryID + "\nLanguage = "
                                + this.language + "\nLanguage ID = "
                                + this.languageID + "\nName = " + this.name + "\n");
        }

}
