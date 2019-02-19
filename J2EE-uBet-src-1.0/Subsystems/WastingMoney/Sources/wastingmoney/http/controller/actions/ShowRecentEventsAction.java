package wastingmoney.http.controller.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class ShowRecentEventsAction extends DefaultAction {

        private static final String STYLESHEET = "events.xsl";

        /* For the plain version of uBet */
        // private static final String SOURCE =
        // "http://localhost:8080/PlainuBet/EventsXML.do?action=RECENT";
        /* For the EJB version of uBet */
        private static final String SOURCE = "http://localhost:8080/EJBuBet/EventsXML.do?action=RECENT";

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {

                String xslPath = request.getSession().getServletContext()
                                .getRealPath("/xslt/" + STYLESHEET);

                StreamSource xslSource = new StreamSource(xslPath);

                List<String> categories = new ArrayList<String>();
                categories.add("FootEs1d");
                categories.add("FootEnPrem");

                String categoryIDs = "";
                for (String category : categories)
                        categoryIDs += category + ",";
                categoryIDs = categoryIDs
                                .substring(0, categoryIDs.length() - 1);

                String xmlPath = SOURCE + "&categories=" + categoryIDs;

                request.setAttribute("xsl", xslSource);

                request.setAttribute("xml", xmlPath);

                return mapping.findForward("ShowRecentEventsContent");
        }
}
