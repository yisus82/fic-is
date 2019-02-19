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
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class ChangePasswordAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                DynaActionForm changePasswordForm = (DynaActionForm) form;
                String oldPassword = (String) changePasswordForm
                                .get("oldPassword");
                String newPassword = (String) changePasswordForm
                                .get("newPassword");

                /* Do login. */
                ActionMessages errors = new ActionMessages();

                try {
                        SessionManager.changePassword(request, response,
                                        oldPassword, newPassword);
                } catch (IncorrectPasswordException e) {
                        errors.add("oldPassword", new ActionMessage(
                                        "ErrorMessages.password.incorrect"));
                }

                /* Return ActionForward. */
                if (errors.isEmpty()) {
                        return mapping.findForward("MainPage");
                }
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
        }

}
