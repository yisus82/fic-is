package ubet.model.userfacade.to;

import java.io.Serializable;

/**
 * A custom transfer object representing the relevant data of an user.
 */
public class LoginResultTO implements Serializable {

        /**
         * The user login (identifier)
         */
        private String login;

        /**
         * The encrypted password of the user
         */
        private String password;

        /**
         * The identifier of the country where the user lives in
         */
        private String countryID;

        /**
         * A constructor to create <code>LoginResultTO</code> objects
         * 
         * @param login
         *                the user login (identifier)
         * @param password
         *                the encrypted password of the user
         * @param countryID
         *                the identifier of the country where the user lives in
         */
        public LoginResultTO(String login, String password, String countryID) {
                this.login = login;
                this.password = password;
                this.countryID = countryID;
        }

        /**
         * @return the <code>login</code>
         */
        public String getLogin() {
                return this.login;
        }

        /**
         * @return the <code>password</code>
         */
        public String getPassword() {
                return this.password;
        }

        /**
         * @return the <code>countryID</code>
         */
        public String getCountryID() {
                return this.countryID;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof LoginResultTO) {
                        LoginResultTO result = (LoginResultTO) object;
                        return (result.getCountryID().equals(this.countryID)
                                        && result.getLogin().equals(this.login) && result
                                        .getPassword().equals(this.password));
                }

                return false;
        }

        @Override
        public String toString() {
                return ("\nCountry ID = " + this.countryID + "\nLogin = "
                                + this.login + "\nPassword = " + this.password + "\n");
        }

}
