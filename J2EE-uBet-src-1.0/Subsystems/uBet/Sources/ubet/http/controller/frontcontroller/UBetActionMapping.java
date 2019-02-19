package ubet.http.controller.frontcontroller;

import org.apache.struts.action.ActionMapping;

public class UBetActionMapping extends ActionMapping {

        private boolean authenticationRequired;

        private boolean adminRequired;

        public UBetActionMapping() {
                authenticationRequired = false;
                adminRequired = false;
        }

        public boolean getAuthenticationRequired() {
                return authenticationRequired;
        }

        public void setAuthenticationRequired(boolean authenticationRequired) {
                this.authenticationRequired = authenticationRequired;
        }

        public boolean getAdminRequired() {
                return adminRequired;
        }

        public void setAdminRequired(boolean adminRequired) {
                this.adminRequired = adminRequired;
        }

}
