package ar.com.gopay.controller;

import ar.com.gopay.domain.Client;
import ar.com.gopay.domain.PaymentLink;
import ar.com.gopay.exception.PaymentLinkException;
import ar.com.gopay.security.UserPrincipal;
import ar.com.gopay.service.ClientService;
import ar.com.gopay.service.PaymentLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/payment-link")
public class PaymentLinkController {

    @Autowired
    private PaymentLinkService paymentLinkService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{linkId}/{token}")
    public String start(@PathVariable("linkId") Long linkId,
                        @PathVariable("token") String token,
                        ModelMap model,
                        HttpSession session,
                        Principal principal) {

        PaymentLink paymentLink = paymentLinkService.getById(linkId);

        validatePaymentLink(paymentLink, token);

        model.put("paymentLink", paymentLink);

        session.setAttribute("linkId", linkId);
        session.setAttribute("token", token);

        // User not in session
        if(principal == null) {

            return "payment/link";

        } else {

            return "payment/check";
        }
    }

    private void validatePaymentLink(PaymentLink paymentLink, String token) {

      if(paymentLink == null || !paymentLink.getToken().equals(token)) {

            throw new PaymentLinkException("Token inválido");

        } else if(paymentLink.isTokenExpired()) {

            throw new PaymentLinkException("El token ha expirado");
        }

    }

    @GetMapping("/check")
    public String check(ModelMap model,
                        HttpSession session,
                        Principal principal) {

        PaymentLinkHelper paymentLinkHelper = getSessionPaymentLink(session);
        PaymentLink paymentLink = paymentLinkService.getById(paymentLinkHelper.getId());

        validatePaymentLink(paymentLink, paymentLinkHelper.getToken());

        model.put("paymentLink", paymentLink);

        if(principal == null) {

            return "payment/signin";
        }

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Client client = clientService.getById(user.getId());

        client.addPaymentLink(paymentLink);

        clientService.save(client);

        return "payment/check";
    }

    @GetMapping("/signin")
    public String signin(ModelMap model,
                         HttpSession session) {

        PaymentLinkHelper paymentLinkHelper = getSessionPaymentLink(session);
        PaymentLink paymentLink = paymentLinkService.getById(paymentLinkHelper.getId());

        validatePaymentLink(paymentLink, paymentLinkHelper.getToken());

        model.put("paymentLink", paymentLink);

        return "payment/signin";
    }

    @GetMapping("/signup")
    public String signup(ModelMap model,
                         HttpSession session,
                         Principal principal) {

        PaymentLinkHelper paymentLinkHelper = getSessionPaymentLink(session);
        PaymentLink paymentLink = paymentLinkService.getById(paymentLinkHelper.getId());

        validatePaymentLink(paymentLink, paymentLinkHelper.getToken());

        model.put("paymentLink", paymentLink);

        // If user is not in session
        if(principal == null) {

            model.put("client", new Client());
        }

        return "payment/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid Client client,
                         BindingResult result,
                         ModelMap model,
                         HttpSession session) {

        PaymentLinkHelper paymentLinkHelper = getSessionPaymentLink(session);
        PaymentLink paymentLink = paymentLinkService.getById(paymentLinkHelper.getId());

        validatePaymentLink(paymentLink, paymentLinkHelper.getToken());

        model.put("paymentLink", paymentLink);

        if(result.hasErrors()) {

            return "payment/signup";
            } else if(clientService.existsByUsername(client.getUsername())) {

            result.addError(new FieldError(
                    "client",
                    "username",
                    "El usuario ya se encuentra registrado"
            ));

            return "payment/signup";
        }

        client.setPassword(passwordEncoder.encode(client.getPassword()));

        clientService.save(client);

        return "redirect:/payment-link/signin";
    }

    private PaymentLinkHelper getSessionPaymentLink(HttpSession session) {
        return new PaymentLinkHelper((Long) session.getAttribute("linkId"),
                (String) session.getAttribute("token"));
    }

    class PaymentLinkHelper {

        private Long id;
        private String token;

        public PaymentLinkHelper(Long id, String token) {
            this.id = id;
            this.token = token;
        }

        public Long getId() {
            return id;
        }

        public String getToken() {
            return token;
        }
    }

}
