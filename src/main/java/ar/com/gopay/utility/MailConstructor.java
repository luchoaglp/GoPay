package ar.com.gopay.utility;

import ar.com.gopay.security.RecoveryPasswordClient;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MailConstructor {

    private final Environment env;

    public MailConstructor(Environment env) {
        this.env = env;
    }


    public SimpleMailMessage constructSignUpTokenEmail(String contextPath,
                                                       Locale locale,
                                                       String token,
                                                       RecoveryPasswordClient recoveryPasswordClient) {

        String url = contextPath + "/register/" + token;
        //String message = "\nPlease click on this link to verify your email and edit your personal information. ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recoveryPasswordClient.getEmail());
        email.setSubject("GoPay - Recuperá tu clave");
        email.setText(url);
        email.setFrom(env.getProperty("support.email"));

        return email;
    }
}
