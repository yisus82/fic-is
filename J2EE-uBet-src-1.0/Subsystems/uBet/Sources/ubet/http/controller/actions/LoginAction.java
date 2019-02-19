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
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class LoginAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                DynaActionForm loginForm = (DynaActionForm) form;
                String login = (String) loginForm.get("login");
                String password = (String) loginForm.get("password");
                boolean rememberMyPassword = loginForm
                                .get("rememberMyPassword") != null;

                /* Do login. */
                ActionMessages errors = new ActionMessages();

                try {

                        SessionManager.login(request, response, login,
                                        password, rememberMyPassword);

                } catch (InstanceNotFoundException e) {
                        errors.add("login", new ActionMessage(
                                        "ErrorMessages.login.notFound"));
                } catch (IncorrectPasswordException e) {
                        errors.add("password", new ActionMessage(
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
