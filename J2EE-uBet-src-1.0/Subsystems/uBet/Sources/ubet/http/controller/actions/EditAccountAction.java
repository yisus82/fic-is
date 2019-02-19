package ubet.http.controller.actions;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ubet.http.controller.session.SessionManager;
import ubet.model.account.to.AccountTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class EditAccountAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Fill "form". */
                DynaActionForm accountForm = (DynaActionForm) form;

                /*
                 * If the request is to allow the user to correct errors in the
                 * form, "accountForm" must not be modified.
                 */
                String action = (String) accountForm.get("action");

                if (request.getAttribute(Globals.ERROR_KEY) == null) {

                        /*
                         * If the user is updating his/her account, set the rest
                         * of attributes.
                         */

                        if ("UPDATE".equals(action)) {

                                UserFacadeDelegate userFacadeDelegate = SessionManager
                                                .getUserFacadeDelegate(request);

                                HttpSession session = request.getSession(true);
                                Long accountID = (Long) session
                                                .getAttribute(SessionManager.ACCOUNT_SESSION_ATTRIBUTE);

                                AccountTO accountTO;
                                try {
                                        accountTO = userFacadeDelegate
                                                        .findAccount(accountID);
                                } catch (InstanceNotFoundException e) {
                                        throw new InternalErrorException(e);
                                }

                                accountForm.set("accountID", accountTO
                                                .getAccountID().toString());

                                accountForm.set("creditCardNumber", accountTO
                                                .getCreditCardNumber());

                                Calendar expirationDate = accountTO
                                                .getExpirationDate();
                                /* January = 0. */
                                accountForm
                                                .set(
                                                                "endMonth",
                                                                (new Integer(
                                                                                expirationDate
                                                                                                .get(Calendar.MONTH) + 1)
                                                                                .toString()
                                                                                .toString()));
                                accountForm
                                                .set(
                                                                "endYear",
                                                                (new Integer(
                                                                                expirationDate
                                                                                                .get(Calendar.YEAR))
                                                                                .toString())
                                                                                .toString());

                                accountForm.set("balance", accountTO
                                                .getBalance().toString());

                        }

                }

                /* Return ActionForward. */
                if ("UPDATE".equals(action)) {
                        return mapping.findForward("UpdateAccountDetailsForm");
                }
                return mapping.findForward("CreateAccountForm");
        }

}
