package ubet.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ubet.http.controller.session.SessionManager;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.to.AccountOperationChunkTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class ShowMyAccountOperationsAction extends DefaultAction {

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

                HttpSession session = request.getSession(true);
                Long accountID = (Long) session
                                .getAttribute(SessionManager.ACCOUNT_SESSION_ATTRIBUTE);

                /* Do find bets. */
                return doFindAccountOperations(mapping, accountID, startIndex,
                                count, request);
        }

        private ActionForward doFindAccountOperations(ActionMapping mapping,
                        Long accountID, int startIndex, int count,
                        HttpServletRequest request)
                        throws InternalErrorException {
                /* Find bets by events. */
                UserFacadeDelegate userFacadeDelegate = SessionManager
                                .getUserFacadeDelegate(request);

                AccountOperationChunkTO operations;
                try {
                        operations = userFacadeDelegate.findAccountOperations(
                                        accountID, startIndex, count, null,
                                        null);
                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                }

                if (operations.getAccountOperationTOs().size() > 0) {
                        request.setAttribute("operations", operations
                                        .getAccountOperationTOs());
                        if (operations.getExistMoreAccountOperations())
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
                return mapping.findForward("ShowMyAccountOperationsContent");
        }

}
