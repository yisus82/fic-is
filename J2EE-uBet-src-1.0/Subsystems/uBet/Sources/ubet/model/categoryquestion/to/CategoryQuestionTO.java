package ubet.model.categoryquestion.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ubet.model.question.to.QuestionTO;

public class CategoryQuestionTO implements Serializable {

        private Long categoryQuestionID;

        private String categoryID;

        private List<QuestionTO> questions;

        public CategoryQuestionTO(String categoryID) {
                this.categoryID = categoryID;
                this.questions = new ArrayList<QuestionTO>();
        }

        public Long getCategoryQuestionID() {
                return categoryQuestionID;
        }

        public String getCategoryID() {
                return categoryID;
        }

        public void setCategoryID(String categoryID) {
                this.categoryID = categoryID;
        }

        public List<QuestionTO> getQuestions() {
                return questions;
        }

        public void setQuestions(List<QuestionTO> questions) {
                this.questions = questions;
        }

        public void addQuestion(QuestionTO question) {
                questions.add(question);
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof CategoryQuestionTO) {
                        CategoryQuestionTO categoryQuestion = (CategoryQuestionTO) object;
                        return (categoryQuestion.getCategoryID().equals(
                                        this.categoryID)
                                        && categoryQuestion
                                                        .getCategoryQuestionID()
                                                        .equals(
                                                                        this.categoryQuestionID) && categoryQuestion
                                        .getQuestions().equals(questions));
                }

                return false;
        }

        @Override
        public String toString() {
                String string = "\nCategory ID = " + this.categoryID;
                string += "\nCategory Question ID = " + this.categoryQuestionID;
                if (questions != null)
                        string += "\nQuestions = " + this.questions;
                string += "\n";
                return string;
        }

}
