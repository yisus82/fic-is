package ibei.model.userprofile.to;

import java.io.Serializable;

public class UserProfileTO implements Serializable {

        public static final String BUYER = "B";

        public static final String SELLER = "S";

        private String login;

        private String password;

        private String type;

        private UserProfileDetailsTO userProfileDetails;

        public UserProfileTO(String login, String password,
                        UserProfileDetailsTO userProfileDetails) {
                this.login = login;
                this.password = password;
                this.userProfileDetails = userProfileDetails;
                this.type = BUYER;
        }

        public String getLogin() {
                return this.login;
        }

        public void setLogin(String login) {
                this.login = login;
        }

        public String getPassword() {
                return this.password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getType() {
                return this.type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public UserProfileDetailsTO getUserProfileDetails() {
                return this.userProfileDetails;
        }

        public void setUserProfileDetails(
                        UserProfileDetailsTO userProfileDetails) {
                this.userProfileDetails = userProfileDetails;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof UserProfileTO) {
                        UserProfileTO user = (UserProfileTO) object;
                        return (user.getLogin().equals(login)
                                        && user.getPassword().equals(password)
                                        && user.getType().equals(type) && user
                                        .getUserProfileDetails().equals(
                                                        userProfileDetails));
                }

                return false;
        }

        public String toString() {
                return ("\nLogin = " + this.login + "\nPassword = "
                                + this.password + "\nType = " + this.type
                                + "\nDetails = " + this.userProfileDetails + "\n");
        }

}
