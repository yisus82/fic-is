package ubet.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ubet.http.controller.caches.CountryCache;
import ubet.http.controller.session.SessionManager;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class EditUserProfileAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /*
                 * Insert list of countries (ordered by english) as attributes
                 * in the "request".
                 */
                request.setAttribute("countries", CountryCache
                                .getAllCountries());

                /* Fill "form". */
                DynaActionForm userProfileForm = (DynaActionForm) form;

                /*
                 * If the request is to allow the user to correct errors in the
                 * form, "userProfileForm" must not be modified.
                 */
                String action = (String) userProfileForm.get("action");

                if (request.getAttribute(Globals.ERROR_KEY) == null) {

                        /*
                         * If the user is updating his/her profile, set the rest
                         * of attributes.
                         */

                        if ("UPDATE".equals(action)) {

                                UserProfileTO userProfileTO = SessionManager
                                                .getUserFacadeDelegate(request)
                                                .findUser();
                                UserProfileDetailsTO userProfileDetailsTO = userProfileTO
                                                .getUserDetails();

                                userProfileForm.set("login", userProfileTO
                                                .getLogin());

                                userProfileForm
                                                .set(
                                                                "firstName",
                                                                userProfileDetailsTO
                                                                                .getFirstName());
                                userProfileForm.set("surname",
                                                userProfileDetailsTO
                                                                .getSurname());
                                userProfileForm
                                                .set(
                                                                "email",
                                                                userProfileDetailsTO
                                                                                .getEmail());
                                userProfileForm
                                                .set(
                                                                "countryID",
                                                                userProfileDetailsTO
                                                                                .getCountryID());

                        }

                }

                /* Return ActionForward. */
                if ("UPDATE".equals(action)) {
                        return mapping
                                        .findForward("UpdateUserProfileDetailsForm");
                }
                return mapping.findForward("RegisterUserForm");
        }

}
