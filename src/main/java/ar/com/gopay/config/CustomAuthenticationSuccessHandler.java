package ar.com.gopay.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        if (request.getParameter("action") != null) {

            if (request.getParameter("action").equals("pay")) {
                response.sendRedirect("payment-link/check");
            }

        } else {

            response.sendRedirect("home");
        }

        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request,
                          HttpServletResponse response, Authentication authentication)
            throws IOException {

        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {

        boolean isClient = false;
        boolean isCompany = false;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_CLIENT")) {
                isClient = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ROLE_COMPANY")) {
                isCompany = true;
                break;
            }
        }

        if (isClient) {
            return "/home";
        } else if (isCompany) {
            return "/company/home";
        } else {
            throw new IllegalStateException();
        }

    }
}
