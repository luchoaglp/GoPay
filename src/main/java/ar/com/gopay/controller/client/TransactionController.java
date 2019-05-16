package ar.com.gopay.controller.client;

import ar.com.gopay.domain.PaymentLink;
import ar.com.gopay.security.UserPrincipal;
import ar.com.gopay.service.PaymentLinkService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final PaymentLinkService paymentLinkService;

    public TransactionController(PaymentLinkService paymentLinkService) {
        this.paymentLinkService = paymentLinkService;
    }

    @GetMapping
    public String profile(ModelMap model,
                          Principal principal) {

        if(principal == null) {
            return "signin";
        }

        UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<PaymentLink> paymentLinks = paymentLinkService.getByClientId(user.getId());

        model.put("paymentLinks", paymentLinks);

        return "transactions/transactions";
    }

}
