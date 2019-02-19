package ubet.model.userprofile.dao;

import java.sql.Connection;

import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLUserProfileDAO {

        /**
         * Creates an user in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param userTO
         *                a transfer object with all of the user data
         * @throws DuplicateInstanceException
         *                 if there is another user with the same login
         *                 (identifier) in the database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void create(Connection connection, UserProfileTO userTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Checks if an user exists in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param login
         *                the user login (identifier)
         * @return <code>true</code> if the user exists<br>
         *         <code>false</code> if the user doesn't exist
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public boolean exists(Connection connection, String login)
                        throws InternalErrorException;

        /**
         * Finds an user in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param login
         *                the user login (identifier)
         * @return a transfer object with the user data
         * @throws InstanceNotFoundException
         *                 if there is no user with the <code>login</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public UserProfileTO find(Connection connection, String login)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        /**
         * Updates an user in the database.
         * 
         * @param connection
         *                the connection to the database
         * @param login
         *                the old login (to prevent incorrect login update)
         * @param userTO
         *                a transfer object with the new user data
         * @throws DuplicateInstanceException
         *                 if an incorrect login update occurs
         * @throws InstanceNotFoundException
         *                 if there is no user with the login included in
         *                 <code>userTO</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void update(Connection connection, String login,
                        UserProfileTO userTO)
                        throws DuplicateInstanceException,
                        InstanceNotFoundException, InternalErrorException;

        /**
         * Removes an user from the database.
         * 
         * @param connection
         *                the connection to the database
         * @param login
         *                the user login (identifier)
         * @throws InstanceNotFoundException
         *                 if there is no user with the <code>login</code>
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void remove(Connection connection, String login)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
