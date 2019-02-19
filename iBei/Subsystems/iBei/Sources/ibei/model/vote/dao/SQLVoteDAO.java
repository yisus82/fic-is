package ibei.model.vote.dao;

import ibei.model.vote.to.VoteTO;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface SQLVoteDAO {

        public VoteTO create(Connection connection, VoteTO voteTO)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        public boolean exists(Connection connection, Long voteID)
                        throws InternalErrorException;

        public VoteTO find(Connection connection, Long voteID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public void remove(Connection connection, Long voteID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
