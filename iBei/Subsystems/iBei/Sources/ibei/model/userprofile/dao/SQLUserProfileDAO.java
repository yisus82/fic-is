package ibei.model.userprofile.dao;

import ibei.model.userprofile.to.UserProfileTO;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLUserProfileDAO {

        public UserProfileTO create(Connection connection,
                        UserProfileTO userProfileTO) throws InstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, String login, String type)
                        throws InternalErrorException;

        public UserProfileTO find(Connection connection, String login)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public UserProfileTO update(Connection connection, String login,
                        UserProfileTO userProfileTO) throws InstanceException,
                        InternalErrorException, DuplicateInstanceException;

        public void remove(Connection connection, String login)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
