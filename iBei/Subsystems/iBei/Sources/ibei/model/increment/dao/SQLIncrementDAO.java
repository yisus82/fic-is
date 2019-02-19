package ibei.model.increment.dao;

import ibei.model.increment.to.IncrementTO;

import java.sql.Connection;
import java.util.Vector;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLIncrementDAO {

        public Vector<IncrementTO> findAll(Connection connection)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
