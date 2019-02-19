package ubet.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ubet.http.controller.session.SessionManager;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.to.AccountChunkTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class ShowMyAccountsAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get initial data. */
                int startIndex;
                int count;

                try {
                        startIndex = new Integer((String) request
                                        .getParameter("startIndex"));
                        count = new Integer((String) request
                                        .getParameter("count"));
                } catch (Exception e) {
                        startIndex = 1;
                        count = 5;
                }

                request.setAttribute("count", count);

                /* Do find accounts. */
                return doFindAccounts(mapping, startIndex, count, request);
        }

        private ActionForward doFindAccounts(ActionMapping mapping,
                        int startIndex, int count, HttpServletRequest request)
                        throws InternalErrorException {
                /* Find accounts by user. */
                UserFacadeDelegate userFacadeDelegate = SessionManager
                                .getUserFacadeDelegate(request);

                AccountChunkTO accounts = userFacadeDelegate
                                .findAccountsByUser(startIndex, count);

                if (accounts.getAccountTOs().size() > 0) {
                        request.setAttribute("accounts", accounts
                                        .getAccountTOs());
                        if (accounts.getExistMoreAccounts())
                                request
                                                .setAttribute(
                                                                "next",
                                                                startIndex
                                                                                + count);

                        if ((startIndex - count) > 1)
                                request.setAttribute("previous", startIndex
                                                - count);
                        else if (startIndex > 1)
                                request.setAttribute("previous", 1);
                }

                /* Return ActionForward. */
                return mapping.findForward("ShowMyAccountsContent");
        }

}
