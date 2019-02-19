package ubet.test.testfacade.delegate;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface TestFacadeDelegate {

        public void removeUser(String login) throws InstanceNotFoundException,
                        InternalErrorException;

        public void removeBetType(Long betTypeID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

        public void removeEvent(Long eventID) throws InstanceNotFoundException,
                        InternalErrorException;

        public void removeBet(Long betID) throws InstanceNotFoundException,
                        InternalErrorException;

        public void removeAccount(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}
