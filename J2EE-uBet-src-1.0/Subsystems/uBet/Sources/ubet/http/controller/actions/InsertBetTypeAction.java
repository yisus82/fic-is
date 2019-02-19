package ubet.http.controller.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import ubet.http.controller.caches.CategoryCache;
import ubet.http.controller.session.SessionManager;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class InsertBetTypeAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                DynaActionForm insertBetTypeForm = (DynaActionForm) form;
                Long eventID = new Long((String) insertBetTypeForm
                                .get("eventID"));
                Long questionID = new Long((String) insertBetTypeForm
                                .get("questionID"));
                /*
                 * Insert list of category questions as attributes in the
                 * "request".
                 */
                String categoryID = (String) insertBetTypeForm
                                .get("categoryID");
                request.setAttribute("questions", CategoryCache
                                .getQuestionsByCategoryIdentifier(categoryID));
                request.setAttribute("eventDescription", insertBetTypeForm
                                .get("eventDescription"));
                HttpSession session = request.getSession(true);
                List<BetOptionTO> options = (List<BetOptionTO>) session
                                .getAttribute(SessionManager.OPTIONS_SESSION_ATTRIBUTE);
                if (options == null)
                        options = new ArrayList<BetOptionTO>();
                if (request.getParameter("insert") != null) {
                        String optionDescription = (String) insertBetTypeForm
                                        .get("optionDescription");
                        Double odds = new Double((String) insertBetTypeForm
                                        .get("odds"));

                        ActionMessages errors = new ActionMessages();

                        if (odds <= 0) {

                                errors
                                                .add(
                                                                "odds",
                                                                new ActionMessage(
                                                                                "ErrorMessages.number.positive"));
                                /*
                                 * Remove the last bet option to avoid
                                 * duplicates
                                 */
                                options.remove(options.size() - 1);
                                saveErrors(request, errors);

                                return new ActionForward(mapping.getInput());

                        }

                        options.add(new BetOptionTO(new Long("-1"),
                                        optionDescription, odds,
                                        new Long("-1"), BetOptionTO.PENDING));

                        BetTypeTO betTypeTO = new BetTypeTO(new Long("-1"),
                                        eventID, questionID);
                        SessionManager.saveOptions(request, options);

                        /* Insert event. */
                        try {
                                SessionManager.insertBetType(request, response,
                                                betTypeTO);
                        } catch (DuplicateInstanceException e) {
                                errors
                                                .add(
                                                                "bet type",
                                                                new ActionMessage(
                                                                                "ErrorMessages.betType.alreadyExists"));
                        }

                        /* Return ActionForward. */
                        if (errors.isEmpty()) {
                                session
                                                .setAttribute(
                                                                SessionManager.OPTIONS_SESSION_ATTRIBUTE,
                                                                null);
                                return mapping.findForward("MainPage");
                        }
                        saveErrors(request, errors);

                        /* Remove the last bet option to avoid duplicates */
                        options.remove(options.size() - 1);
                        SessionManager.saveOptions(request, options);

                        return new ActionForward(mapping.getInput());
                }

                String optionDescription = (String) insertBetTypeForm
                                .get("optionDescription");
                Double odds = new Double((String) insertBetTypeForm.get("odds"));

                ActionMessages errors = new ActionMessages();

                if (odds <= 0) {

                        errors.add("odds", new ActionMessage(
                                        "ErrorMessages.number.positive"));
                        /* Remove bet options to avoid duplicates */
                        options.removeAll(options);
                        saveErrors(request, errors);

                        return new ActionForward(mapping.getInput());

                }

                options.add(new BetOptionTO(new Long("-1"), optionDescription,
                                odds, new Long("-1"), BetOptionTO.PENDING));
                SessionManager.saveOptions(request, options);
                insertBetTypeForm.set("optionDescription", null);
                insertBetTypeForm.set("odds", null);
                return mapping.findForward("InsertBetType");
        }

}
