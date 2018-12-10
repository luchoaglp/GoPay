package ar.com.gopay.controller;

import ar.com.gopay.domain.Client;
import ar.com.gopay.domain.PaymentLink;
import ar.com.gopay.domain.nosis.ws1.Nosis;
import ar.com.gopay.exception.PaymentLinkException;
import ar.com.gopay.security.UserPrincipal;
import ar.com.gopay.service.ClientService;
import ar.com.gopay.service.NosisPaymentVariableService;
import ar.com.gopay.service.NosisService;
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

import static ar.com.gopay.domain.PaymentLinkState.PE;

@Controller
@RequestMapping("/payment-link")
public class PaymentLinkController {

    @Autowired
    private PaymentLinkService paymentLinkService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NosisService nosisService;

    @Autowired
    private NosisPaymentVariableService nosisPaymentVariableService;


    @GetMapping("/{linkId}/{token}")
    public String start(@PathVariable("linkId") Long linkId,
                        @PathVariable("token") String token,
                        ModelMap model,
                        HttpSession session,
                        Principal principal) {

        PaymentLink paymentLink = paymentLinkService.getById(linkId);

        validatePaymentLink(paymentLink, token);

        session.setAttribute("linkId", linkId);
        session.setAttribute("token", token);

        model.put("paymentLink", paymentLink);

        // User not in session
        if(principal == null) {

            return "payment/link";

        } else {

            return "payment/check";
        }
    }

    @GetMapping("/check")
    public String check(ModelMap model,
                        HttpSession session,
                        Principal principal) {

        if(principal == null) {

            return "payment/signin";
        }

        PaymentLinkHelper paymentLinkHelper = getSessionPaymentLink(session);
        PaymentLink paymentLink = paymentLinkService.getById(paymentLinkHelper.getId());

        validatePaymentLink(paymentLink, paymentLinkHelper.getToken());

        model.put("paymentLink", paymentLink);

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Client client = clientService.getById(user.getId());

        if(paymentLink.getClient() == null) {

            Nosis nosis = nosisService.getNosisByClient(client);

            boolean isValidTransaction = nosisPaymentVariableService.processVariables(client, paymentLink, nosis, paymentLink.getAmount());

            client.addPaymentLink(paymentLink);

            // Client claims his token
            // If the transaction is rejected, it changes state to RE
            clientService.save(client);

            if(!isValidTransaction) {

                session.removeAttribute("linkId");
                session.removeAttribute("token");

                throw new PaymentLinkException("Transacción rechazada");
            }

            return "payment/check";

            // The link belongs to the Client, and the transaction is pending
        } else if(paymentLink.getClient().getId().equals(user.getId())) {

            return "payment/check";
        }

        throw new PaymentLinkException("Operación inválida");
    }

    @GetMapping("/signin")
    public String signin() {

        return "payment/signin";
    }

    @GetMapping("/signup")
    public String signup(ModelMap model,
                         Principal principal) {

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

        if(result.hasErrors()) {

            return "payment/signup";

        } else if(clientService.existsByUsername(client.getUsername())) {

            result.addError(new FieldError(
                    "client",
                    "username",
                    "El usuario ya se encuentra registrado"
            ));

            return "payment/signup";

        } else if(clientService.existsByDni(client.getDni())) {

            result.addError(new FieldError(
                    "client",
                    "dni",
                    "El DNI ya se encuentra registrado"
            ));

            return "payment/signup";
        }

        client.setPassword(passwordEncoder.encode(client.getPassword()));

        clientService.save(client);

        return "payment/signin";
    }

    private void validatePaymentLink(PaymentLink paymentLink, String token) {

        if(paymentLink == null || !paymentLink.getToken().equals(token) || !paymentLink.getState().equals(PE)) {

            throw new PaymentLinkException("Token inválido");

        } else if(paymentLink.isTokenExpired()) {

            throw new PaymentLinkException("El token ha expirado");
        }
    }

    private PaymentLinkHelper getSessionPaymentLink(HttpSession session) {

        Long linkId = (Long) session.getAttribute("linkId");

        if(linkId == null) {
            throw new PaymentLinkException("No tiene operaciones pendientes");
        }

        return new PaymentLinkHelper(linkId,
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
