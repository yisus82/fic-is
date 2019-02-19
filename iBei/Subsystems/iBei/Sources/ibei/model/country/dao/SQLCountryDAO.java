package ibei.model.country.dao;

import ibei.model.country.to.CountryTO;

import java.sql.Connection;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLCountryDAO {

        public Vector<CountryTO> findAll(Connection connection)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
