package ubet.model.userfacade.util;

public class PasswordEncrypter {

        private PasswordEncrypter() {
        }

        public final static String crypt(String clearPassword) {

                /*
                 * "jcrypt" only considers the first 8 characters of the clear
                 * text, and generates an encrypted text that is always 11
                 * characters in length. The first two characters of the
                 * encrypted text are always the first two characters of the
                 * salt (the first parameter of "jcrypt"). If the length of the
                 * salt is lower than 2, the salt is completed with a default
                 * character ('A'). This salt is used to perturb the algorithm
                 * in one of 4096 different ways. Check "man crypt" on a Unix
                 * machine and the implementation of "jcrypt" for further
                 * details.
                 * 
                 * The implementation of this method uses the clear password as
                 * the salt. The first two characters of the string returned by
                 * "jcrypt" are removed (they are the first two characters of
                 * the clear password).
                 */
                String encryptedPasswordWithSalt = jcrypt.crypt(clearPassword,
                                clearPassword);
                String encryptedPassword = encryptedPasswordWithSalt
                                .substring(2);

                return encryptedPassword;

        }

}