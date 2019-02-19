package ibei.model.category.to;

import java.io.Serializable;
import java.util.Vector;

public class CategoryTO implements Serializable {

        private String categoryID;

        private String name;

        private Vector<String> subcategories;

        private String parentID;

        public CategoryTO(String categoryID, String name, Vector<String> subcategories,
                        String parentID) {
                this.categoryID = categoryID;
                this.name = name;
                this.subcategories = subcategories;
                this.parentID = parentID;
        }

        public String getCategoryID() {
                return this.categoryID;
        }

        public String getName() {
                return this.name;
        }

        public Vector<String> getSubcategories() {
                return this.subcategories;
        }

        public void setSubcategories(Vector<String> subcategories) {
                this.subcategories = subcategories;
        }

        public String getParentID() {
                return this.parentID;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof CategoryTO) {
                        CategoryTO category = (CategoryTO) object;
                        return (category.getCategoryID().equals(categoryID)
                                        && category.getName().equals(name)
                                        && category.getParentID().equals(
                                                        parentID) && category
                                        .getSubcategories().equals(
                                                        subcategories));
                }

                return false;

        }

        public String toString() {
                return ("\nCategoryID = " + this.categoryID + "\nName = "
                                + this.name + "\nParentID = " + this.parentID
                                + "\nSubcategories = " + this.subcategories + "\n");
        }
}
