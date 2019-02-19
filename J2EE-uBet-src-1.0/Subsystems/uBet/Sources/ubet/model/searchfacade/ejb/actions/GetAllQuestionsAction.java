package ubet.model.searchfacade.ejb.actions;

import java.util.List;

import javax.persistence.EntityManager;

import ubet.model.question.entity.Question;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class GetAllQuestionsAction {

        private EntityManager entityManager;

        public GetAllQuestionsAction(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public List<QuestionTO> execute() throws InternalErrorException {
                List<Question> questions = entityManager.createQuery(
                                "SELECT q FROM Question q "
                                                + "ORDER BY q.questionID")
                                .getResultList();

                return SearchFacadeHelper.toQuestionTOs(questions);
        }

}
