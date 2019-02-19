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

import ubet.model.event.to.EventTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import ubet.model.searchfacade.to.BetTypeChunkTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class ShowEventBetTypesAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get initial data. */
                DynaActionForm showEventBetTypesForm = (DynaActionForm) form;

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
                Long eventID = new Long((String) showEventBetTypesForm
                                .get("eventID"));
                if (eventID == null) {
                        eventID = new Long((String) request
                                        .getAttribute("eventID"));
                }

                /* Do find bet types by event. */
                return doFindBetTypesByEvent(mapping, eventID, startIndex,
                                count, request);
        }

        private ActionForward doFindBetTypesByEvent(ActionMapping mapping,
                        Long eventID, int startIndex, int count,
                        HttpServletRequest request)
                        throws InternalErrorException {
                /* Find bet types by events. */
                SearchFacadeDelegate searchFacadeDelegate = SearchFacadeDelegateFactory
                                .getDelegate();

                BetTypeChunkTO betTypes = searchFacadeDelegate
                                .findBetTypesByEvent(eventID, startIndex, count);
                EventTO event = searchFacadeDelegate.findEvent(eventID);

                if (betTypes.getBetTypeTOs().size() > 0) {
                        request.setAttribute("betTypes", betTypes
                                        .getBetTypeTOs());
                        if (!event.getDate().after(Calendar.getInstance()))
                                request.setAttribute("publish", true);
                        if (betTypes.getExistMoreBetTypes())
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
                return mapping.findForward("ShowEventBetTypesContent");
        }

}
