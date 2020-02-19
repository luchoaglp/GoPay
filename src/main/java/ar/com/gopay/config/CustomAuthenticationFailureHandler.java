package ar.com.gopay.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        String url = "signin?";

        if(request.getParameter("action") != null) {

            if(request.getParameter("action").equals("pay")) {
                url += "action=pay&";
            }
        }
        response.sendRedirect(url +"error=" + exception.getMessage());
    }
}
