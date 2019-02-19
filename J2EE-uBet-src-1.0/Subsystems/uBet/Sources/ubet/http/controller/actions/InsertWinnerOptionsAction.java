package ubet.http.controller.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ubet.model.betoption.to.BetOptionTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class InsertWinnerOptionsAction extends DefaultAction {

        protected ActionForward doExecute(ActionMapping mapping,
                        ActionForm form, HttpServletRequest request,
                        HttpServletResponse response) throws IOException,
                        ServletException, InternalErrorException {
                SearchFacadeDelegate searchFacadeDelegate = SearchFacadeDelegateFactory
                                .getDelegate();
                List<BetOptionTO> betOptions = searchFacadeDelegate
                                .findBetOptions(new Long(request
                                                .getParameter("betTypeID")),
                                                -1, -1);

                request.setAttribute("betOptions", betOptions);

                /* Return ActionForward. */
                return mapping.findForward("InsertWinnerOptions");
        }

}
