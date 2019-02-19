package ubet.http.controller.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ubet.model.betoption.to.BetOptionTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import ubet.model.searchfacade.to.EventResultTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class EventsXMLAction extends DefaultAction {

        private static final String ENCODING = "iso-8859-1";

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                response.setContentType("application/xml; charset=ISO-8859-1");
                PrintWriter writer = response.getWriter();

                String action = request.getParameter("action");
                SearchFacadeDelegate searchFacadeDelegate = SearchFacadeDelegateFactory
                                .getDelegate();

                List<EventResultTO> events = null;
                if ("HIGHLIGHTED".equals(action))
                        events = searchFacadeDelegate
                                        .findAllHighlightedEvents()
                                        .getEventTOs();
                else if ("RECENT".equals(action)) {
                        String categories = request.getParameter("categories");
                        String[] identifiers = categories.split(",");
                        List<String> categoryIDs = new ArrayList<String>();
                        for (String identifier : identifiers)
                                categoryIDs.add(identifier);
                        events = searchFacadeDelegate.findRecentEvents(
                                        categoryIDs, -1, -1).getEventTOs();
                }

                Document document = createDOM(events);

                DOMSource source = new DOMSource(document);

                StreamResult result = new StreamResult(writer);

                TransformerFactory transformerFactory = TransformerFactory
                                .newInstance();
                Transformer transformer = null;
                try {
                        transformer = transformerFactory.newTransformer();
                } catch (TransformerConfigurationException e) {
                        e.printStackTrace();
                        throw new InternalErrorException(e);
                }

                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.ENCODING, ENCODING);

                try {
                        transformer.transform(source, result);
                } catch (TransformerException e) {
                        throw new InternalErrorException(e);
                }

                writer.flush();
                writer.close();
                return null;
        }

        private static Document createDOM(List<EventResultTO> events)
                        throws InternalErrorException {
                DocumentBuilderFactory factory = DocumentBuilderFactory
                                .newInstance();
                Document document = null;
                try {
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        document = builder.newDocument();

                        Element root = document.createElement("events");
                        document.appendChild(root);
                        for (EventResultTO eventResult : events) {
                                Element event = document.createElement("event");
                                Element description = document
                                                .createElement("description");
                                description
                                                .appendChild(document
                                                                .createTextNode(eventResult
                                                                                .getDescription()));
                                Element category = document
                                                .createElement("category");
                                category.appendChild(document
                                                .createTextNode(eventResult
                                                                .getCategory()
                                                                .getName()));
                                Element date = document.createElement("date");
                                date.appendChild(document
                                                .createTextNode(eventResult
                                                                .getDate()
                                                                .getTime()
                                                                .toString()));
                                event.appendChild(description);
                                event.appendChild(category);
                                event.appendChild(date);
                                Element options = document
                                                .createElement("options");
                                for (BetOptionTO betOption : eventResult
                                                .getBetType().getOptions()) {
                                        Element option = document
                                                        .createElement("option");
                                        Element optionDescription = document
                                                        .createElement("optionDescription");
                                        optionDescription
                                                        .appendChild(document
                                                                        .createTextNode(betOption
                                                                                        .getDescription()
                                                                                        + "("
                                                                                        + betOption
                                                                                                        .getOdds()
                                                                                        + ")"));
                                        option.appendChild(optionDescription);
                                        options.appendChild(option);
                                }
                                event.appendChild(options);

                                root.appendChild(event);
                        }
                } catch (ParserConfigurationException e) {
                        throw new InternalErrorException(e);
                }

                return document;
        }

}