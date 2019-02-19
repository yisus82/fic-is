package ubet.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.adminfacade.delegate.AdminFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class SetHighlightedAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Set highlighted. */
                AdminFacadeDelegate adminFacadeDelegate = AdminFacadeDelegateFactory
                                .getDelegate();

                ActionMessages errors = new ActionMessages();

                try {
                        adminFacadeDelegate.setHighlighted(new Long(request
                                        .getParameter("eventID")), new Boolean(
                                        request.getParameter("highlighted")));
                } catch (InstanceNotFoundException e) {
                        errors.add("eventID", new ActionMessage(
                                        "ErrorMessages.event.notFound"));
                }

                /* Return ActionForward. */
                return mapping.findForward("MainPage");
        }

}
