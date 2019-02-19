package ubet.test.testfacade.ejb;

import javax.naming.NamingException;

import ubet.test.testfacade.delegate.TestFacadeDelegate;
import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.jndi.ServiceLocator;

public class EJBTestFacadeDelegate implements TestFacadeDelegate {

        private final static String TEST_FACADE_JNDI_NAME_PARAMETER = "EJBTestFacadeDelegate/testFacadeJNDIName";

        private static String testFacadeJNDIName;

        private TestFacade testFacade;

        static {

                try {

                        /* Initialize "userFacadeJNDIName". */
                        testFacadeJNDIName = ConfigurationParametersManager
                                        .getParameter(TEST_FACADE_JNDI_NAME_PARAMETER);

                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        public EJBTestFacadeDelegate() throws InternalErrorException {
                try {

                        testFacade = ServiceLocator.findService(
                                        TestFacade.class, testFacadeJNDIName);

                } catch (NamingException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void removeUser(String login) throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        testFacade.removeUser(login);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void removeBetType(Long betTypeID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        testFacade.removeBetType(betTypeID);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void removeEvent(Long eventID) throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        testFacade.removeEvent(eventID);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void removeBet(Long betID) throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        testFacade.removeBet(betID);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void removeAccount(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        testFacade.removeAccount(accountID);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

}
