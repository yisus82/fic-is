package ubet.model.searchfacade.ejb.actions;

import java.util.List;

import javax.persistence.EntityManager;

import ubet.model.category.entity.Category;
import ubet.model.category.to.CategoryTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class GetAllCategoriesAction {

        private EntityManager entityManager;

        public GetAllCategoriesAction(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public List<CategoryTO> execute() throws InternalErrorException {
                List<Category> categories = entityManager.createQuery(
                                "SELECT c FROM Category c "
                                                + "ORDER BY c.categoryID")
                                .getResultList();
                return SearchFacadeHelper.toCategoryTOs(categories);
        }

}
