package ubet.http.controller.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

import ubet.http.controller.session.SessionManager;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class InsertEventAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                DynaActionForm insertEventForm = (DynaActionForm) form;
                Long questionID = new Long((String) insertEventForm
                                .get("questionID"));
                Calendar date = Calendar.getInstance();
                Integer year = new Integer((String) insertEventForm.get("year"));
                Integer month = new Integer((String) insertEventForm
                                .get("month"));
                Integer day = new Integer((String) insertEventForm.get("day"));
                Integer hour = new Integer((String) insertEventForm.get("hour"));
                Integer minutes = new Integer((String) insertEventForm
                                .get("minutes"));
                HttpSession session = request.getSession(true);
                List<BetOptionTO> options = (List<BetOptionTO>) session
                                .getAttribute(SessionManager.OPTIONS_SESSION_ATTRIBUTE);

                if (options == null)
                        options = new ArrayList<BetOptionTO>();
                request.setAttribute("questionDescription", insertEventForm
                                .get("questionDescription"));
                ActionMessages errors;
                if (request.getParameter("insert") != null) {
                        /* January = 0. */
                        date.set(year, month - 1, day, hour, minutes, 0);
                        errors = new ActionMessages();
                        if (date.before(Calendar.getInstance())) {
                                errors
                                                .add(
                                                                "date",
                                                                new ActionMessage(
                                                                                "ErrorMessages.date.upcomingDate"));

                                saveErrors(request, errors);

                                return new ActionForward(mapping.getInput());
                        }

                        if (!validDate(day, month, year)) {
                                errors
                                                .add(
                                                                "date",
                                                                new ActionMessage(
                                                                                "ErrorMessages.date.invalidDate"));

                                saveErrors(request, errors);

                                return new ActionForward(mapping.getInput());
                        }

                        if (!validTime(hour, minutes)) {
                                errors
                                                .add(
                                                                "minutes",
                                                                new ActionMessage(
                                                                                "ErrorMessages.date.invalidTime"));

                                saveErrors(request, errors);

                                return new ActionForward(mapping.getInput());
                        }

                        boolean highlighted = insertEventForm
                                        .get("highlighted") != null;
                        EventTO eventTO;
                        if (highlighted) {
                                eventTO = new EventTO(
                                                new Long("-1"),
                                                (String) insertEventForm
                                                                .get("eventDescription"),
                                                date,
                                                (String) insertEventForm
                                                                .get("category"),
                                                new Long("-1"), EventTO.READY,
                                                Calendar.getInstance());
                        } else {
                                eventTO = new EventTO(
                                                new Long("-1"),
                                                (String) insertEventForm
                                                                .get("eventDescription"),
                                                date,
                                                (String) insertEventForm
                                                                .get("category"),
                                                new Long("-1"), EventTO.NO,
                                                Calendar.getInstance());
                        }

                        String optionDescription = (String) insertEventForm
                                        .get("optionDescription");
                        Double odds = new Double((String) insertEventForm
                                        .get("odds"));

                        errors = new ActionMessages();

                        if (odds <= 0) {

                                errors
                                                .add(
                                                                "odds",
                                                                new ActionMessage(
                                                                                "ErrorMessages.number.positive"));

                                saveErrors(request, errors);

                                return new ActionForward(mapping.getInput());

                        }

                        options.add(new BetOptionTO(new Long("-1"),
                                        optionDescription, odds,
                                        new Long("-1"), BetOptionTO.PENDING));

                        BetTypeTO betTypeTO = new BetTypeTO(new Long("-1"),
                                        new Long("-1"), questionID);
                        SessionManager.saveOptions(request, options);

                        /* Insert event. */
                        try {
                                SessionManager.insertEvent(request, response,
                                                eventTO, betTypeTO);
                        } catch (DuplicateInstanceException e) {
                                errors
                                                .add(
                                                                "event",
                                                                new ActionMessage(
                                                                                "ErrorMessages.event.alreadyExists"));
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

                /* "Next option" button. */
                date = Calendar.getInstance();
                /* January = 0. */
                date.set(year, month - 1, day, hour, minutes, 0);
                errors = new ActionMessages();
                if (date.before(Calendar.getInstance())) {
                        errors.add("date", new ActionMessage(
                                        "ErrorMessages.date.upcomingDate"));

                        saveErrors(request, errors);

                        return new ActionForward(mapping.getInput());
                }

                if (!validDate(day, month, year)) {
                        errors.add("date", new ActionMessage(
                                        "ErrorMessages.date.invalidDate"));

                        saveErrors(request, errors);

                        return new ActionForward(mapping.getInput());
                }

                if (!validTime(hour, minutes)) {
                        errors.add("minutes", new ActionMessage(
                                        "ErrorMessages.date.invalidTime"));

                        saveErrors(request, errors);

                        return new ActionForward(mapping.getInput());
                }

                String optionDescription = (String) insertEventForm
                                .get("optionDescription");
                Double odds = new Double((String) insertEventForm.get("odds"));

                errors = new ActionMessages();

                if (odds <= 0) {

                        errors.add("odds", new ActionMessage(
                                        "ErrorMessages.number.positive"));

                        saveErrors(request, errors);

                        return new ActionForward(mapping.getInput());

                }
                options.add(new BetOptionTO(new Long("-1"), optionDescription,
                                odds, new Long("-1"), BetOptionTO.PENDING));
                SessionManager.saveOptions(request, options);
                insertEventForm.set("optionDescription", null);
                insertEventForm.set("odds", null);
                return mapping.findForward("InsertEvent");
        }

        private boolean validDate(Integer day, Integer month, Integer year) {
                if ((day < 1) || (month < 1) || (day > 31) || (month > 12))
                        return false;
                else if ((day == 31)
                                && ((month == 2) || (month == 4)
                                                || (month == 6) || (month == 9) || (month == 11)))
                        return false;
                else if ((day == 30) && (month == 2))
                        return false;
                else if ((day == 29) && (month == 2))
                        if ((year % 4) != 0)
                                return false;
                        else if (((year % 100) == 0) && ((year % 1000) != 0))
                                return false;

                return true;
        }

        private boolean validTime(Integer hour, Integer minutes) {
                if ((hour < 0) || (minutes < 0) || (hour > 23)
                                || (minutes > 59))
                        return false;

                return true;
        }

}
