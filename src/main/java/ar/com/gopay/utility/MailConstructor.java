package ar.com.gopay.utility;

import ar.com.gopay.security.RecoveryPasswordClient;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class EmailServiceImpl implements EmailServi {

    private final Environment env;

    public EmailServiceImpl(Environment env) {
        this.env = env;
    }

    public SimpleMailMessage constructSignUpTokenEmail(String contextPath,
                                                       Locale locale,
                                                       String token,
                                                       RecoveryPasswordClient recoveryPasswordClient) {

        SimpleMailMessage email = new SimpleMailMessage();

        String url = contextPath + "/account/recovery/edit/password/" + token;

        email.setTo(recoveryPasswordClient.getEmail());
        email.setSubject("GoPay - Recuperá tu clave");
        email.setText(url);
        //emailSender.send(message);
        /*
        //String message = "\nPlease click on this link to verify your email and edit your personal information. ";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recoveryPasswordClient.getEmail());
        email.setSubject("GoPay - Recuperá tu clave");
        email.setText(url);
        email.setFrom(env.getProperty("support.email"));
        */

        return email;
    }
}
