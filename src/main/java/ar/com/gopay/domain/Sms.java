package ar.com.gopay.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Sms {

    @NotBlank
    @Size(min = 8, max = 8, message = "{pin.size}")
    @Pattern(regexp = "^[A-Z]{2}-(\\d){5}$", message = "{pin.pattern}")
    private String pin;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
