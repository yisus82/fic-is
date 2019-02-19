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
import org.apache.struts.action.DynaActionForm;

import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.adminfacade.delegate.AdminFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class PublishResultsAction extends DefaultAction {

        protected ActionForward doExecute(ActionMapping mapping,
                        ActionForm form, HttpServletRequest request,
                        HttpServletResponse response) throws IOException,
                        ServletException, InternalErrorException {
                /* Get data. */
                DynaActionForm insertWinnerOptionsForm = (DynaActionForm) form;
                String[] winnerOptions = (String[]) insertWinnerOptionsForm
                                .get("selectedItems");

                List<Long> list = new ArrayList<Long>();
                for (String option : winnerOptions)
                        list.add(new Long(option));

                AdminFacadeDelegate adminFacadeDelegate = AdminFacadeDelegateFactory
                                .getDelegate();
                adminFacadeDelegate.publishResults(list);

                /* Return ActionForward. */
                return mapping.findForward("MainPage");
        }

}
