package ubet.model.question.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import ubet.model.bettype.entity.BetType;
import ubet.model.category.entity.Category;

/**
 * An entity representing the information of a question.
 */
@Entity
public class Question {
        /**
         * NOTE: this entity class does not contain a "version" property since
         * its instances are never updated after being persisted.
         */

        /**
         * The question identifier
         */
        private Long questionID;

        /**
         * The description of the question
         */
        private String description;

        /**
         * The categories associated with the question
         */
        private List<Category> categories;

        /**
         * The bet types associated with the question
         */
        private List<BetType> betTypes;

        public Question() {
                categories = new ArrayList<Category>();
                betTypes = new ArrayList<BetType>();

        }

        /**
         * A constructor to create <code>Question</code> objects
         * 
         * @param questionID
         *                the question identifier
         * @param description
         *                the description of the question
         */
        public Question(Long questionID, String description) {
                this.questionID = questionID;
                this.description = description;
        }

        @Id
        public Long getQuestionID() {
                return questionID;
        }

        public void setQuestionID(Long questionID) {
                this.questionID = questionID;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
        public List<BetType> getBetTypes() {
                return betTypes;
        }

        public void setBetTypes(List<BetType> betTypes) {
                this.betTypes = betTypes;
        }

        @OneToMany(mappedBy = "question")
        public List<Category> getCategories() {
                return categories;
        }

        public void setCategories(List<Category> categories) {
                this.categories = categories;
        }

}
