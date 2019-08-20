package ar.com.gopay.utility;

import ar.com.gopay.security.RecoveryPasswordClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MailConstructor {

    @Value("${email.user}")
    private String user;

    public SimpleMailMessage constructSignUpTokenEmail(String contextPath,
                                                       Locale locale,
                                                       String token,
                                                       RecoveryPasswordClient recoveryPasswordClient) {

        SimpleMailMessage email = new SimpleMailMessage();

        String txt = "Hacé click en el siguiente link para recuperar tu clave:\n\n";

        txt += contextPath + "/account/recovery/edit/password/" + token;

        email.setFrom(user);
        email.setTo(recoveryPasswordClient.getEmail());
        email.setSubject("GoPay - Recuperá tu clave");
        email.setText(txt);

        return email;
    }
}
