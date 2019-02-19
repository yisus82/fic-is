package ibei.model.productfacade.to;

import java.io.Serializable;
import java.util.Calendar;

public class ProductResultTO implements Serializable {

        private Long productID;

        private String name;

        private Calendar endTime;

        private Double currentPrice;

        public ProductResultTO(Long productID, String name, Calendar endTime,
                        Double currentPrice) {
                this.productID = productID;
                this.name = name;
                this.endTime = endTime;
                this.currentPrice = currentPrice;
        }

        public Long getProductID() {
                return this.productID;
        }

        public String getName() {
                return this.name;
        }

        public Calendar getEndTime() {
                return this.endTime;
        }

        public Double getCurrentPrice() {
                return this.currentPrice;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof ProductResultTO) {
                        ProductResultTO result = (ProductResultTO) object;
                        return (result.getCurrentPrice().equals(currentPrice)
                                        && result.getEndTime().equals(endTime)
                                        && result.getName().equals(name) && result
                                        .getProductID().equals(productID));
                }

                return false;

        }

}
