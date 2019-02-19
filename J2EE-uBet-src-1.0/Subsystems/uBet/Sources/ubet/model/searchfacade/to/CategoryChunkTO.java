package ubet.model.searchfacade.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A custom transfer object representing the information of a category.
 */
public class CategoryChunkTO implements Serializable {

        /**
         * The category identifier
         */
        private String categoryID;

        /**
         * The name of the category
         */
        private String name;

        /**
         * A custom transfer object representing the information of the parent
         * of this category
         */
        private CategoryChunkTO parent;

        /**
         * Indicates if the category has children (<code>false</code>) or
         * it's a leaf category (<code>true</code>)
         */
        private boolean leaf;

        /**
         * The identifier of the default question associated to the category
         */
        private Long questionID;

        /**
         * A collection with the children of this category
         */
        private List<CategoryChunkTO> children;

        /**
         * A constructor to create <code>CategoryTO</code> objects
         * 
         * @param categoryID
         *                the category identifier
         * @param name
         *                the name of the category
         * @param parent
         *                a custom transfer object representing the information
         *                of the parent of this category
         * @param leaf
         *                <code>false</code> if the category has children or
         *                <code>true</code> if it's a leaf category
         * @param questionID
         *                the identifier of the default question associated to
         *                the category
         */
        public CategoryChunkTO(String categoryID, String name,
                        CategoryChunkTO parent, boolean leaf, Long questionID) {
                this.categoryID = categoryID;
                this.name = name;
                this.parent = parent;
                this.leaf = leaf;
                this.questionID = questionID;
                children = new ArrayList<CategoryChunkTO>();
        }

        /**
         * A constructor to create <code>CategoryTO</code> objects
         * 
         * @param categoryID
         *                the category identifier
         * @param name
         *                the name of the category
         * @param leaf
         *                <code>false</code> if the category has children or
         *                <code>true</code> if it's a leaf category
         * @param questionID
         *                the identifier of the default question associated to
         *                the category
         */
        public CategoryChunkTO(String categoryID, String name, boolean leaf,
                        Long questionID) {
                this.categoryID = categoryID;
                this.name = name;
                this.parent = null;
                this.leaf = leaf;
                this.questionID = questionID;
                children = new ArrayList<CategoryChunkTO>();
        }

        /**
         * @return the <code>categoryID</code>
         */
        public String getCategoryID() {
                return this.categoryID;
        }

        /**
         * @return the <code>name</code>
         */
        public String getName() {
                return this.name;
        }

        /**
         * @return the <code>parent</code>
         */
        public CategoryChunkTO getParent() {
                return this.parent;
        }

        /**
         * @param parent
         *                the parent to set
         */
        public void setParent(CategoryChunkTO parent) {
                this.parent = parent;
        }

        /**
         * @return <code>false</code> if the category has children<br>
         *         <code>true</code> if it's a leaf category
         */
        public boolean isLeaf() {
                return this.leaf;
        }

        /**
         * @return the <code>questionID</code>
         */
        public Long getQuestionID() {
                return this.questionID;
        }

        /**
         * @return the <code>children</code>
         */
        public List<CategoryChunkTO> getChildren() {
                return children;
        }

        /**
         * 
         * @param child
         *                the child to add
         */
        public void addChild(CategoryChunkTO child) {
                children.add(child);
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof CategoryChunkTO) {
                        CategoryChunkTO category = (CategoryChunkTO) object;
                        return (category.getCategoryID()
                                        .equals(this.categoryID)
                                        && category.getName().equals(this.name)
                                        // We don't invoke
                                        // "category.getParent.equals(this.parent)"
                                        // method because we would
                                        // enter in an infinite recursive loop.
                                        && category.getQuestionID().equals(
                                                        this.questionID) && (category
                                        .isLeaf() == this.leaf));
                }

                return false;
        }

        @Override
        public String toString() {
                String string = "\nCategory ID = " + this.categoryID
                                + "\nName = " + this.name;
                if (parent != null)
                        string += "\nParent ID = "
                                        + this.parent.getCategoryID();
                string += "\nLeaf = " + this.leaf + "\nQuestion ID = "
                                + questionID;
                if (children != null)
                        string += "\nChildren = " + children;
                string += "\n";
                return string;
        }

}
