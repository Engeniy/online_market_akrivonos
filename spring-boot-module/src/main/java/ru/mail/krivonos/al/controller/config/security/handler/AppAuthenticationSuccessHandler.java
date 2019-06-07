package ru.mail.krivonos.al.controller.config.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ru.mail.krivonos.al.controller.exceptions.IllegalAuthorityStateException;
import ru.mail.krivonos.al.controller.exceptions.IllegalResponseStateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.ADMIN_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.CUSTOMER_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.AuthorityConstants.SALE_AUTHORITY_NAME;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ARTICLES_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.ITEMS_PAGE_URL;
import static ru.mail.krivonos.al.controller.constant.URLConstants.USERS_PAGE_URL;

public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String AUTHORITY_STATE_ERROR_MESSAGE = "%s tried to authenticate, but doesn't" +
            " have any proper authority.";
    private static final String COMMITTED_RESPONSE_ERROR_MESSAGE = "Response have been committed. Unable to redirect " +
            "%s to %s.";

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException {
        String targetURL = null;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            switch (authority.getAuthority()) {
                case ADMIN_AUTHORITY_NAME:
                    targetURL = USERS_PAGE_URL;
                    break;
                case CUSTOMER_AUTHORITY_NAME:
                    targetURL = ARTICLES_PAGE_URL;
                    break;
                case SALE_AUTHORITY_NAME:
                    targetURL = ITEMS_PAGE_URL;
                    break;
                default:
                    targetURL = null;
            }
        }
        if (targetURL == null) {
            throw new IllegalAuthorityStateException(String.format(AUTHORITY_STATE_ERROR_MESSAGE,
                    authentication.getName()));
        }
        if (response.isCommitted()) {
            throw new IllegalResponseStateException(String.format(COMMITTED_RESPONSE_ERROR_MESSAGE,
                    authentication.getName(), targetURL));
        }
        redirectStrategy.sendRedirect(request, response, targetURL);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
