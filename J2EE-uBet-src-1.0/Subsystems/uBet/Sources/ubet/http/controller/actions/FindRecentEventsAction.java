package ubet.http.controller.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import ubet.model.searchfacade.to.EventChunkTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class FindRecentEventsAction extends DefaultAction {

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

                /* Get data. */
                String categories = request.getParameter("categories");

                /* Do find events by category. */
                return doFindRecentEvents(mapping, categories, startIndex,
                                count, request);
        }

        private ActionForward doFindRecentEvents(ActionMapping mapping,
                        String categories, int startIndex, int count,
                        HttpServletRequest request)
                        throws InternalErrorException {
                String[] identifiers = categories.split(",");
                List<String> categoryIDs = new ArrayList<String>();
                for (String identifier : identifiers)
                        categoryIDs.add(identifier);

                /* Find events by category. */
                SearchFacadeDelegate searchFacadeDelegate = SearchFacadeDelegateFactory
                                .getDelegate();

                EventChunkTO events = searchFacadeDelegate.findRecentEvents(
                                categoryIDs, startIndex, count);

                if (events.getEventTOs().size() > 0) {
                        request.setAttribute("categories", categories);

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
                return mapping.findForward("FindRecentEventsContent");
        }

}
