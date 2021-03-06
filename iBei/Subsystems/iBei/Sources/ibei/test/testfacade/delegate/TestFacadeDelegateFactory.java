package ibei.test.testfacade.delegate;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public final class TestFacadeDelegateFactory {

        private final static String DELEGATE_CLASS_NAME_PARAMETER = "TestFacadeDelegateFactory/delegateClassName";

        private final static Class<TestFacadeDelegate> delegateClass = getDelegateClass();

        private TestFacadeDelegateFactory() {
        }

        private static Class<TestFacadeDelegate> getDelegateClass() {

                Class<TestFacadeDelegate> theClass = null;

                try {

                        String delegateClassName = ConfigurationParametersManager
                                        .getParameter(DELEGATE_CLASS_NAME_PARAMETER);

                        theClass = (Class<TestFacadeDelegate>) Class
                                        .forName(delegateClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;

        }

        public static TestFacadeDelegate getDelegate()
                        throws InternalErrorException {

                try {
                        return delegateClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}
