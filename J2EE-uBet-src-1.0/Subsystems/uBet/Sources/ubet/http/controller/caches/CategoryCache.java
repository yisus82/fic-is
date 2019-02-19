package ubet.http.controller.caches;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ubet.model.category.to.CategoryTO;
import ubet.model.question.to.QuestionTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import ubet.model.searchfacade.to.CategoryChunkTO;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * This class is a cache of the 'Category' table of the database. It has been
 * designed as an utility class.
 */
public class CategoryCache {
        private static List categories = null;

        private static CategoryChunkTO tree = null;

        private static Map categoryQuestionMap;

        private static CategoryTO rootCategory;

        private CategoryCache() {
        }

        public static CategoryChunkTO getRootCategory()
                        throws InternalErrorException {

                if (categories == null) {
                        getCategoriesFromDataBase();
                }

                rootCategory = null;
                Iterator iterator = categories.iterator();
                boolean cont;
                do {
                        cont = false;
                        if (iterator.hasNext()) {
                                CategoryTO categoryTO = (CategoryTO) iterator
                                                .next();
                                // The father of the root category must be
                                // herself
                                if (categoryTO.getParentID().equals(
                                                categoryTO.getCategoryID()))
                                        rootCategory = categoryTO;
                                cont = ((rootCategory == null) && iterator
                                                .hasNext());
                        }
                } while (cont);
                if (rootCategory == null) {
                        throw new InternalErrorException(new Exception(
                                        "A root category was not found!"));
                }
                tree = new CategoryChunkTO(rootCategory.getCategoryID(),
                                rootCategory.getName(), rootCategory.isLeaf(),
                                rootCategory.getQuestionID());
                tree.setParent(tree);
                buildTree(tree);

                return tree;
        }

        private static void getCategoriesFromDataBase()
                        throws InternalErrorException {

                SearchFacadeDelegate searchFacade = SearchFacadeDelegateFactory
                                .getDelegate();

                categories = searchFacade.getAllCategories();
                categoryQuestionMap = searchFacade.getAllCategoryQuestions();
        }

        private static void buildTree(CategoryChunkTO rootCategory) {
                setChildren(rootCategory);
                Iterator iterator = rootCategory.getChildren().iterator();
                while (iterator.hasNext()) {
                        CategoryChunkTO currentChild = (CategoryChunkTO) iterator
                                        .next();
                        buildTree(currentChild);
                }
        }

        private static void setChildren(CategoryChunkTO parent) {
                Iterator iterator = categories.iterator();

                while (iterator.hasNext()) {
                        CategoryTO category = (CategoryTO) iterator.next();
                        if ((category.getParentID().equals(parent
                                        .getCategoryID()))
                                        && !category.equals(rootCategory)) {
                                CategoryChunkTO chunkCategory = new CategoryChunkTO(
                                                category.getCategoryID(),
                                                category.getName(), parent,
                                                category.isLeaf(),
                                                category.getQuestionID());
                                parent.addChild(chunkCategory);
                        }
                }
        }

        public static CategoryChunkTO getCategoryByIdentifier(String parentID)
                        throws InternalErrorException {
                if (tree == null) {
                        try {
                                getRootCategory();
                        } catch (InternalErrorException e) {
                                throw e;
                        }
                }
                if (parentID.equals(tree.getCategoryID()))
                        return tree;
                return getCategory(parentID, tree);
        }

        private static CategoryChunkTO getCategory(String parentID,
                        CategoryChunkTO rootCategory) {
                if (rootCategory.getCategoryID().equals(parentID))
                        return rootCategory;
                Iterator iterator = rootCategory.getChildren().iterator();
                while (iterator.hasNext()) {
                        CategoryChunkTO customCategory = (CategoryChunkTO) iterator
                                        .next();
                        CategoryChunkTO c = getCategory(parentID,
                                        customCategory);
                        if (c != null)
                                return c;
                }
                return null;
        }

        public static boolean isLeaf(String categoryID)
                        throws InternalErrorException {
                try {
                        return getCategoryByIdentifier(categoryID).isLeaf();
                } catch (InternalErrorException e) {
                        throw e;
                }
        }

        public static void treeToString(CategoryChunkTO category) {
                Iterator iterator = category.getChildren().iterator();
                while (iterator.hasNext()) {
                        CategoryChunkTO current = (CategoryChunkTO) iterator
                                        .next();
                        treeToString(current);
                }
        }

        public static QuestionTO getRemarkedQuestion(String categoryID)
                        throws InternalErrorException {
                try {
                        CategoryChunkTO category = getCategoryByIdentifier(categoryID);
                        Long questionID = category.getQuestionID();
                        if (questionID == null) {
                                return getRemarkedQuestion(category.getParent()
                                                .getCategoryID());
                        }
                        return QuestionCache.getQuestion(questionID);
                } catch (InternalErrorException e) {
                        throw e;
                }
        }

        public static List getQuestionsByCategoryIdentifier(
                        String categoryIdentifier)
                        throws InternalErrorException {
                try {
                        CategoryChunkTO category = getCategoryByIdentifier(categoryIdentifier);
                        if ((List) categoryQuestionMap.get(category
                                        .getCategoryID()) == null)
                                return getQuestionsByCategoryIdentifier(category
                                                .getParent().getCategoryID());
                        return (List) categoryQuestionMap
                                        .get(categoryIdentifier);
                } catch (InternalErrorException e) {
                        throw e;
                }
        }

}