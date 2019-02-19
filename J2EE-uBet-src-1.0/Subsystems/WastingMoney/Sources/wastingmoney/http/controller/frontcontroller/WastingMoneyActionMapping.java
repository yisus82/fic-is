package wastingmoney.http.controller.frontcontroller;

import org.apache.struts.action.ActionMapping;

public class WastingMoneyActionMapping extends ActionMapping {

        private boolean authenticationRequired;

        public WastingMoneyActionMapping() {
                authenticationRequired = false;
        }

        public boolean getAuthenticationRequired() {
                return authenticationRequired;
        }

        public void setAuthenticationRequired(boolean authenticationRequired) {
                this.authenticationRequired = authenticationRequired;
        }

}
