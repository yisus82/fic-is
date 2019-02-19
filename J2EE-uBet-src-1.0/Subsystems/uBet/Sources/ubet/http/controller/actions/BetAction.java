package ubet.http.controller.actions;

import java.io.IOException;
import java.util.Calendar;

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
import ubet.model.bet.to.BetTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.exceptions.BetOutOfTimeException;
import ubet.model.userfacade.exceptions.InsufficientBalanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class BetAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                DynaActionForm betForm = (DynaActionForm) form;
                Long eventID = new Long((String) betForm.get("eventID"));
                Long betTypeID = new Long((String) betForm.get("betTypeID"));
                Long betOptionID = new Long((String) betForm.get("betOptionID"));
                Double amount = new Double((String) betForm.get("amount"));
                UserFacadeDelegate userFacadeDelegate = SessionManager
                                .getUserFacadeDelegate(request);
                HttpSession session = request.getSession(true);
                Long accountID = (Long) session
                                .getAttribute(SessionManager.ACCOUNT_SESSION_ATTRIBUTE);

                BetTO betTO = new BetTO(new Long("-1"), betTypeID, betOptionID,
                                accountID, eventID, amount, Calendar
                                                .getInstance(), BetTO.PENDING);

                /* Insert bet. */
                ActionMessages errors = new ActionMessages();

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

                        userFacadeDelegate.bet(betTO);
                } catch (BetOutOfTimeException e) {
                        errors.add("balance", new ActionMessage(
                                        "ErrorMessages.bet.outOfTime"));
                } catch (InsufficientBalanceException e) {
                        errors
                                        .add(
                                                        "amount",
                                                        new ActionMessage(
                                                                        "ErrorMessages.bet.insufficientBalance"));
                }

                /* Return ActionForward. */
                if (errors.isEmpty())
                        return mapping.findForward("MainPage");
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
        }

}
