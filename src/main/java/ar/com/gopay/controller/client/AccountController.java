package ar.com.gopay.controller.client;

import ar.com.gopay.domain.Client;
import ar.com.gopay.exception.RecoveryPasswordException;
import ar.com.gopay.security.RecoveryPasswordClient;
import ar.com.gopay.security.RecoveryPasswordToken;
import ar.com.gopay.security.UserPrincipal;
import ar.com.gopay.service.ClientService;
import ar.com.gopay.service.RecoveryPasswordTokenService;
import ar.com.gopay.utility.MailConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final RecoveryPasswordTokenService recoveryPasswordTokenService;
    private final MailConstructor mailConstructor;
    private final JavaMailSender emailSender;

    public AccountController(ClientService clientService, PasswordEncoder passwordEncoder, RecoveryPasswordTokenService recoveryPasswordClientService, RecoveryPasswordTokenService recoveryPasswordTokenService, MailConstructor mailConstructor, JavaMailSender emailSender) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.recoveryPasswordTokenService = recoveryPasswordTokenService;
        this.mailConstructor = mailConstructor;
        this.emailSender = emailSender;
    }

    @GetMapping
    public String profile(ModelMap model,
                          Principal principal) {

        if(principal == null) {
            return "signin";
        }

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Client in session
        Client client = clientService.getById(user.getId());

        model.put("client", client);

        return "account/account";
    }

    @GetMapping("/edit/data")
    public String editData(ModelMap model,
                           Principal principal) {

        if(principal == null) {
            return "signin";
        }

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Client in session
        Client client = clientService.getById(user.getId());

        model.put("client", client);

        return "account/edit/data";
    }

    @PostMapping("/edit/data")
    public String editData(Client client,
                           BindingResult result,
                           Principal principal) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        if(principal == null) {
            return "signin";
        }

        Set<ConstraintViolation<Client>> violations = validator.validate(client);

        violations
                .stream()
                .forEach(violation -> {
                    if(!violation.getPropertyPath().toString().equals("password") &&
                            !violation.getPropertyPath().toString().equals("username") &&
                            !violation.getPropertyPath().toString().equals("email")) {

                        result.addError(new FieldError(
                                "client",
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        ));

                    }
                });

        if(result.hasErrors()) {
            return "account/edit/data";
        }

        UserPrincipal user = (UserPrincipal) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        clientService.editData(client, user.getId());

        return "redirect:/account";
    }

    @GetMapping("/edit/password")
    public String editPassword(Principal principal) {

        if(principal == null) {
            return "signin";
        }

        return "account/edit/password";
    }

    @PostMapping("/edit/password")
    public String editPassword(@RequestParam(name = "password1") String password1,
                               @RequestParam(name = "password2") String password2,
                               ModelMap model,
                               Principal principal) {

        if(principal == null) {
            return "signin";
        }

        if(password1.length() < 6) {

            model.put("errPass1", "La clave debe al menos contener 6 caracteres.");

            return "account/edit/password";
        }

        if(!password1.equals(password2)) {

            model.put("errPass2", "Las claves no coinciden.");

            return "account/edit/password";
        }

        UserPrincipal user = (UserPrincipal) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        clientService.editPassword(passwordEncoder.encode(password1), user.getId());

        return "redirect:/logout";
    }

    @GetMapping("/recovery/password")
    public String recoveryPassword(Model model,
                                   Principal principal) {

        // If user is in session
        if(principal != null) {
            return "redirect:/home";
        }

        model.addAttribute("recoveryPasswordClient", new RecoveryPasswordClient());

        return "account/recovery/password";
    }

    @PostMapping("/recovery/password")
    public String recoveryPassword(@Valid RecoveryPasswordClient recoveryPasswordClient,
                                   BindingResult result,
                                   HttpServletRequest request) {

        if(result.hasErrors()) {
            return "account/recovery/password";
        }

        Client client = clientService.getByEmail(recoveryPasswordClient.getEmail());

        if(client == null) {

            result.addError(new FieldError(
                    "recoveryPasswordClient",
                    "email",
                    "El email no se encuentra registrado"
            ));

            return "account/recovery/password";
        }

        String token = UUID.randomUUID().toString();

        recoveryPasswordClient.setEmail(recoveryPasswordClient.getEmail().trim());

        RecoveryPasswordToken recoveryPasswordToken = new RecoveryPasswordToken(token, client);
        recoveryPasswordToken.setSignUpClient(recoveryPasswordClient);

        //System.out.println(recoveryPasswordClient);
        System.out.println(recoveryPasswordToken);

        recoveryPasswordTokenService.save(recoveryPasswordToken);

        String appUrl = "http://" + request.getServerName() +
                ":" + request.getServerPort() + request.getContextPath();

        SimpleMailMessage smm = mailConstructor.constructSignUpTokenEmail(appUrl,
                request.getLocale(),
                token, recoveryPasswordClient);

        emailSender.send(smm);

        System.out.println(smm);

        return "redirect:/";
    }

    @GetMapping("/recovery/edit/password/{token}")
    public String recoveryEditPassword(@PathVariable("token") String token,
                                       Model model) {

        RecoveryPasswordToken recoveryPasswordToken = recoveryPasswordTokenService.findByToken(token);

        if(recoveryPasswordToken == null) {
            throw new RecoveryPasswordException("Token inválido");
        }

        model.addAttribute("token", recoveryPasswordToken);

        return "account/recovery/edit/password";
    }

    @PostMapping("/recovery/edit/password")
    public String recoveryEditPassword(@RequestParam(name = "password1") String password1,
                                       @RequestParam(name = "password2") String password2,
                                       @RequestParam(name = "tokenId") Long tokenId,
                                       @RequestParam(name = "token") String token,
                                       ModelMap model) {

        RecoveryPasswordToken recoveryPasswordToken = recoveryPasswordTokenService.findById(tokenId);

        if(recoveryPasswordToken == null ||
                !recoveryPasswordToken.getToken().equals(token)) {
            throw new RecoveryPasswordException("Token inválido");
        }

        if(recoveryPasswordToken.isTokenExpired()) {
            throw new RecoveryPasswordException("Token expirado");
        }

        if(password1.length() < 6) {
            model.addAttribute("token", recoveryPasswordToken);
            model.put("errPass1", "La clave debe al menos contener 6 caracteres.");

            return "account/recovery/edit/password";
        }

        if(!password1.equals(password2)) {
            model.addAttribute("token", recoveryPasswordToken);
            model.put("errPass2", "Las claves no coinciden.");

            return "account/recovery/edit/password";
        }

        Client client = clientService.getById(recoveryPasswordToken.getClient().getId());

        if(client == null) {
            throw new RecoveryPasswordException("Cliente inválido");
        }

        clientService.editPassword(passwordEncoder.encode(password1), client.getId());

        return "redirect:/";
    }

}
