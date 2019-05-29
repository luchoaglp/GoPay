package ar.com.gopay.controller;

import ar.com.gopay.domain.Client;
import ar.com.gopay.domain.Fee;
import ar.com.gopay.domain.PaymentLink;
import ar.com.gopay.domain.Sms;
import ar.com.gopay.domain.nosis.Nosis;
import ar.com.gopay.exception.PaymentLinkException;
import ar.com.gopay.security.UserPrincipal;
import ar.com.gopay.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;

import static ar.com.gopay.domain.PaymentLinkState.OK;
import static ar.com.gopay.domain.PaymentLinkState.RE;
import static ar.com.gopay.domain.nosispayment.NosisSmsState.APROBADO;
import static ar.com.gopay.domain.nosispayment.NosisSmsState.PENDIENTE;

@Slf4j
@Controller
@RequestMapping("/payment-link")
public class PaymentLinkController {

    private final PaymentLinkService paymentLinkService;
    private final ClientService clientService;
    private final NosisService nosisService;
    private final NosisPaymentVariableService nosisPaymentVariableService;
    private final NosisSmsService nosisSmsService;

    public PaymentLinkController(PaymentLinkService paymentLinkService, ClientService clientService, NosisService nosisService, NosisPaymentVariableService nosisPaymentVariableService, NosisSmsService nosisSmsService) {
        this.paymentLinkService = paymentLinkService;
        this.clientService = clientService;
        this.nosisService = nosisService;
        this.nosisPaymentVariableService = nosisPaymentVariableService;
        this.nosisSmsService = nosisSmsService;
    }

    @GetMapping("/{linkId}/{token}")
    public String start(@PathVariable("linkId") Long linkId,
                        @PathVariable("token") String token,
                        ModelMap model,
                        HttpSession session,
                        Principal principal) {

        PaymentLink paymentLink = paymentLinkService.getById(linkId);

        // Verify that the payment link exists in the DB
        // Verify that the DB token is equal to the session token
        // Verify that the transaction is pending
        // Verify that the token has not expired
        // If the verification fails throws an exception
        validatePaymentLink(paymentLink, token);

        session.setAttribute("linkId", linkId);
        session.setAttribute("token", token);

        model.put("paymentLink", paymentLink);

        // Client is in session
        if(principal != null) {

            return "redirect:/payment-link/check";
        }

        return "payment/link";
    }

    @GetMapping("/check")
    public String check(ModelMap model,
                        HttpSession session,
                        Principal principal) {

        if(principal == null) {
            return "signin";
        }

        // Bring the payment link of the session, if it does not exist, throw an exception
        PaymentLinkHelper paymentLinkHelper = getSessionPaymentLink(session);
        PaymentLink paymentLink = paymentLinkService.getById(paymentLinkHelper.getId());

        // Verify that the payment link exists in DB
        // Verify that the DB token is equal to the session token
        // Verify that the transaction is pending
        // Verify that the token has not expired
        // If payment link is invalid, throws an exception
        validatePaymentLink(paymentLink, paymentLinkHelper.getToken());

        model.put("paymentLink", paymentLink);

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Client in session
        Client client = clientService.getById(user.getId());

        // The payment link doesn't belong to a Client
        if(paymentLink.getClient() == null) {

            model.put("phone", client.getPhone());
            model.put("sms", new Sms());

            Nosis nosis = nosisService.getNosisByClient(client);

            // Verify customer data against Nosis. (first verification)
            // Possible results of the verification: 1) Transaction is still pending. 2) Transaction is rejected
            // If the transaction is rejected, it changes state to RE
            nosisPaymentVariableService.validateNosisData(client, paymentLink, nosis, paymentLink.getAmount());

            if(!paymentLink.getState().equals(RE)) {

                nosis = nosisService.validation(client);

                // Possible PIN states: (PENDIENTE, ERROR)
                nosisSmsService.validateSms(paymentLink, nosis, client.getPhone());

                // The payment link is destroyed because the transaction was rejected
            } else {

                session.removeAttribute("linkId");
                session.removeAttribute("token");
            }

            client.addPaymentLink(paymentLink);

            // Client claims his payment linK
            clientService.save(client);

            if(paymentLink.getState().equals(RE)) {

                throw new PaymentLinkException("Transacción rechazada");
            }

            // If the SMS has not been sent
            model.put("smsError", !paymentLink.getNosisSms().getNosisSmsValidation().getSmsSent());

            return "payment/check";

            // The payment link belongs to the Client in session and the transaction is still pending
        } else if(paymentLink.getClient().getId().equals(user.getId())) {

            // TODO: previous PIN

            // If the SMS was sent
            if(paymentLink.getNosisSms().getNosisSmsValidation().getSmsSent()) {

                switch (paymentLink.getNosisSms().getSmsLastState()) {

                    case PENDIENTE:

                        model.put("phone", client.getPhone());
                        model.put("sms", new Sms());
                        model.put("smsError", false);

                        return "payment/check";

                    case APROBADO:

                        model.put("fees", Arrays.asList(
                                new Fee(3, paymentLink.getAmount()),
                                new Fee(6, paymentLink.getAmount())
                        ));

                        return "payment/fee";

                    //case ERROR:
                    case RECHAZADO:

                        model.put("phone", client.getPhone());
                        model.put("smsError", true);
                        model.put("smsLastState", paymentLink.getNosisSms().getSmsLastState());

                        return "payment/check";
                }

            } else {
                model.put("smsError", true);
            }
        }

        throw new PaymentLinkException("Operación inválida");
    }

    @GetMapping("/resend")
    public String resend(ModelMap model,
                         HttpSession session,
                         Principal principal) {

        if(principal == null) {
            return "signin";
        }

        // Bring the payment link of the session, if it does not exist, throw an exception
        PaymentLinkHelper paymentLinkHelper = getSessionPaymentLink(session);
        PaymentLink paymentLink = paymentLinkService.getById(paymentLinkHelper.getId());

        // Verify that the payment link exists in DB
        // Verify that the DB token is equal to the session token
        // Verify that the transaction is pending
        // Verify that the token has not expired
        // If payment link is invalid, throws an exception
        validatePaymentLink(paymentLink, paymentLinkHelper.getToken());

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Client in session
        Client client = clientService.getById(user.getId());

        // The payment link belongs to the Client in session and the transaction is still pending
        if(paymentLink.getClient().getId().equals(user.getId())) {

            model.put("paymentLink", paymentLink);
            model.put("phone", client.getPhone());
            model.put("sms", new Sms());
            model.put("smsError", false);

            if(!paymentLink.getNosisSms().getSmsLastState().equals(PENDIENTE)) {

                Nosis nosis = nosisService.validation(client);

                // Possible PIN states: (PENDIENTE, ERROR)
                nosisSmsService.validateSms(paymentLink, nosis, client.getPhone());

                paymentLinkService.save(paymentLink);
            }

            return "payment/check";
        }

        throw new PaymentLinkException("Operación inválida");
    }

    @PostMapping("/validation")
    public String validation(@Valid Sms sms,
                             BindingResult result,
                             ModelMap model,
                             HttpSession session,
                             Principal principal) {

        // Verify that the payment link exists in DB
        // Verify that the DB token is equal to the session token
        // Verify that the transaction is pending
        // Verify that the token has not expired
        // Verify that the Client is in session
        // if payment link is invalid, throws an exception
        PaymentLink paymentLink = getPaymentLinkValidated(session, principal);

        Client client = paymentLink.getClient();

        // The payment link doesn't belong to a Client
        if(client == null) {
            throw new PaymentLinkException("No tiene operaciones pendientes");
        }

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // This link does not belong to this client
        if(!paymentLink.getClient().getId().equals(user.getId())) {
            throw new PaymentLinkException("Operación inválida");
        }

        model.put("paymentLink", paymentLink);

        if(result.hasErrors()) {

            model.put("phone", client.getPhone());
            model.put("smsError", !paymentLink.getNosisSms().getNosisSmsValidation().getSmsSent());

            return "payment/check";
        }

        // Verify the PIN entered by the Client. (second verification)
        Nosis nosis = nosisService.evaluation(paymentLink, sms.getPin().toUpperCase());

        // Verify the PIN entered by the Client. (second verification)
        // Possible PIN states: (APROBADO, RECHAZADO)
        nosisSmsService.evaluateSms(paymentLink, nosis);

        paymentLinkService.save(paymentLink);

        if(!paymentLink.getNosisSms().getSmsLastState().equals(APROBADO)) {

            model.put("phone", client.getPhone());
            model.put("smsError", true);
            model.put("smsLastState", paymentLink.getNosisSms().getSmsLastState());

            result.addError(new FieldError(
                    "sms",
                    "pin",
                    "Estado del PIN: " + paymentLink.getNosisSms().getSmsLastState()
            ));

            return "payment/check";
        }

        model.put("fees", Arrays.asList(
                new Fee(3, paymentLink.getAmount()),
                new Fee(6, paymentLink.getAmount())
        ));

        return "payment/fee";
    }

    @PostMapping("/fee")
    public String fee(@ModelAttribute("feesQuantity") int feesQuantity,
                      HttpSession session,
                      Principal principal) {

        PaymentLink paymentLink = getPaymentLinkValidated(session, principal);

        Client client = paymentLink.getClient();

        if(client == null) {
            throw new PaymentLinkException("No tiene operaciones pendientes");
        }

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // This link does not belong to this client
        if(!paymentLink.getClient().getId().equals(user.getId())) {
            throw new PaymentLinkException("Operación inválida");
        }

        if(!paymentLink.getNosisSms().getSmsLastState().equals(APROBADO)) {
            throw new PaymentLinkException("Operación inválida");
        }

        paymentLink.setFeesQuantity(feesQuantity);
        paymentLink.setState(OK);

        paymentLinkService.save(paymentLink);

        session.removeAttribute("linkId");
        session.removeAttribute("token");

        return "payment/success";
    }

    private PaymentLink getPaymentLinkValidated(HttpSession session, Principal principal) {

        if(principal == null) {
            throw new PaymentLinkException("No tiene operaciones pendientes");
        }

        // Bring the payment link of the session, if it does not exist, throw an exception
        PaymentLinkHelper paymentLinkHelper = getSessionPaymentLink(session);
        PaymentLink paymentLink = paymentLinkService.getById(paymentLinkHelper.getId());

        validatePaymentLink(paymentLink, paymentLinkHelper.getToken());

        return paymentLink;
    }

    private void validatePaymentLink(PaymentLink paymentLink, String token) {

        if(paymentLink == null){

            throw new PaymentLinkException("Token inválido");

        } else if(!paymentLink.getToken().equals(token)) {

            throw new PaymentLinkException("Token inválido");

        } else if(!paymentLink.isPending()) {

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
