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
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class FindFavoritesAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get initial data. */
                HttpSession session = request.getSession(true);
                Long accountID = (Long) session
                                .getAttribute(SessionManager.ACCOUNT_SESSION_ATTRIBUTE);

                /* Do find favorites. */
                return doFindFavorites(mapping, accountID, request);
        }

        private ActionForward doFindFavorites(ActionMapping mapping,
                        Long accountID, HttpServletRequest request)
                        throws InternalErrorException {
                /* Find favorites. */
                UserFacadeDelegate userFacadeDelegate = SessionManager
                                .getUserFacadeDelegate(request);

                List<String> categories;
                try {
                        categories = userFacadeDelegate
                                        .findFavorites(accountID);
                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                }

                String categoryIDs = "";
                for (String category : categories)
                        categoryIDs += category + ",";
                if (categoryIDs != "")
                        categoryIDs = categoryIDs.substring(0, categoryIDs
                                        .length() - 1);

                /* Return ActionForward. */
                return new ActionForward("/FindRecentEvents.do?categories="
                                + categoryIDs);
        }

}
