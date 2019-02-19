package ubet.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import ubet.http.controller.session.SessionManager;
import ubet.model.accountoperation.to.AccountOperationTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class AddWithdrawAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                DynaActionForm addWithdrawForm = (DynaActionForm) form;
                HttpSession session = request.getSession(true);
                Long accountID = (Long) session
                                .getAttribute(SessionManager.ACCOUNT_SESSION_ATTRIBUTE);
                Double amount = new Double((String) addWithdrawForm
                                .get("amount"));
                String operationType = (String) addWithdrawForm
                                .get("operationType");

                /* Do action. */
                if (operationType.equals(AccountOperationTO.ADD_OPERATION)) {
                        return doAdd(mapping, accountID, amount, request);
                }
                return doWithdraw(mapping, accountID, amount, request);
        }

        private ActionForward doAdd(ActionMapping mapping, Long accountID,
                        Double amount, HttpServletRequest request)
                        throws InternalErrorException {
                ActionMessages errors = new ActionMessages();

                /* Add. */
                try {
                        if (amount <= 0) {

                                errors
                                                .add(
                                                                "amount",
                                                                new ActionMessage(
                                                                                "ErrorMessages.number.positive"));
                                saveErrors(request, errors);

                                return new ActionForward(mapping.getInput());

                        }

                        UserFacadeDelegate userFacadeDelegate = SessionManager
                                        .getUserFacadeDelegate(request);

                        userFacadeDelegate.addToAccount(accountID, amount);

                        return mapping.findForward("MainPage");

                } catch (InstanceNotFoundException e) {

                        errors.add("accountID", new ActionMessage(
                                        "ErrorMessages.account.notFound"));
                        saveErrors(request, errors);

                        return new ActionForward(mapping.getInput());

                }
        }

        private ActionForward doWithdraw(ActionMapping mapping, Long accountID,
                        Double amount, HttpServletRequest request)
                        throws InternalErrorException {
                ActionMessages errors = new ActionMessages();

                /* Withdraw. */
                try {
                        if (amount <= 0) {

                                errors
                                                .add(
                                                                "amount",
                                                                new ActionMessage(
                                                                                "ErrorMessages.number.positive"));
                                saveErrors(request, errors);

                                return new ActionForward(mapping.getInput());

                        }
                        UserFacadeDelegate userFacadeDelegate = SessionManager
                                        .getUserFacadeDelegate(request);
                        userFacadeDelegate.withdrawFromAccount(accountID,
                                        amount);
                } catch (InstanceNotFoundException e) {
                        errors.add("accountID", new ActionMessage(
                                        "ErrorMessages.account.notFound"));

                } catch (InsufficientBalanceException e) {
                        errors
                                        .add(
                                                        "balance",
                                                        new ActionMessage(
                                                                        "ErrorMessages.balance.insufficientBalance"));
                }

                /* Return ActionForward. */
                if (errors.isEmpty()) {
                        return mapping.findForward("MainPage");
                }
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
        }

}
