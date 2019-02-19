package ibei.model.product.to;

import ibei.model.util.DateOperations;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

public class ProductTO implements Serializable {

        private Long productID;

        private String name;

        private String description;

        private Calendar endTime;

        private Double currentPrice;

        private ProductDetailsTO productDetails;

        public ProductTO(Long productID, String name, String description,
                        Calendar endTime, Double currentPrice,
                        ProductDetailsTO productDetails) {
                this.productID = productID;
                this.name = name;
                this.description = description;
                Calendar dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(endTime);
                this.endTime = dateWithoutMilliSeconds;
                this.currentPrice = currentPrice;
                this.productDetails = productDetails;
        }

        public Long getProductID() {
                return this.productID;
        }

        public void setProductID(Long productID) {
                this.productID = productID;
        }

        public String getName() {
                return this.name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getDescription() {
                return this.description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public Calendar getEndTime() {
                return this.endTime;
        }

        public void setEndTime(Calendar endTime) {
                this.endTime = endTime;
        }

        public Double getCurrentPrice() {
                return this.currentPrice;
        }

        public void setCurrentPrice(Double currentPrice) {
                this.currentPrice = currentPrice;
        }

        public ProductDetailsTO getProductDetails() {
                return this.productDetails;
        }

        public void setProductDetails(ProductDetailsTO productDetails) {
                this.productDetails = productDetails;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof ProductTO) {
                        ProductTO product = (ProductTO) object;
                        return (product.getCurrentPrice().equals(currentPrice)
                                        && product.getDescription().equals(
                                                        description)
                                        && product.getEndTime().equals(endTime)
                                        && product.getName().equals(name)
                                        && product.getProductDetails().equals(
                                                        productDetails) && product
                                        .getProductID().equals(productID));
                }

                return false;

        }

        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(endTime.getTime());

                return ("\nCurrent price = " + this.currentPrice
                                + "\nDescription = " + this.description
                                + "\nEndTime = " + dateString + "\nName = "
                                + this.name + "\nProduct Details = "
                                + this.productDetails + "\n");
        }

}
