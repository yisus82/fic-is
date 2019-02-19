package ubet.http.controller.session;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;

import ubet.model.account.to.AccountTO;
import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.adminfacade.delegate.AdminFacadeDelegateFactory;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import ubet.model.userfacade.delegate.UserFacadeDelegate;
import ubet.model.userfacade.delegate.UserFacadeDelegateFactory;
import ubet.model.userfacade.exceptions.IncorrectPasswordException;
import ubet.model.userfacade.to.LoginResultTO;
import ubet.model.userprofile.to.UserProfileTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

/**
 * A facade utility class to transparently manage session objects and cookies.
 * The following objects are mantained in the session:
 * 
 * <ul>
 * 
 * <li>The user's login, under the key <code>LOGIN_SESSION_ATTRIBUTE</code>.
 * This attribute is only present for authenticated users. </li>
 * 
 * 
 * <li>An instance of <code>Locale</code>. If the user has been
 * authenticated, his/her value is consistent with his/her profile information.
 * This attribute must only be accessed by using
 * <code>org.apache.struts.action.Action.getLocale</code> or
 * <code>es.udc.fbellas.j2ee.util.struts.action.DefaultActionForm.getLocale</code>.
 * </li>
 * 
 * </ul>
 * 
 * <p>
 * 
 * For users that select "remember my password" in the login wizard, the
 * following cookies are used:
 * 
 * <ul>
 * <li><code>LOGIN_COOKIE</code>: to store the login.</li>
 * <li><code>ENCRYPTED_PASSWORD_COOKIE</code>: to store the encrypted
 * password.</li>
 * </ul>
 * 
 * <p>
 * 
 * In order to make transparent the management of session objects and cookies to
 * the implementation of controller actions, this class provides a number of
 * methods equivalent to some of the ones provided by
 * <code>AdminFacadeDelegate</code>
 * <code>UserFacadeDelegate</code>, which
 * manage session objects and cookies, and call upon the corresponding model
 * facades' methods. These methods are as follows:
 * 
 * <ul>
 * <li><code>changePassword</code></li>
 * <li><code>createAccount</code></li>
 * <li><code>findUserProfile</code></li>
 * <li><code>insertBetType</code></li>
 * <li><code>insertEvent</code></li>
 * <li><code>login</code></li>
 * <li><code>registerUser</code></li>
 * <li><code>updateAccountDetails</code></li>
 * <li><code>updateUserProfileDetails</code></li>
 * </ul>
 * 
 * It is important to remember that when needing to do some of the above actions
 * from the controller, the corresponding method of this class (one of the
 * previous list) must be called, and <b>not</b> the one in model facades. The
 * rest of methods of the model facades must be called directly. Currently,
 * there exists only one, <code>findUser</code>, but in a real portal there
 * would be more. For example, in a personalizable portal,
 * <code>UserFacadeDelegate</code> could include:
 * <code>findServicePreferences</code>, <code>updateServicePreferences</code>,
 * <code>findLayout</code>, <code>updateLayout</code>, etc. In a
 * electronic commerce shop, <code>UserFacadeDelegate</code> could include:
 * <code>getShoppingCart</code>, <code>addToShoppingCart</code>,
 * <code>removeFromShoppingCart</code>, <code>makeOrder</code>,
 * <code>findPendingOrders</code>, etc. When needing to invoke directly a
 * method of <code>UserFacadeDelegate</code>,
 * <code>SessionManager.getUserFacadeDelegate</code> must be invoked in order
 * to get the personal delegate (each user has his/her own delegate).
 * 
 * <p>
 * 
 * Same as <code>UserFacadeDelegate</code>, there exist some logical
 * restrictions with regard to the order of method calling. In particular, for
 * example, <code>updateUserProfileDetails</code> and
 * <code>changePassword</code> can not be called if <code>login</code> or
 * <code>registerUser</code> have not been previously called. After the user
 * calls one of these two methods, the user is said to be authenticated.
 * 
 */
public final class SessionManager {

        private final static String USER_FACADE_DELEGATE_SESSION_ATTRIBUTE = "userFacadeDelegate";

        public final static String OPTIONS_SESSION_ATTRIBUTE = "options";

        public final static String LOGIN_SESSION_ATTRIBUTE = "login";

        public final static String ACCOUNT_SESSION_ATTRIBUTE = "account";

        public final static String LOGIN_COOKIE = "login";

        public final static String ENCRYPTED_PASSWORD_COOKIE = "encryptedPassword";

        private final static int COOKIES_TIME_TO_LIVE_REMEMBER_MY_PASSWORD = 30 * 24 * 3600; // 30

        // days

        private final static int COOKIES_TIME_TO_LIVE_REMOVE = 0;

        private SessionManager() {
        }

        public final static void login(HttpServletRequest request,
                        HttpServletResponse response, String login,
                        String clearPassword, boolean rememberMyPassword)
                        throws IncorrectPasswordException,
                        InstanceNotFoundException, InternalErrorException {
                /*
                 * Try to login, and if successful, update session with the
                 * necessary objects for an authenticated user.
                 */
                LoginResultTO loginResultTO = doLogin(request, login,
                                clearPassword, false, rememberMyPassword);

                /* Add cookies if requested. */
                if (rememberMyPassword) {
                        leaveCookies(response, login, loginResultTO
                                        .getPassword(),
                                        COOKIES_TIME_TO_LIVE_REMEMBER_MY_PASSWORD);
                }
        }

        public final static void registerUser(HttpServletRequest request,
                        UserProfileTO userTO, String creditCardNumber,
                        Calendar expirationDate, Double balance)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                /* Register user. */
                UserFacadeDelegate userFacadeDelegate = getUserFacadeDelegate(request);

                AccountTO account = userFacadeDelegate.registerUser(userTO,
                                creditCardNumber, expirationDate, balance);

                /*
                 * Update session with the necessary objects for an
                 * authenticated user.
                 */
                Locale locale = Locale.getDefault();

                updateSesssionForAuthenticatedUser(request, userTO.getLogin(),
                                locale, account.getAccountID());
        }

        public final static void updateUserProfileDetails(
                        HttpServletRequest request, UserProfileTO userTO)
                        throws InternalErrorException,
                        DuplicateInstanceException {
                /* Update user's profile details. */
                UserFacadeDelegate userFacadeDelegate = getUserFacadeDelegate(request);

                userFacadeDelegate.updateUserDetails(userTO);

                /* Update user's session objects. */
                Locale locale = Locale.getDefault();
                HttpSession session = request.getSession(true);
                Long accountID = (Long) session
                                .getAttribute(ACCOUNT_SESSION_ATTRIBUTE);
                updateSesssionForAuthenticatedUser(request, userTO.getLogin(),
                                locale, accountID);
        }

        public final static void changePassword(HttpServletRequest request,
                        HttpServletResponse response, String oldClearPassword,
                        String newClearPassword)
                        throws IncorrectPasswordException,
                        InternalErrorException {
                /* Change user's password. */
                UserFacadeDelegate userFacadeDelegate = getUserFacadeDelegate(request);

                userFacadeDelegate.changePassword(oldClearPassword,
                                newClearPassword);
                /* Remove cookies. */
                leaveCookies(response, "", "", COOKIES_TIME_TO_LIVE_REMOVE);
        }

        public final static void createAccount(HttpServletRequest request,
                        AccountTO accountTO) throws InternalErrorException,
                        DuplicateInstanceException {
                /* Create account. */
                UserFacadeDelegate userFacadeDelegate = getUserFacadeDelegate(request);

                AccountTO account = userFacadeDelegate.createAccount(accountTO);

                HttpSession session = request.getSession(true);
                String login = ((String) session
                                .getAttribute(LOGIN_SESSION_ATTRIBUTE));
                Locale locale = Locale.getDefault();

                updateSesssionForAuthenticatedUser(request, login, locale,
                                account.getAccountID());

        }

        public final static void updateAccountDetails(
                        HttpServletRequest request, AccountTO accountTO)
                        throws InternalErrorException,
                        DuplicateInstanceException, InstanceNotFoundException {
                /* Update user's profile details. */
                UserFacadeDelegate userFacadeDelegate = getUserFacadeDelegate(request);

                userFacadeDelegate.updateAccountDetails(accountTO);
        }

        public final static void insertEvent(HttpServletRequest request,
                        HttpServletResponse response, EventTO eventTO,
                        BetTypeTO betTypeTO) throws InternalErrorException,
                        DuplicateInstanceException {
                HttpSession session = request.getSession(true);
                List<BetOptionTO> options = (List<BetOptionTO>) session
                                .getAttribute(OPTIONS_SESSION_ATTRIBUTE);
                AdminFacadeDelegate adminFacadeDelegate = AdminFacadeDelegateFactory
                                .getDelegate();

                adminFacadeDelegate.insertEvent(eventTO, betTypeTO, options);
        }

        public final static void insertBetType(HttpServletRequest request,
                        HttpServletResponse response, BetTypeTO betTypeTO)
                        throws InternalErrorException,
                        DuplicateInstanceException {
                /* Insert bet type. */
                HttpSession session = request.getSession(true);
                List<BetOptionTO> options = (List<BetOptionTO>) session
                                .getAttribute(OPTIONS_SESSION_ATTRIBUTE);
                AdminFacadeDelegate adminFacadeDelegate = AdminFacadeDelegateFactory
                                .getDelegate();

                adminFacadeDelegate.insertBetType(betTypeTO, options);
        }

        /**
         * Destroyes session, and removes cookies if the user had selected
         * "remember my password".
         */
        public final static void logout(HttpServletRequest request,
                        HttpServletResponse response)
                        throws InternalErrorException {
                /* Remove cookies. */
                leaveCookies(response, "", "", COOKIES_TIME_TO_LIVE_REMOVE);

                /* Invalidate session. */
                HttpSession session = request.getSession(true);
                session.invalidate();
        }

        public final static UserFacadeDelegate getUserFacadeDelegate(
                        HttpServletRequest request)
                        throws InternalErrorException {
                HttpSession session = request.getSession(true);
                UserFacadeDelegate userFacadeDelegate = (UserFacadeDelegate) session
                                .getAttribute(USER_FACADE_DELEGATE_SESSION_ATTRIBUTE);

                if (userFacadeDelegate == null) {
                        userFacadeDelegate = UserFacadeDelegateFactory
                                        .getDelegate();
                        session.setAttribute(
                                        USER_FACADE_DELEGATE_SESSION_ATTRIBUTE,
                                        userFacadeDelegate);
                }

                return userFacadeDelegate;
        }

        /**
         * Guarantees that the session will have the necessary objects if the
         * user has been authenticated or had selected "remember my password" in
         * the past.
         */
        public final static void touchSession(HttpServletRequest request)
                        throws InternalErrorException {
                /* Check if "login" is in the session. */
                String login = null;
                HttpSession session = request.getSession(false);

                if (session != null) {

                        login = (String) session
                                        .getAttribute(LOGIN_SESSION_ATTRIBUTE);

                        if (login != null) {
                                return;
                        }
                }

                /*
                 * The user had not been authenticated or his/her session has
                 * expired. We need to check if the user has selected "remember
                 * my password" in the last login (login and password will come
                 * as cookies). If so, we reconstruct user's session objects.
                 */
                updateSessionFromCookies(request);
        }

        public final static boolean isUserAuthenticated(
                        HttpServletRequest request) {
                HttpSession session = request.getSession(false);

                if (session == null) {
                        return false;
                }
                return session.getAttribute(LOGIN_SESSION_ATTRIBUTE) != null;
        }

        public final static boolean isAdmin(HttpServletRequest request) {
                HttpSession session = request.getSession(false);

                if (session == null) {
                        return false;
                }
                String login = ((String) session
                                .getAttribute(LOGIN_SESSION_ATTRIBUTE));
                if (login == null) {
                        return false;
                }
                return login.equals("admin");
        }

        private final static void updateSesssionForAuthenticatedUser(
                        HttpServletRequest request, String login,
                        Locale locale, Long accountID)
                        throws InternalErrorException {
                /* Insert objects in session. */
                HttpSession session = request.getSession(true);

                session.setAttribute(LOGIN_SESSION_ATTRIBUTE, login);
                session.setAttribute(Globals.LOCALE_KEY, locale);
                session.setAttribute(ACCOUNT_SESSION_ATTRIBUTE, accountID);
        }

        /**
         * Tries to login (inserting necessary objects in the session) by using
         * cookies (if present).
         */
        private final static void updateSessionFromCookies(
                        HttpServletRequest request)
                        throws InternalErrorException {
                /* Check if there are cookies in the request */
                Cookie[] cookies = request.getCookies();
                if (cookies == null) {
                        return;
                }

                /*
                 * Check if the login and the encrypted password come as
                 * cookies.
                 */
                String login = null;
                String encryptedPassword = null;
                int foundCookies = 0;

                for (int i = 0; (i < cookies.length) && (foundCookies < 2); i++) {
                        if (cookies[i].getName().equals(LOGIN_COOKIE)) {
                                login = cookies[i].getValue();
                                foundCookies++;
                        }
                        if (cookies[i].getName().equals(
                                        ENCRYPTED_PASSWORD_COOKIE)) {
                                encryptedPassword = cookies[i].getValue();
                                foundCookies++;
                        }
                }

                if (foundCookies != 2) {
                        return;
                }

                /*
                 * Try to login, and if successful, update session with the
                 * necessary objects for an authenticated user.
                 */
                try {
                        doLogin(request, login, encryptedPassword, true, true);
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) { // Incorrect login or encryptedPassword
                        return;
                }
        }

        private final static void leaveCookies(HttpServletResponse response,
                        String login, String encryptedPassword, int timeToLive) {
                /* Create cookies. */
                Cookie loginCookie = new Cookie(LOGIN_COOKIE, login);
                Cookie encryptedPasswordCookie = new Cookie(
                                ENCRYPTED_PASSWORD_COOKIE, encryptedPassword);

                /* Set maximum age to cookies. */
                loginCookie.setMaxAge(timeToLive);
                encryptedPasswordCookie.setMaxAge(timeToLive);

                /* Add cookies to response. */
                response.addCookie(loginCookie);
                response.addCookie(encryptedPasswordCookie);
        }

        /**
         * Tries to log in with the corresponding method of
         * <code>UserFacadeDelegate</code>, and if successful, inserts in the
         * session the necessary objects for an authenticated user.
         */
        private final static LoginResultTO doLogin(HttpServletRequest request,
                        String login, String password,
                        boolean passwordIsEncrypted, boolean rememberMyPassword)
                        throws IncorrectPasswordException,
                        InstanceNotFoundException, InternalErrorException {
                /* Check "login" and "clearPassword". */
                UserFacadeDelegate userFacadeDelegate = getUserFacadeDelegate(request);

                LoginResultTO loginResultTO = userFacadeDelegate.login(login,
                                password, passwordIsEncrypted);

                /* Insert necessary objects in the session. */
                Locale locale = Locale.getDefault();
                Long accountID;
                if (!login.equals("admin"))
                        accountID = userFacadeDelegate.findAccountsByUser(-1,
                                        -1).getAccountTOs().get(0)
                                        .getAccountID();
                else
                        accountID = new Long("0");
                updateSesssionForAuthenticatedUser(request, login, locale,
                                accountID);

                /* Return "loginResultTO". */
                return loginResultTO;
        }

        public final static void saveOptions(HttpServletRequest request,
                        List<BetOptionTO> options) {
                /* Insert options in session. */
                HttpSession session = request.getSession(true);

                session.setAttribute(OPTIONS_SESSION_ATTRIBUTE, options);
        }

        public final static void saveAccount(HttpServletRequest request,
                        Long accountID) {
                /* Insert account in session. */
                HttpSession session = request.getSession(true);

                session.setAttribute(ACCOUNT_SESSION_ATTRIBUTE, accountID);
        }

}
