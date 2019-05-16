package ar.com.gopay.controller.client;

import ar.com.gopay.domain.Client;
import ar.com.gopay.service.ClientService;
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

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;

    public ClientController(ClientService clientService, PasswordEncoder passwordEncoder) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping({"", "/home"})
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

        } else if(clientService.existsByEmail(client.getEmail())) {

            result.addError(new FieldError(
                    "client",
                    "email",
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
