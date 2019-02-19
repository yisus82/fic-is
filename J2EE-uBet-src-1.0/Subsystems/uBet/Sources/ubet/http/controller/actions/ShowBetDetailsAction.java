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
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.to.BetStatusTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class ShowBetDetailsAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get initial data. */
                Long betID = new Long(request.getParameter("betID"));

                HttpSession session = request.getSession(true);
                Long accountID = (Long) session
                                .getAttribute(SessionManager.ACCOUNT_SESSION_ATTRIBUTE);

                /* Do find bet. */
                return doFindBet(mapping, accountID, betID, request);
        }

        private ActionForward doFindBet(ActionMapping mapping, Long accountID,
                        Long betID, HttpServletRequest request)
                        throws InternalErrorException {
                /* Find bet. */
                UserFacadeDelegate userFacadeDelegate = SessionManager
                                .getUserFacadeDelegate(request);

                List<BetStatusTO> bets = userFacadeDelegate.findBetsByAccount(
                                accountID, -1, -1, null, null)
                                .getBetStatusTOs();

                for (BetStatusTO bet : bets) {
                        if (bet.getBetID().equals(betID)) {
                                request.setAttribute("bet", bet);
                                break;
                        }
                }

                /* Return ActionForward. */
                return mapping.findForward("ShowBetDetailsContent");
        }

}
