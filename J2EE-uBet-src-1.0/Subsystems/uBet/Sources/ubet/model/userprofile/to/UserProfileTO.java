package ubet.model.userprofile.to;

import java.io.Serializable;

/**
 * A transfer object representing the information of an user.
 */
public class UserProfileTO implements Serializable {

        /**
         * The user login (identifier)
         */
        private String login;

        /**
         * The password (normally encrypted) of the user
         */
        private String password;

        /**
         * The details of the user
         */
        private UserProfileDetailsTO userDetails;

        /**
         * A constructor to create <code>UserProfileTO</code> objects
         * 
         * @param login
         *                the user login (identifier)
         * @param password
         *                the password (normally encrypted) of the user
         * @param userDetails
         *                the details of the user
         */
        public UserProfileTO(String login, String password,
                        UserProfileDetailsTO userDetails) {
                this.login = login;
                this.password = password;
                this.userDetails = userDetails;
        }

        /**
         * @return the <code>login</code>
         */
        public String getLogin() {
                return this.login;
        }

        /**
         * @param login
         *                the login to set
         */
        public void setLogin(String login) {
                this.login = login;
        }

        /**
         * @return the <code>password</code>
         */
        public String getPassword() {
                return this.password;
        }

        /**
         * @param password
         *                the password to set
         */
        public void setPassword(String password) {
                this.password = password;
        }

        /**
         * @return the <code>userDetails</code>
         */
        public UserProfileDetailsTO getUserDetails() {
                return this.userDetails;
        }

        /**
         * @param userDetails
         *                the userDetails to set
         */
        public void setUserDetails(UserProfileDetailsTO userDetails) {
                this.userDetails = userDetails;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof UserProfileTO) {
                        UserProfileTO user = (UserProfileTO) object;
                        return (user.getLogin().equals(this.login)
                                        && user.getPassword().equals(
                                                        this.password) && user
                                        .getUserDetails().equals(
                                                        this.userDetails));
                }

                return false;
        }

        @Override
        public String toString() {
                return ("\nLogin = " + this.login + "\nPassword = "
                                + this.password + "\nUser details = "
                                + this.userDetails + "\n");
        }

}
