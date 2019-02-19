package ubet.http.controller.caches;

import java.util.Iterator;
import java.util.List;

import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * This class is a cache of the 'Question' table of the database. It has been
 * designed as an utility class.
 */
public class QuestionCache {
        private static List<QuestionTO> questions;

        /*
         * The constructor has been modified with private scope to avoid the
         * instantiation of this utility class.
         */
        private QuestionCache() {
        }

        public static QuestionTO getQuestion(Long questionID)
                        throws InternalErrorException {
                if (questions == null) {
                        createQuestions();
                }

                for (Iterator i = questions.iterator(); i.hasNext();) {
                        QuestionTO q = (QuestionTO) i.next();
                        if (q.getQuestionID().equals(questionID))
                                return q;
                }

                return null;
        }

        public static List<QuestionTO> getAllQuestions()
                        throws InternalErrorException {
                if (questions == null) {
                        createQuestions();
                }
                return questions;
        }

        private static void createQuestions() throws InternalErrorException {
                SearchFacadeDelegate searchFacadeDelegate = SearchFacadeDelegateFactory
                                .getDelegate();

                questions = searchFacadeDelegate.getAllQuestions();
        }
}
