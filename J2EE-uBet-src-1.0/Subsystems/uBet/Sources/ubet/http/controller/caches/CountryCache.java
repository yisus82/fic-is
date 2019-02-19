package ubet.http.controller.caches;

import java.util.Iterator;
import java.util.List;

import ubet.model.country.to.CountryTO;
import ubet.model.searchfacade.delegate.SearchFacadeDelegate;
import ubet.model.searchfacade.delegate.SearchFacadeDelegateFactory;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * This class is a cache of the 'Country' table of the database. It has been
 * designed as an utility class.
 */
public class CountryCache {
        private static List<CountryTO> countries;

        /*
         * The constructor has been modified with private scope to avoid the
         * instantiation of this utility class.
         */
        private CountryCache() {
        }

        public static CountryTO getCountry(String countryID)
                        throws InternalErrorException {
                if (countries == null) {
                        createCountries();
                }

                for (Iterator i = countries.iterator(); i.hasNext();) {
                        CountryTO c = (CountryTO) i.next();
                        if (c.getCountryID().compareTo(countryID) == 0) {
                                return c;
                        }
                }

                return null;
        }

        public static List<CountryTO> getAllCountries()
                        throws InternalErrorException {
                if (countries == null) {
                        createCountries();
                }
                return countries;
        }

        private static void createCountries() throws InternalErrorException {
                SearchFacadeDelegate searchFacadeDelegate = SearchFacadeDelegateFactory
                                .getDelegate();

                countries = searchFacadeDelegate.getAllCountries();
        }
}
