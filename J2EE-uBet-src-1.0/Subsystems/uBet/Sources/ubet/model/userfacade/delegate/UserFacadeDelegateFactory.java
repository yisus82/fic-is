package ubet.model.userfacade.delegate;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>UserFacadeDelegate</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>UserFacadeDelegateFactory/delegateClassName</code>: it must
 * specify the full class name of the class implementing
 * <code>UserFacadeDelegate</code>.</li>
 * </ul>
 */
public final class UserFacadeDelegateFactory {

        private final static String DELEGATE_CLASS_NAME_PARAMETER = "UserFacadeDelegateFactory/delegateClassName";

        private final static Class<UserFacadeDelegate> delegateClass = getDelegateClass();

        private UserFacadeDelegateFactory() {
        }

        private static Class<UserFacadeDelegate> getDelegateClass() {
                Class<UserFacadeDelegate> theClass = null;

                try {

                        String delegateClassName = ConfigurationParametersManager
                                        .getParameter(DELEGATE_CLASS_NAME_PARAMETER);

                        theClass = (Class<UserFacadeDelegate>) Class
                                        .forName(delegateClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static UserFacadeDelegate getDelegate()
                        throws InternalErrorException {
                try {
                        return delegateClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
