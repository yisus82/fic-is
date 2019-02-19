package ubet.model.searchfacade.delegate;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A factory to get <code>SearchFacadeDelegate</code> objects.
 * <p>
 * Required configuration parameters:
 * <ul>
 * <li><code>SearchFacadeDelegateFactory/delegateClassName</code>: it must
 * specify the full class name of the class implementing
 * <code>SearchFacadeDelegate</code>.</li>
 * </ul>
 */
public final class SearchFacadeDelegateFactory {

        private final static String DELEGATE_CLASS_NAME_PARAMETER = "SearchFacadeDelegateFactory/delegateClassName";

        private final static Class<SearchFacadeDelegate> delegateClass = getDelegateClass();

        private SearchFacadeDelegateFactory() {
        }

        private static Class<SearchFacadeDelegate> getDelegateClass() {
                Class<SearchFacadeDelegate> theClass = null;

                try {

                        String delegateClassName = ConfigurationParametersManager
                                        .getParameter(DELEGATE_CLASS_NAME_PARAMETER);

                        theClass = (Class<SearchFacadeDelegate>) Class
                                        .forName(delegateClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;
        }

        public static SearchFacadeDelegate getDelegate()
                        throws InternalErrorException {
                try {
                        return delegateClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}
