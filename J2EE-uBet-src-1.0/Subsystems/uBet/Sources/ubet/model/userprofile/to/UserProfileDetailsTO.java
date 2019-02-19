package ubet.model.userprofile.to;

import java.io.Serializable;

/**
 * A transfer object representing the information of an user profile details.
 */
public class UserProfileDetailsTO implements Serializable {

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
         * The identifier of the country where the user lives in
         */
        private String countryID;

        /**
         * A constructor to create <code>UserProfileDetailsTO</code> objects
         * 
         * @param firstName
         *                the first name of the user
         * @param surname
         *                the surname of the user
         * @param email
         *                the e-mail of the user
         * @param countryID
         *                the identifier of the country where the user lives in
         */
        public UserProfileDetailsTO(String firstName, String surname,
                        String email, String countryID) {
                this.firstName = firstName;
                this.surname = surname;
                this.email = email;
                this.countryID = countryID;
        }

        /**
         * @return the <code>firstName</code>
         */
        public String getFirstName() {
                return this.firstName;
        }

        /**
         * @param firstName
         *                the firstName to set
         */
        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        /**
         * @return the <code>surname</code>
         */
        public String getSurname() {
                return this.surname;
        }

        /**
         * @param surname
         *                the surname to set
         */
        public void setSurname(String surname) {
                this.surname = surname;
        }

        /**
         * @return the <code>email</code>
         */
        public String getEmail() {
                return this.email;
        }

        /**
         * 
         * @param email
         *                the email to set
         */
        public void setEmail(String email) {
                this.email = email;
        }

        /**
         * @return the <code>countryID</code>
         */
        public String getCountryID() {
                return this.countryID;
        }

        /**
         * @param countryID
         *                the countryID to set
         */
        public void setCountryID(String countryID) {
                this.countryID = countryID;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof UserProfileDetailsTO) {
                        UserProfileDetailsTO details = (UserProfileDetailsTO) object;
                        return (details.getCountryID().equals(this.countryID)
                                        && details.getEmail()
                                                        .equals(this.email)
                                        && details.getFirstName().equals(
                                                        this.firstName) && details
                                        .getSurname().equals(this.surname));
                }

                return false;
        }

        @Override
        public String toString() {
                return ("\nCountryID = " + this.countryID + "\nE-mail = "
                                + this.email + "\nFirst name = "
                                + this.firstName + "\nSurname = "
                                + this.surname + "\n");
        }

}
