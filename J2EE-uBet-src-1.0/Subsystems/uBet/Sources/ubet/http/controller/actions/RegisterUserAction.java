package ubet.http.controller.actions;

import java.io.IOException;
import java.util.Calendar;

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

public class RegisterUserAction extends DefaultAction {

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
                UserProfileTO userProfileTO = new UserProfileTO(
                                (String) userProfileForm.get("login"),
                                (String) userProfileForm.get("password"),
                                userProfileDetailsTO);

                Calendar expirationDate = Calendar.getInstance();
                /* January = 0. */
                Integer endMonth = (new Integer((String) userProfileForm
                                .get("endMonth"))) - 1;
                Integer endYear = (new Integer((String) userProfileForm
                                .get("endYear")));
                expirationDate.set(Calendar.DAY_OF_MONTH, 1);
                expirationDate.set(Calendar.MONTH, endMonth);
                expirationDate.set(Calendar.YEAR, endYear);
                String creditCardNumber = (String) userProfileForm
                                .get("creditCardNumber");
                Double balance = new Double((String) userProfileForm
                                .get("balance"));

                /* Register user. */
                ActionMessages errors = new ActionMessages();

                if (balance <= 0) {

                        errors.add("balance", new ActionMessage(
                                        "ErrorMessages.number.positive"));
                        saveErrors(request, errors);

                        return new ActionForward(mapping.getInput());

                }

                if (expirationDate.before(Calendar.getInstance())) {

                        errors.add("expirationDate", new ActionMessage(
                                        "ErrorMessages.date.upcomingDate"));
                        saveErrors(request, errors);

                        return new ActionForward(mapping.getInput());

                }

                try {

                        SessionManager.registerUser(request, userProfileTO,
                                        creditCardNumber, expirationDate,
                                        balance);
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
