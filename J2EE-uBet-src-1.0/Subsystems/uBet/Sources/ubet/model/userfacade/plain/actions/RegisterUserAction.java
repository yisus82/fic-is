package ubet.model.userfacade.plain.actions;

import java.sql.Connection;
import java.util.Calendar;

import ubet.model.account.to.AccountTO;
import ubet.model.userfacade.util.PasswordEncrypter;
import ubet.model.userprofile.dao.SQLUserProfileDAO;
import ubet.model.userprofile.dao.SQLUserProfileDAOFactory;
import ubet.model.userprofile.to.UserProfileTO;

import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.exceptions.ModelException;
import es.udc.fbellas.j2ee.util.sql.TransactionalPlainAction;

public class RegisterUserAction implements TransactionalPlainAction {

        private UserProfileTO userTO;

        private String password;

        private String creditCardNumber;

        private Calendar expirationDate;

        private Double balance;

        public RegisterUserAction(UserProfileTO userTO,
                        String creditCardNumber, Calendar expirationDate,
                        Double balance) {
                this.userTO = userTO;
                this.password = userTO.getPassword();
                this.userTO.setPassword(PasswordEncrypter.crypt(this.userTO
                                .getPassword()));
                this.creditCardNumber = creditCardNumber;
                this.expirationDate = expirationDate;
                this.balance = balance;
        }

        public Object execute(Connection connection) throws ModelException,
                        InternalErrorException {
                SQLUserProfileDAO userDAO = SQLUserProfileDAOFactory.getDAO();

                userDAO.create(connection, userTO);

                userTO.setPassword(password);

                AccountTO account = new AccountTO(new Long("-1"), userTO
                                .getLogin(), creditCardNumber, expirationDate,
                                balance);

                return new CreateAccountAction(account).execute(connection);

        }

}
