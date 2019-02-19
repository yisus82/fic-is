package ubet.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import ubet.http.controller.session.SessionManager;
import ubet.model.userprofile.to.UserProfileDetailsTO;
import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class UpdateUserProfileDetailsAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                DynaActionForm userProfileForm = (DynaActionForm) form;
                UserProfileDetailsTO userProfileDetailsTO = new UserProfileDetailsTO(
                                (String) userProfileForm.get("firstName"),
                                (String) userProfileForm.get("surname"),
                                (String) userProfileForm.get("email"),
                                (String) userProfileForm.get("countryID"));
                UserProfileTO userProfileTO = SessionManager
                                .getUserFacadeDelegate(request).findUser();
                userProfileTO.setLogin((String) userProfileForm.get("login"));
                userProfileTO.setUserDetails(userProfileDetailsTO);

                /* Update user profile details. */
                ActionMessages errors = new ActionMessages();

                try {
                        SessionManager.updateUserProfileDetails(request,
                                        userProfileTO);
                } catch (DuplicateInstanceException e) {
                        errors.add("login", new ActionMessage(
                                        "ErrorMessages.login.alreadyExists"));
                }

                /* Return ActionForward. */
                if (errors.isEmpty()) {
                        return mapping.findForward("MainPage");
                }
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
        }

}
