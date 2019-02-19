package ubet.http.controller.actions;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ubet.http.controller.session.SessionManager;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import ubet.model.searchfacade.to.EventChunkTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class FindEventsAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get initial data. */
                DynaActionForm findEventsForm = (DynaActionForm) form;

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

                /* Get data. */
                String categoryID = (String) findEventsForm.get("parent");
                if ((categoryID == null) || (categoryID.equals(""))) {
                        categoryID = request.getParameter("identifier");
                }

                /* Do find events by category. */
                return doFindEventsByCategory(mapping, categoryID, startIndex,
                                count, request);
        }

        private ActionForward doFindEventsByCategory(ActionMapping mapping,
                        String categoryID, int startIndex, int count,
                        HttpServletRequest request)
                        throws InternalErrorException {
                /* Find events by category. */
                SearchFacadeDelegate searchFacadeDelegate = SearchFacadeDelegateFactory
                                .getDelegate();

                EventChunkTO events;
                if (SessionManager.isAdmin(request))
                        events = searchFacadeDelegate.findEventsByCategory(
                                        categoryID, startIndex, count, null,
                                        null);
                else
                        events = searchFacadeDelegate.findEventsByCategory(
                                        categoryID, startIndex, count, Calendar
                                                        .getInstance(), null);

                if (events.getEventTOs().size() > 0) {
                        request.setAttribute("events", events.getEventTOs());

                        if (events.getExistMoreEvents())
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
                return mapping.findForward("FindEventsContent");
        }

}
