package ibei.model.product.to;

import ibei.model.util.DateOperations;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

public class ProductDetailsTO implements Serializable {

        private Calendar startTime;

        private Double startingPrice;

        private String shippingInfo;

        private String categoryID;

        private String userID;

        private String winnerID;

        private Double winnerBid;

        public ProductDetailsTO(Calendar startTime, Double startingPrice,
                        String shippingInfo, String categoryID, String userID,
                        String winnerID, Double winnerBid) {
                Calendar dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(startTime);
                this.startTime = dateWithoutMilliSeconds;
                this.startingPrice = startingPrice;
                this.shippingInfo = shippingInfo;
                this.categoryID = categoryID;
                this.userID = userID;
                this.winnerID = winnerID;
                this.winnerBid = winnerBid;
        }

        public Calendar getStartTime() {
                return this.startTime;
        }

        public Double getStartingPrice() {
                return this.startingPrice;
        }

        public String getShippingInfo() {
                return this.shippingInfo;
        }

        public void setShippingInfo(String shippingInfo) {
                this.shippingInfo = shippingInfo;
        }

        public String getCategoryID() {
                return this.categoryID;
        }

        public void setCategoryID(String categoryID) {
                this.categoryID = categoryID;
        }

        public String getUserID() {
                return this.userID;
        }

        public String getWinnerID() {
                return this.winnerID;
        }

        public void setWinnerID(String winnerID) {
                this.winnerID = winnerID;
        }

        public Double getWinnerBid() {
                return this.winnerBid;
        }

        public void setWinnerBid(Double winnerBid) {
                this.winnerBid = winnerBid;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof ProductDetailsTO) {
                        ProductDetailsTO details = (ProductDetailsTO) object;
                        return (details.getCategoryID().equals(categoryID)
                                        && details.getShippingInfo().equals(
                                                        shippingInfo)
                                        && details.getStartingPrice().equals(
                                                        startingPrice)
                                        && details.getStartTime().equals(
                                                        startTime)
                                        && details.getUserID().equals(userID)
                                        && ((winnerBid == details
                                                        .getWinnerBid()) || (details
                                                        .getWinnerBid()
                                                        .equals(winnerBid))) && ((winnerID == details
                                        .getWinnerID()) || (details
                                        .getWinnerID().equals(winnerID))));
                }

                return false;

        }

        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(startTime.getTime());

                return ("\nCategoryID = " + this.categoryID
                                + "\nShipping Info = " + this.shippingInfo
                                + "\nStarting price = " + this.startingPrice
                                + "\nStart time = " + dateString
                                + "\nUserID = " + this.userID
                                + "\nWinner bid = " + this.winnerBid
                                + "\nWinnerID = " + this.winnerID + "\n");
        }
}
