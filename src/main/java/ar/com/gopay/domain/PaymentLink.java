package ar.com.gopay.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "payments_links")
public class PaymentLink {

    private static final int EXPIRATION = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 36)
    private String token;

    @NotNull
    private Double amount;

    @JsonProperty("external_tx_id")
    @NotBlank
    @NotNull
    private String externalTxId;

    @OneToOne(targetEntity = Company.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Company company;

    @JsonProperty("expiry_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-03:00")
    private Date expiryDate;

    @JsonProperty("is_token_expired")
    @Transient
    private boolean tokenExpired;

    public PaymentLink() {
    }

    public PaymentLink(String token, Double amount, String externalTxId, Company company) {
        this.token = token;
        this.amount = amount;
        this.externalTxId = externalTxId;
        this.company = company;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(final Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public String getExternalTxId() {
        return externalTxId;
    }

    public void setExternalTxId(String externalTxId) {
        this.externalTxId = externalTxId;
    }

    public boolean isTokenExpired() {
        return this.tokenExpired = this.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime() <= 0;
    }

    public void setTokenExpired(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    @Override
    public String toString() {
        return "PaymentLink{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", amount=" + amount +
                ", externalTxId='" + externalTxId + '\'' +
                ", company=" + company +
                ", expiryDate=" + expiryDate +
                '}';
    }

}