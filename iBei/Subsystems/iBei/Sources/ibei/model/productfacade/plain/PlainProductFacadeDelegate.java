package ibei.model.productfacade.plain;

import ibei.model.bid.to.BidTO;
import ibei.model.product.to.ProductTO;
import ibei.model.productfacade.delegate.ProductFacadeDelegate;
import ibei.model.productfacade.plain.actions.FindBidsByProductAction;
import ibei.model.productfacade.plain.actions.FindProductAction;
import ibei.model.productfacade.plain.actions.FindProductsByCategoryAction;
import ibei.model.productfacade.plain.actions.FindProductsByKeywordsAction;
import ibei.model.productfacade.to.ProductResultTO;
import ibei.model.util.GlobalNames;

import java.util.Vector;

import javax.sql.DataSource;

import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.PlainActionProcessor;

public class PlainProductFacadeDelegate implements ProductFacadeDelegate {

        private DataSource getDataSource() throws InternalErrorException {
                return DataSourceLocator
                                .getDataSource(GlobalNames.IBEI_DATA_SOURCE);
        }

        public Vector<BidTO> findBidsByProduct(Long productID, int startIndex,
                        int count) throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        FindBidsByProductAction action = new FindBidsByProductAction(
                                        productID, startIndex, count);

                        return (Vector<BidTO>) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public Vector<ProductResultTO> findProductsByCategory(
                        String categoryID, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        FindProductsByCategoryAction action = new FindProductsByCategoryAction(
                                        categoryID, startIndex, count);

                        return (Vector<ProductResultTO>) PlainActionProcessor
                                        .process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public Vector<ProductResultTO> findProductsByKeywords(
                        Vector<String> keywords, int startIndex, int count)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        FindProductsByKeywordsAction action = new FindProductsByKeywordsAction(
                                        keywords, startIndex, count);

                        return (Vector<ProductResultTO>) PlainActionProcessor
                                        .process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

        public ProductTO findProduct(Long productID)
                        throws InstanceNotFoundException,
                        InternalErrorException {

                try {

                        FindProductAction action = new FindProductAction(
                                        productID);

                        return (ProductTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }

        }

}
