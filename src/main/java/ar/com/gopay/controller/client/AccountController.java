package ar.com.gopay.controller.client;

import ar.com.gopay.domain.Client;
import ar.com.gopay.security.UserPrincipal;
import ar.com.gopay.service.ClientService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;

    public AccountController(ClientService clientService, PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
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

}
