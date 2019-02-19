package ubet.model.searchfacade.ejb.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import ubet.model.category.entity.Category;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class GetAllCategoryQuestionsAction {

        private EntityManager entityManager;

        public GetAllCategoryQuestionsAction(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public Map<String, List<QuestionTO>> execute()
                        throws InternalErrorException {
                Map<String, List<QuestionTO>> categoryQuestions = new HashMap<String, List<QuestionTO>>();
                List<Category> categories = entityManager.createQuery(
                                "SELECT c FROM Category c WHERE c.leaf = 'N'"
                                                + "ORDER BY c.categoryID")
                                .getResultList();
                for (Category category : categories)
                        categoryQuestions
                                        .put(
                                                        category
                                                                        .getCategoryID(),
                                                        SearchFacadeHelper
                                                                        .toQuestionTOs(category
                                                                                        .getQuestions()));
                return categoryQuestions;
        }

}
