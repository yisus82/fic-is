package ubet.model.adminfacade.delegate;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>AdminFacadeDelegate</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>AdminFacadeDelegateFactory/delegateClassName</code>: it must
 * specify the full class name of the class implementing
 * <code>AdminFacadeDelegate</code>.</li>
 * </ul>
 */
public final class AdminFacadeDelegateFactory {

        private final static String DELEGATE_CLASS_NAME_PARAMETER = "AdminFacadeDelegateFactory/delegateClassName";

        private final static Class<AdminFacadeDelegate> delegateClass = getDelegateClass();

        private AdminFacadeDelegateFactory() {
        }

        private static Class<AdminFacadeDelegate> getDelegateClass() {
                Class<AdminFacadeDelegate> theClass = null;

                try {

                        String delegateClassName = ConfigurationParametersManager
                                        .getParameter(DELEGATE_CLASS_NAME_PARAMETER);

                        theClass = (Class<AdminFacadeDelegate>) Class
                                        .forName(delegateClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static AdminFacadeDelegate getDelegate()
                        throws InternalErrorException {
                try {
                        return delegateClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
