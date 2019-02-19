package ubet.model.category.to;

import java.io.Serializable;

/**
 * A transfer object representing the information of a category.
 */
public class CategoryTO implements Serializable {

        /**
         * The category identifier
         */
        private String categoryID;

        /**
         * The name of the category
         */
        private String name;

        /**
         * The identifier of the parent of the category. It could be
         * <code>categoryID</code> in the case of the root category
         */
        private String parentID;

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
         * A constructor to create <code>CategoryTO</code> objects
         * 
         * @param categoryID
         *                the category identifier
         * @param name
         *                the name of the category
         * @param parentID
         *                the identifier of the parent of the category. It could
         *                be <code>null</code> in the case of the root
         *                category
         * @param leaf
         *                <code>false</code> if the category has children or
         *                <code>true</code> if it's a leaf category
         * @param questionID
         *                the identifier of the default question associated to
         *                the category
         */
        public CategoryTO(String categoryID, String name, String parentID,
                        boolean leaf, Long questionID) {
                this.categoryID = categoryID;
                this.name = name;
                this.parentID = parentID;
                this.leaf = leaf;
                this.questionID = questionID;
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
         * @return the <code>parentID</code>
         */
        public String getParentID() {
                return this.parentID;
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

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof CategoryTO) {
                        CategoryTO category = (CategoryTO) object;
                        return (category.getCategoryID()
                                        .equals(this.categoryID)
                                        && category.getName().equals(this.name)
                                        && category.getParentID().equals(
                                                        this.parentID)
                                        && category.getQuestionID().equals(
                                                        this.questionID) && (category
                                        .isLeaf() == this.leaf));
                }

                return false;
        }

        @Override
        public String toString() {
                return ("\nCategory ID = " + this.categoryID + "\nName = "
                                + this.name + "\nParent ID = " + this.parentID
                                + "\nLeaf = " + this.leaf + "\nQuestion ID = "
                                + questionID + "\n");
        }

}
