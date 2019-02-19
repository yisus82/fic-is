package ubet.model.searchfacade.ejb.actions;

import java.util.List;

import javax.persistence.EntityManager;

import ubet.model.country.entity.Country;
import ubet.model.country.to.CountryTO;
import ubet.model.searchfacade.ejb.SearchFacadeHelper;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public class GetAllCountriesAction {

        private EntityManager entityManager;

        public GetAllCountriesAction(EntityManager entityManager) {
                this.entityManager = entityManager;
        }

        public List<CountryTO> execute() throws InternalErrorException {
                List<Country> countries = entityManager.createQuery(
                                "SELECT c FROM Country c ORDER BY c.name")
                                .getResultList();
                return SearchFacadeHelper.toCountryTOs(countries);
        }

}
