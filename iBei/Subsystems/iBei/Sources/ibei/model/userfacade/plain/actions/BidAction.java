package ibei.model.userfacade.plain.actions;

import ibei.model.bid.dao.SQLBidDAO;
import ibei.model.bid.dao.SQLBidDAOFactory;
import ibei.model.bid.to.BidTO;
import ibei.model.increment.IncrementCache;
import ibei.model.product.dao.SQLProductDAO;
import ibei.model.product.dao.SQLProductDAOFactory;
import ibei.model.product.to.ProductTO;
import ibei.model.userfacade.exceptions.BidOutOfTimeException;

import java.sql.Connection;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class BidAction implements TransactionalPlainAction {

        BidTO bidTO;

        public BidAction(BidTO bidTO) {
                this.bidTO = bidTO;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {

                SQLProductDAO productDAO = SQLProductDAOFactory.getDAO();
                ProductTO productTO = productDAO.find(connection, bidTO
                                .getProductID());
                
                if (bidTO.getDate().after(productTO.getEndTime()))
                        throw new BidOutOfTimeException(productTO.getEndTime(), bidTO.getDate());

                SQLBidDAO bidDAO = SQLBidDAOFactory.getDAO();
                bidTO = bidDAO.create(connection, bidTO);

                String winnerID = productTO.getProductDetails().getWinnerID();
                if (winnerID == null) {
                        productTO.getProductDetails().setWinnerID(
                                        bidTO.getUserID());
                        productTO.getProductDetails().setWinnerBid(
                                        bidTO.getCurrentBid());
                        productTO.setCurrentPrice(bidTO.getCurrentBid());
                        productDAO.update(connection, productTO);
                } else {
                        BidTO winnerBid = bidDAO.findMax(connection, productTO
                                        .getProductID());
                        if (bidTO.getMaxBid() > winnerBid
                                        .getMaxBid()) {
                                winnerBid.setCurrentBid(winnerBid.getMaxBid());
                                bidDAO.update(connection, winnerBid);
                                productTO.getProductDetails().setWinnerID(
                                                winnerID);
                                productTO.getProductDetails().setWinnerBid(
                                                winnerBid.getCurrentBid());
                                productTO.setCurrentPrice(winnerBid
                                                .getCurrentBid());
                                IncrementCache incrementCache = IncrementCache
                                                .getInstance();
                                Double increment = incrementCache
                                                .getIncrement(winnerBid
                                                                .getCurrentBid());
                                Double newBid = winnerBid.getCurrentBid() + increment;
                                if (newBid > bidTO.getMaxBid())
                                        newBid = bidTO.getMaxBid();
                                bidTO.setCurrentBid(newBid);
                                bidDAO.update(connection, bidTO);
                                productTO.getProductDetails().setWinnerID(
                                                bidTO.getUserID());
                                productTO.getProductDetails().setWinnerBid(newBid);
                                productTO.setCurrentPrice(newBid);
                                productDAO.update(connection, productTO);
                        } else {
                                winnerBid.setCurrentBid(bidTO.getMaxBid());
                                bidDAO.update(connection, winnerBid);
                                productTO.getProductDetails().setWinnerID(
                                                winnerID);
                                productTO.getProductDetails().setWinnerBid(
                                                winnerBid.getCurrentBid());
                                productTO.setCurrentPrice(winnerBid
                                                .getCurrentBid());
                                bidTO.setCurrentBid(bidTO.getMaxBid());
                                bidDAO.update(connection, bidTO);
                                productDAO.update(connection, productTO);
                        }
                }

                return bidTO;

        }

}
