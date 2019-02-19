package ubet.http.controller.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ubet.http.controller.caches.CategoryCache;
import ubet.http.controller.caches.QuestionCache;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.to.CategoryChunkTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.struts.action.DefaultAction;

public class FindChildrenOfCategoryAction extends DefaultAction {

        public ActionForward doExecute(ActionMapping mapping, ActionForm form,
                        HttpServletRequest request, HttpServletResponse response)
                        throws IOException, ServletException,
                        InternalErrorException {
                /* Get data. */
                DynaActionForm findChildrenOfCategoryForm = (DynaActionForm) form;
                String parentID = null;
                if (form != null) {
                        parentID = (String) findChildrenOfCategoryForm
                                        .get("parent");
                }
                if ((parentID == null) || (parentID.equals("")))
                        parentID = CategoryCache.getRootCategory()
                                        .getCategoryID();

                try {
                        return doFindEventsByCategory(mapping, parentID,
                                        request);
                } catch (InstanceNotFoundException e) {
                        return mapping.getInputForward();
                }
        }

        private ActionForward doFindEventsByCategory(ActionMapping mapping,
                        String parentID, HttpServletRequest request)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                List<CategoryChunkTO> chunkCategories = CategoryCache
                                .getCategoryByIdentifier(parentID)
                                .getChildren();

                QuestionTO question = QuestionCache.getQuestion(CategoryCache
                                .getCategoryByIdentifier(parentID)
                                .getQuestionID());
                if (question != null) {
                        request.setAttribute("questionID", question
                                        .getQuestionID().toString());
                        request.setAttribute("questionDescription", question
                                        .getDescription());
                }

                if (chunkCategories.size() > 0) {
                        request.setAttribute("categories", chunkCategories);
                        return mapping.findForward("ChildrenOfCategory");
                }
                return mapping.findForward("FindEvents");
        }

}
