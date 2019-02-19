package ubet.http.controller.frontcontroller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import ubet.http.controller.session.SessionManager;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A filter to check if the action to be executed requires that the user had
 * been authenticated. If the user has not been authenticated and the action
 * requires it, <code>doProcess</code> returns the <code>ActionForward</code>
 * returned by <code>mapping.findForward("AuthenticationPage")</code>.
 */
public class AuthenticationPreProcessingFilter extends PreProcessingFilter {

        public AuthenticationPreProcessingFilter(PreProcessingFilter nextFilter) {
                super(nextFilter);
        }

        protected ActionForward doProcess(HttpServletRequest request,
                        HttpServletResponse response, Action action,
                        ActionForm form, ActionMapping mapping)
                        throws IOException, ServletException,
                        InternalErrorException {
                UBetActionMapping uBetActionMapping = (UBetActionMapping) mapping;

                if (uBetActionMapping.getAuthenticationRequired()) {

                        if (SessionManager.isUserAuthenticated(request)) {
                                return null;
                        }
                        return mapping.findForward("AuthenticationPage");

                }
                if (uBetActionMapping.getAdminRequired()) {

                        if (SessionManager.isAdmin(request)) {
                                return null;
                        }
                        return mapping.findForward("AuthenticationPage");

                }
                return null;
        }

}
