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
import ubet.model.account.to.AccountTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class UpdateAccountDetailsAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                DynaActionForm accountForm = (DynaActionForm) form;
                Calendar expirationDate = Calendar.getInstance();
                /* January = 0. */
                Integer endMonth = (new Integer((String) accountForm
                                .get("endMonth"))) - 1;
                Integer endYear = (new Integer((String) accountForm
                                .get("endYear")));
                expirationDate.set(Calendar.DAY_OF_MONTH, 1);
                expirationDate.set(Calendar.MONTH, endMonth);
                expirationDate.set(Calendar.YEAR, endYear);
                String creditCardNumber = (String) accountForm
                                .get("creditCardNumber");
                Double balance = new Double((String) accountForm.get("balance"));

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

                UserFacadeDelegate userFacadeDelegate = SessionManager
                                .getUserFacadeDelegate(request);
                UserProfileTO userProfileTO = SessionManager
                                .getUserFacadeDelegate(request).findUser();
                AccountTO accountTO = new AccountTO(new Long(
                                (String) accountForm.get("accountID")),
                                userProfileTO.getLogin(), creditCardNumber,
                                expirationDate, balance);

                /* Update account details. */
                errors = new ActionMessages();

                try {
                        userFacadeDelegate.updateAccountDetails(accountTO);
                } catch (InstanceNotFoundException e) {
                        errors.add("accountID", new ActionMessage(
                                        "ErrorMessages.account.notFound"));
                }

                /* Return ActionForward. */
                if (errors.isEmpty()) {
                        return mapping.findForward("MainPage");
                }
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
        }

}
