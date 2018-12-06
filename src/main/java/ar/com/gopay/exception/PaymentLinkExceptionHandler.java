package ar.com.gopay.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class PaymentLinkExceptionHandler {

    @ExceptionHandler(PaymentLinkException.class)
    public String handlePaymentLinkException(HttpServletRequest request, Exception ex){

        request.setAttribute("error", ex.getMessage());

        return "payment/error";
    }

}
