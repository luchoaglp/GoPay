package ar.com.gopay.controller.client;

import ar.com.gopay.domain.Client;
import ar.com.gopay.security.UserPrincipal;
import ar.com.gopay.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private ClientService clientService;

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
    public String edit(ModelMap model,
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
    public String edit(Client client,
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

        //System.out.println("Err: " + result.getAllErrors());

        if(result.hasErrors()) {
            return "account/edit/data";
        }

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        clientService.edit(client, user.getId());

        return "redirect:/account";
    }

}
