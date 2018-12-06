package ar.com.gopay.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client extends User {

    @JsonProperty("first_name")
    @NotBlank(message = "{firstName.notblank}")
    @Size(min = 2, max = 50, message =  "{firstName.size}")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 7, max = 8)
    private String dni;

    @JsonProperty("payment_links")
    @OneToMany(cascade = CascadeType.ALL)
    private List<PaymentLink> paymentLinks;

    public Client() { }

    public Client(String username, String email, String password,
                  String firstName, String lastName, String dni) {
        super(username, email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
    }

    public void addPaymentLink(PaymentLink paymentLink) {
        paymentLinks.add(paymentLink);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public List<PaymentLink> getPaymentLinks() {
        return paymentLinks;
    }

    public void setPaymentLinks(List<PaymentLink> paymentLinks) {
        this.paymentLinks = paymentLinks;
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dni='" + dni + '\'' +
                ", paymentLinks=" + paymentLinks +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
