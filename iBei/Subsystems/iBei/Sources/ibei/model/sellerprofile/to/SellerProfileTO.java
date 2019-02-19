package ibei.model.sellerprofile.to;

import ibei.model.userprofile.to.UserProfileDetailsTO;
import ibei.model.userprofile.to.UserProfileTO;
import ibei.model.util.DateOperations;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

public class SellerProfileTO extends UserProfileTO implements Serializable {

        private String creditCardNumber;

        private Calendar expirationDate;

        public SellerProfileTO(String login, String password,
                        UserProfileDetailsTO userProfileDetails,
                        String creditCardNumber, Calendar expirationDate) {
                super(login, password, userProfileDetails);
                setType(SELLER);
                this.creditCardNumber = creditCardNumber;
                Calendar dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(expirationDate);
                this.expirationDate = dateWithoutMilliSeconds;
        }

        public String getCreditCardNumber() {
                return this.creditCardNumber;
        }

        public void setCreditCardNumber(String creditCardNumber) {
                this.creditCardNumber = creditCardNumber;
        }

        public Calendar getExpirationDate() {
                return this.expirationDate;
        }

        public void setExpirationDate(Calendar expirationDate) {
                this.expirationDate = expirationDate;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof SellerProfileTO) {
                        SellerProfileTO seller = (SellerProfileTO) object;
                        return (super.equals(object)
                                        && seller.getCreditCardNumber().equals(
                                                        creditCardNumber) && seller
                                        .getExpirationDate().equals(
                                                        expirationDate));
                }

                return false;

        }

        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(expirationDate
                                .getTime());

                return (super.toString() + "\nCreditCardNumber = "
                                + this.creditCardNumber
                                + "\nExpiration date = " + dateString + "\n");
        }

}
