package ubet.http.controller.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ubet.http.controller.session.SessionManager;
import ubet.model.account.to.AccountTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class ChangeCurrentAccountAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                UserFacadeDelegate userFacadeDelegate = SessionManager
                                .getUserFacadeDelegate(request);
                HttpSession session = request.getSession(true);
                Long accountID = (Long) session
                                .getAttribute(SessionManager.ACCOUNT_SESSION_ATTRIBUTE);
                List<AccountTO> accountTOs = userFacadeDelegate
                                .findAccountsByUser(-1, -1).getAccountTOs();

                /* Change current account. */
                int i = 0;
                for (AccountTO account : accountTOs) {
                        if (!account.getAccountID().equals(accountID))
                                i++;
                        else
                                break;
                }
                if ((accountTOs.size() - 1) == i)
                        SessionManager.saveAccount(request, accountTOs.get(0)
                                        .getAccountID());
                else
                        SessionManager.saveAccount(request, accountTOs.get(
                                        i + 1).getAccountID());

                /* Return ActionForward. */

                return new ActionForward("/ShowMyAccounts.do");
        }

}
