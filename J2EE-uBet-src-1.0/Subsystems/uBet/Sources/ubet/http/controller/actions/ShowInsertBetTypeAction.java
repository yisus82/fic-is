package ubet.http.controller.actions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ubet.http.controller.caches.CategoryCache;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class ShowInsertBetTypeAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /*
                 * Insert list of category questions as attributes in the
                 * "request".
                 */
                String categoryID = request.getParameter("categoryID");
                request.setAttribute("questions", CategoryCache
                                .getQuestionsByCategoryIdentifier(categoryID));

                /* Return ActionForward. */
                return mapping.findForward("InsertBetTypeForm");
        }

}
