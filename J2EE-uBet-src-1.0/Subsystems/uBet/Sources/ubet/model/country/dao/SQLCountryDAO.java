package ubet.model.country.dao;

import java.sql.Connection;
import java.util.List;

import ubet.model.country.to.CountryTO;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLCountryDAO {

        /**
         * Checks if a country exists in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param countryID
         *                the country identifier
         * @return <code>true</code> if the country exists<br>
         *         <code>false</code> if the country doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, String countryID)
                        throws InternalErrorException;

        /**
         * Finds a country in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param countryID
         *                the category identifier
         * @return a transfer object with the country data
         * @throws InstanceNotFoundException
         *                 if there is no country with the
         *                 <code>countryID</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public CountryTO find(Connection connection, String countryID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Finds all the countries in the database.
         * 
         * @param connection
         *                the connection to the database
         * @return a collection of transfer objects with the countries data
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public List<CountryTO> getAll(Connection connection)
                        throws InternalErrorException;

}
