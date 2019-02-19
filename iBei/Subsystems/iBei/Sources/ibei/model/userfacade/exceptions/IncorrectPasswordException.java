package ibei.model.userfacade.exceptions;

import es.udc.fbellas.j2ee.util.exceptions.ModelException;

/**
 * An exception thrown when a bad password is found when asked
 * 
 */
public class IncorrectPasswordException extends ModelException {

        /**
         * The user login (identifier)
         */
        private String login;

        /**
         * Constructs a new exception with the message "Incorrect password
         * exception => login = <code>login</code>"
         * 
         * @param login
         *                the user login (identifier)
         */
        public IncorrectPasswordException(String login) {
                super("Incorrect password exception => login = " + login);
                this.login = login;
        }

        /**
         * @return the <code>login</code>
         */
        public String getLogin() {
                return this.login;
        }

}
