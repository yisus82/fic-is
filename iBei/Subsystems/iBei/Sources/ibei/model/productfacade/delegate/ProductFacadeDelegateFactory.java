package ibei.model.productfacade.delegate;

import es.udc.fbellas.j2ee.util.configuration.ConfigurationParametersManager;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public final class ProductFacadeDelegateFactory {

        private final static String DELEGATE_CLASS_NAME_PARAMETER = "ProductFacadeDelegateFactory/delegateClassName";

        private final static Class<ProductFacadeDelegate> delegateClass = getDelegateClass();

        private ProductFacadeDelegateFactory() {
        }

        private static Class<ProductFacadeDelegate> getDelegateClass() {

                Class<ProductFacadeDelegate> theClass = null;

                try {

                        String delegateClassName = ConfigurationParametersManager
                                        .getParameter(DELEGATE_CLASS_NAME_PARAMETER);

                        theClass = (Class<ProductFacadeDelegate>) Class
                                        .forName(delegateClassName);

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return theClass;

        }

        public static ProductFacadeDelegate getDelegate()
                        throws InternalErrorException {

                try {
                        return delegateClass.newInstance();
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}
