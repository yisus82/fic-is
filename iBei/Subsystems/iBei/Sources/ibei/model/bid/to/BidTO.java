package ibei.model.bid.to;

import ibei.model.util.DateOperations;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

public class BidTO implements Serializable {

        private Long bidID;

        private String userID;

        private Long productID;

        private Double currentBid;

        private Double maxBid;

        private Calendar date;

        public BidTO(Long bidID, String userID, Long productID,
                        Double currentBid, Double maxBid, Calendar date) {
                this.bidID = bidID;
                this.userID = userID;
                this.productID = productID;
                this.currentBid = currentBid;
                this.maxBid = maxBid;
                Calendar dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(date);
                this.date = dateWithoutMilliSeconds;
        }

        public Long getBidID() {
                return this.bidID;
        }

        public void setBidID(Long bidID) {
                this.bidID = bidID;
        }

        public String getUserID() {
                return this.userID;
        }

        public Long getProductID() {
                return this.productID;
        }

        public Double getCurrentBid() {
                return this.currentBid;
        }

        public void setCurrentBid(Double currentBid) {
                this.currentBid = currentBid;
        }

        public Double getMaxBid() {
                return this.maxBid;
        }

        public Calendar getDate() {
                return this.date;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof BidTO) {
                        BidTO bid = (BidTO) object;
                        return (bid.getBidID().equals(bidID)
                                        && bid.getCurrentBid().equals(
                                                        currentBid)
                                        && bid.getDate().equals(date)
                                        && bid.getMaxBid().equals(maxBid)
                                        && bid.getProductID().equals(productID) && bid
                                        .getUserID().equals(userID));
                }

                return false;

        }

        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(date.getTime());

                return ("\nBidID = " + this.bidID + "\nCurrent bid = "
                                + this.currentBid + "\nDate = " + dateString
                                + "\nMax bid = " + this.maxBid
                                + "\nProductID = " + this.productID
                                + "\nUserID = " + this.userID + "\n");
        }

}
