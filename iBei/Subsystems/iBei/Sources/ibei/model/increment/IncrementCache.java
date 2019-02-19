package ibei.model.increment;

import ibei.model.increment.dao.SQLIncrementDAO;
import ibei.model.increment.dao.SQLIncrementDAOFactory;
import ibei.model.increment.to.IncrementTO;
import ibei.model.util.GlobalNames;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.sql.DataSource;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.GeneralOperations;

/**
 * A Singleton class used to manage IncrementVO objects. As the Increment SQL
 * table is read-only it is better to use a Singleton to cache the object and to
 * access the database only once.
 */
public class IncrementCache {

        private Vector<IncrementTO> increments = null;

        private static IncrementCache instance = null;

        private IncrementCache() throws InternalErrorException {
                Connection connection = null;
                try {
                        DataSource dataSource = DataSourceLocator
                                        .getDataSource(GlobalNames.IBEI_DATA_SOURCE);
                        connection = dataSource.getConnection();
                        SQLIncrementDAO dao = SQLIncrementDAOFactory.getDAO();
                        increments = dao.findAll(connection);
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

        public static IncrementCache getInstance()
                        throws InternalErrorException {

                if (instance == null)
                        instance = new IncrementCache();
                return instance;

        }

        public Double getIncrement(Double value) {

                IncrementTO incrementVO = null;
                for (int i = 0; i < increments.size(); i++) {
                        incrementVO = (IncrementTO) increments.get(i);
                        /*
                         * 'maxValue' == 0 means there is no more increments so
                         * this is the one we have to return
                         */
                        if (incrementVO.getMaxValue().doubleValue() == 0)
                                return incrementVO.getIncrement();
                        if ((incrementVO.getMinValue().doubleValue() <= value
                                        .doubleValue())
                                        && (incrementVO.getMaxValue()
                                                        .doubleValue() > value
                                                        .doubleValue()))
                                return incrementVO.getIncrement();
                }
                /* Just in case */
                return null;

        }

}
