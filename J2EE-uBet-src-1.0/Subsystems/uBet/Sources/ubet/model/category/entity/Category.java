package ubet.model.category.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import ubet.model.event.entity.Event;
import ubet.model.question.entity.Question;

/**
 * An entity representing the information of a category.
 */
@Entity
public class Category {
        /**
         * NOTE: this entity class does not contain a "version" property since
         * its instances are never updated after being persisted.
         */

        /**
         * The category identifier
         */
        private String categoryID;

        /**
         * The name of the category
         */
        private String name;

        /**
         * The parent of the category. It could be itself in the case of the
         * root category
         */
        private Category parent;

        /**
         * Indicates if the category has children ("N") or it's a leaf category
         * ("N")
         */
        private String leaf;

        /**
         * The default question associated to the category
         */
        private Question question;

        /**
         * The children categories of the category
         */
        private List<Category> children;

        /**
         * The events associated with the category
         */
        private List<Event> events;

        /**
         * The questions associated with the category
         */
        private List<Question> questions;

        public Category() {
                children = new ArrayList<Category>();
                events = new ArrayList<Event>();
                questions = new ArrayList<Question>();
        }

        /**
         * A constructor to create <code>Category</code> objects
         * 
         * @param categoryID
         *                the category identifier
         * @param name
         *                the name of the category
         * @param parent
         *                the parent of the category. It could be itself in the
         *                case of the root category
         * @param leaf
         *                "N" if the category has children or "Y" if it's a leaf
         *                category
         * @param question
         *                the default question associated to the category
         */
        public Category(String categoryID, String name, Category parent,
                        String leaf, Question question) {
                this.categoryID = categoryID;
                this.name = name;
                this.parent = parent;
                this.leaf = leaf;
                this.question = question;
        }

        @Id
        public String getCategoryID() {
                return categoryID;
        }

        public void setCategoryID(String categoryID) {
                this.categoryID = categoryID;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @ManyToOne
        @JoinColumn(name = "parent")
        public Category getParent() {
                return parent;
        }

        public void setParent(Category parent) {
                this.parent = parent;
        }

        public String getLeaf() {
                return leaf;
        }

        public void setLeaf(String leaf) {
                this.leaf = leaf;
        }

        @OneToOne
        @JoinColumn(name = "questionID")
        public Question getQuestion() {
                return question;
        }

        public void setQuestion(Question question) {
                this.question = question;
        }

        @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
        public List<Category> getChildren() {
                return children;
        }

        public void setChildren(List<Category> children) {
                this.children = children;
        }

        @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
        public List<Event> getEvents() {
                return events;
        }

        public void setEvents(List<Event> events) {
                this.events = events;
        }

        @ManyToMany
        @JoinTable(name = "CategoryQuestion", joinColumns = @JoinColumn(name = "categoryID"), inverseJoinColumns = @JoinColumn(name = "questionID"))
        public List<Question> getQuestions() {
                return questions;
        }

        public void setQuestions(List<Question> questions) {
                this.questions = questions;
        }

}
