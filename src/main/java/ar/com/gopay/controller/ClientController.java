package ar.com.gopay.controller;

import ar.com.gopay.domain.Client;
import ar.com.gopay.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/signin")
    public String signin() {

        return "signin";
    }

    @GetMapping("/signup")
    public String signup(ModelMap model,
                         Principal principal) {

        // If user is in session
        if(principal != null) {

            return "redirect:/home";
        }

        model.put("client", new Client());

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid Client client,
                         BindingResult result) {

        if(result.hasErrors()) {

            return "signup";

        } else if(clientService.existsByUsername(client.getUsername())) {

            result.addError(new FieldError(
                    "client",
                    "username",
                    "El usuario ya se encuentra registrado"
            ));

            return "signup";

        } else if(clientService.existsByDni(client.getDni())) {

            result.addError(new FieldError(
                    "client",
                    "dni",
                    "El DNI ya se encuentra registrado"
            ));

            return "signup";
        }

        client.setPassword(passwordEncoder.encode(client.getPassword()));

        clientService.save(client);

        return "signin";
    }

}
