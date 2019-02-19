package ibei.model.userprofile.to;

import java.io.Serializable;

public class UserProfileDetailsTO implements Serializable {

        private String firstName;

        private String surname;

        private String email;

        private String street;

        private String city;

        private String state;

        private Short postalCode;

        private String countryID;

        public UserProfileDetailsTO(String firstName, String surname,
                        String email, String street, String city, String state,
                        Short postalCode, String countryID) {
                this.firstName = firstName;
                this.surname = surname;
                this.email = email;
                this.street = street;
                this.city = city;
                this.state = state;
                this.postalCode = postalCode;
                this.countryID = countryID;
        }

        public String getFirstName() {
                return this.firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getSurname() {
                return this.surname;
        }

        public void setSurname(String surname) {
                this.surname = surname;
        }

        public String getEmail() {
                return this.email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getStreet() {
                return this.street;
        }

        public void setStreet(String street) {
                this.street = street;
        }

        public String getCity() {
                return this.city;
        }

        public void setCity(String city) {
                this.city = city;
        }

        public String getState() {
                return this.state;
        }

        public void setState(String state) {
                this.state = state;
        }

        public Short getPostalCode() {
                return this.postalCode;
        }

        public void setPostalCode(Short postalCode) {
                this.postalCode = postalCode;
        }

        public String getCountryID() {
                return this.countryID;
        }

        public void setCountryID(String countryID) {
                this.countryID = countryID;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof UserProfileDetailsTO) {
                        UserProfileDetailsTO details = (UserProfileDetailsTO) object;
                        return (details.getCity().equals(city)
                                        && details.getCountryID().equals(
                                                        countryID)
                                        && details.getEmail().equals(email)
                                        && details.getFirstName().equals(
                                                        firstName)
                                        && details.getPostalCode().equals(
                                                        postalCode)
                                        && details.getState().equals(state)
                                        && details.getStreet().equals(street) && details
                                        .getSurname().equals(surname));
                }

                return false;

        }

        public String toString() {
                return ("\nCity = " + this.city + "\nCountryID = "
                                + this.countryID + "\nEmail = " + this.email
                                + "\nFirst name = " + this.firstName
                                + "\nPostal code = " + this.postalCode
                                + "\nState = " + this.state + "\nStreet = "
                                + this.street + "\nSurname = " + this.surname + "\n");
        }

}
