package ar.com.gopay.domain;

import ar.com.gopay.domain.nosispayment.NosisClientData;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clients", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "dni" })
})
public class Client extends User {

    @JsonProperty("first_name")
    @NotBlank(message = "{firstName.notblank}")
    @Size(min = 2, max = 50, message =  "{firstName.size}")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "{lastName.notblank}")
    @Size(min = 2, max = 50, message =  "{lastName.size}")
    private String lastName;

    @NotBlank(message = "{dni.notblank}")
    @Size(min = 2, max = 50, message =  "{dni.size}")
    private String dni;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[1-3](\\d){9}$", message = "{phone.pattern}")
    private String phone;

    @JsonProperty("payment_links")
    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PaymentLink> paymentLinks;

    @JsonProperty("nosis_client_data")
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NosisClientData nosisClientData;

    public Client() {
        this.createdDate = new Date();
    }

    public Client(String username, String email, String password,
                  String firstName, String lastName, String dni, String phone) {
        super(username, email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.phone = phone;
    }

    public void addPaymentLink(PaymentLink paymentLink) {
        paymentLinks.add(paymentLink);
        paymentLink.setClient(this);
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<PaymentLink> getPaymentLinks() {
        return paymentLinks;
    }

    public void setPaymentLinks(List<PaymentLink> paymentLinks) {
        this.paymentLinks = paymentLinks;
    }

    public NosisClientData getNosisClientData() {
        return nosisClientData;
    }

    public void setNosisClientData(NosisClientData nosisClientData) {
        if (nosisClientData == null) {
            if (this.nosisClientData != null) {
                this.nosisClientData.setClient(null);
            }
        } else {
            nosisClientData.setClient(this);
        }
        this.nosisClientData = nosisClientData;
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dni='" + dni + '\'' +
                ", paymentLinks=" + paymentLinks +
                ", nosisClientData=" + nosisClientData +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
