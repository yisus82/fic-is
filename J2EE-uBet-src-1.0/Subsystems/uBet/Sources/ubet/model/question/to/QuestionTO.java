package ubet.model.question.to;

import java.io.Serializable;

/**
 * A transfer object representing the information of a question.
 */
public class QuestionTO implements Serializable {

        /**
         * The question identifier
         */
        private Long questionID;

        /**
         * The description of the question
         */
        private String description;

        /**
         * A constructor to create <code>QuestionTO</code> objects
         * 
         * @param questionID
         *                the question identifier
         * @param description
         *                the description of the question
         */
        public QuestionTO(Long questionID, String description) {
                this.questionID = questionID;
                this.description = description;
        }

        /**
         * @return the <code>questionID</code>
         */
        public Long getQuestionID() {
                return this.questionID;
        }

        /**
         * @param questionID
         *                the questionID to set
         */
        public void setQuestionID(Long questionID) {
                this.questionID = questionID;
        }

        /**
         * @return the <code>description</code>
         */
        public String getDescription() {
                return this.description;
        }

        /**
         * @param description
         *                the description to set
         */
        public void setDescription(String description) {
                this.description = description;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof QuestionTO) {
                        QuestionTO question = (QuestionTO) object;
                        return (question.getDescription().equals(
                                        this.description) && question
                                        .getQuestionID()
                                        .equals(this.questionID));
                }

                return false;
        }

        @Override
        public String toString() {
                return ("\nDescription = " + this.description
                                + "\nQuestionID = " + this.questionID + "\n");
        }

}
