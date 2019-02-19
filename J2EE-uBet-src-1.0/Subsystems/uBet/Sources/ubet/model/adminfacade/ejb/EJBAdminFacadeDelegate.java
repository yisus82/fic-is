package ubet.model.adminfacade.ejb;

import java.util.List;

import javax.naming.NamingException;

import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.jndi.ServiceLocator;

public class EJBAdminFacadeDelegate implements AdminFacadeDelegate {

        private final static String ADMIN_FACADE_JNDI_NAME_PARAMETER = "EJBAdminFacadeDelegate/adminFacadeJNDIName";

        private static String adminFacadeJNDIName;

        private AdminFacade adminFacade;

        static {

                try {

                        /* Initialize "userFacadeJNDIName". */
                        adminFacadeJNDIName = ConfigurationParametersManager
                                        .getParameter(ADMIN_FACADE_JNDI_NAME_PARAMETER);

                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        public EJBAdminFacadeDelegate() throws InternalErrorException {
                try {

                        adminFacade = ServiceLocator.findService(
                                        AdminFacade.class, adminFacadeJNDIName);

                } catch (NamingException e) {
                        throw new InternalErrorException(e);
                }
        }

        public EventTO insertEvent(EventTO eventTO, BetTypeTO betTypeTO,
                        List<BetOptionTO> options)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                try {
                        return adminFacade.insertEvent(eventTO, betTypeTO,
                                        options);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public BetTypeTO insertBetType(BetTypeTO betTypeTO,
                        List<BetOptionTO> options)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                try {
                        return adminFacade.insertBetType(betTypeTO, options);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void publishResults(List<Long> winnerOptions)
                        throws InternalErrorException {
                try {
                        adminFacade.publishResults(winnerOptions);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

        public void setHighlighted(Long eventID, boolean highlighted)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {
                        adminFacade.setHighlighted(eventID, highlighted);
                } catch (RuntimeException e) {
                        throw new InternalErrorException(e);
                }
        }

}
