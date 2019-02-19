package ibei.model.country;

import ibei.model.country.dao.SQLCountryDAO;
import ibei.model.country.dao.SQLCountryDAOFactory;
import ibei.model.country.to.CountryTO;
import ibei.model.util.GlobalNames;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.sql.DataSource;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;
import es.udc.fbellas.j2ee.util.struts.view.LabelValue;

/**
 * This class is a cache of the 'Country' table of the database.
 * It has been designed as an utility class.
 */
public class CountryCache {

        private Vector<LabelValue> list = null;

        private HashMap<String, CountryTO> map = null;

        private static CountryCache instance = null;

        private CountryCache() throws InternalErrorException {
                Connection connection = null;
                try {
                        DataSource dataSource = DataSourceLocator
                                        .getDataSource(GlobalNames.IBEI_DATA_SOURCE);
                        connection = dataSource.getConnection();
                        SQLCountryDAO dao = SQLCountryDAOFactory.getDAO();
                        Vector<CountryTO> countries = dao.findAll(connection);
                        list = new Vector();
                        map = new HashMap();
                        for (int i = 0; i < countries.size(); i++) {
                                CountryTO countryVO = (CountryTO) countries
                                                .get(i);
                                list.addElement(new LabelValue(countryVO
                                                .getName(), countryVO
                                                .getCountryID()));
                                map.put(countryVO.getCountryID(), countryVO);
                        }
                } catch (InstanceNotFoundException e) {
                        throw new InternalErrorException(e);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (SQLException e) {
                        throw new InternalErrorException(e);
                } finally {
                        GeneralOperations.closeConnection(connection);
                }
        }

        public static CountryCache getInstance() throws InternalErrorException {

                if (instance == null)
                        instance = new CountryCache();
                return instance;

        }

        public Vector<LabelValue> getCountries() {
                return this.list;
        }

        public CountryTO getCountry(String countryID) {
                return map.get(countryID);
        }

        public boolean exists(String countryID) {
                return map.containsKey(countryID);
        }

}
