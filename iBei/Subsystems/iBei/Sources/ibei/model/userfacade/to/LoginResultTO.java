package ibei.model.userfacade.to;

import java.io.Serializable;

public class LoginResultTO implements Serializable {

        private String login;

        private String password;

        private String type;

        private String countryID;

        public LoginResultTO(String login, String password, String type,
                        String countryID) {
                this.login = login;
                this.password = password;
                this.type = type;
                this.countryID = countryID;
        }

        public String getLogin() {
                return this.login;
        }

        public String getPassword() {
                return this.password;
        }

        public String getType() {
                return this.type;
        }

        public String getCountryID() {
                return this.countryID;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof LoginResultTO) {
                        LoginResultTO result = (LoginResultTO) object;
                        return (result.getCountryID().equals(countryID)
                                        && result.getLogin().equals(login)
                                        && result.getPassword()
                                                        .equals(password) && result
                                        .getType().equals(type));
                }

                return false;

        }

}
