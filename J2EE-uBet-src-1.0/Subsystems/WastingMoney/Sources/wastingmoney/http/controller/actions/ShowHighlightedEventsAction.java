package wastingmoney.http.controller.actions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class ShowHighlightedEventsAction extends DefaultAction {

        private static final String STYLESHEET = "events.xsl";

        /* For the plain version of uBet */
        // private static final String SOURCE =
        // "http://localhost:8080/PlainuBet/EventsXML.do?action=HIGHLIGHTED";
        /* For the EJB version of uBet */
        private static final String SOURCE = "http://localhost:8080/EJBuBet/EventsXML.do?action=HIGHLIGHTED";

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                try {
                        TransformerFactory transformerFactory = TransformerFactory
                                        .newInstance();

                        String xslPath = request.getSession()
                                        .getServletContext().getRealPath(
                                                        "/xslt/" + STYLESHEET);

                        StreamSource xslSource = new StreamSource(xslPath);

                        StringWriter stringWriter = new StringWriter();
                        StreamResult streamResult = new StreamResult(
                                        stringWriter);

                        URLConnection connection = new URL(SOURCE)
                                        .openConnection();
                        Reader sourceXML = new InputStreamReader(connection
                                        .getInputStream());

                        Transformer transformer = transformerFactory
                                        .newTransformer(xslSource);

                        transformer.setOutputProperty(OutputKeys.ENCODING,
                                        "ISO-8859-1");
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        transformer.transform(new StreamSource(sourceXML),
                                        streamResult);
                        request.setAttribute("events", stringWriter.toString());

                        return mapping
                                        .findForward("ShowHighlightedEventsContent");
                } catch (TransformerException e) {
                        throw new InternalErrorException(e);
                }
        }
}
