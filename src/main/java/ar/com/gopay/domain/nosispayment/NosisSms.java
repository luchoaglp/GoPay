package ar.com.gopay.domain.nosispayment;

import ar.com.gopay.domain.PaymentLink;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "nosis_sms")
public class NosisSms {

    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("sms_last_state")
    private String smsLastState;

    @JsonProperty("last_modified_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-03:00")
    @LastModifiedDate
    private Date lastModifiedDate;

    @JsonProperty("sms_tx")
    private String smsTx;

    @JsonProperty("phone")
    private String phone;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private PaymentLink paymentLink;

    @JsonProperty("nosis_sms_validation")
    @OneToOne(mappedBy = "nosisSms", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NosisSmsValidation nosisSmsValidation;

    @JsonProperty("nosis_sms_evaluation")
    @OneToOne(mappedBy = "nosisSms", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NosisSmsEvaluation nosisSmsEvaluation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSmsLastState() {
        return smsLastState;
    }

    public void setSmsLastState(String smsLastState) {
        this.smsLastState = smsLastState;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getSmsTx() {
        return smsTx;
    }

    public void setSmsTx(String smsTx) {
        this.smsTx = smsTx;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PaymentLink getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(PaymentLink paymentLink) {
        this.paymentLink = paymentLink;
    }

    public NosisSmsValidation getNosisSmsValidation() {
        return nosisSmsValidation;
    }

    public void setNosisSmsValidation(NosisSmsValidation nosisSmsValidation) {
        if (nosisSmsValidation == null) {
            if (this.nosisSmsValidation != null) {
                this.nosisSmsValidation.setNosisSms(null);
            }
        } else {
            nosisSmsValidation.setNosisSms(this);
        }
        this.nosisSmsValidation = nosisSmsValidation;
    }

    public NosisSmsEvaluation getNosisSmsEvaluation() {
        return nosisSmsEvaluation;
    }

    public void setNosisSmsEvaluation(NosisSmsEvaluation nosisSmsEvaluation) {
        if (nosisSmsEvaluation == null) {
            if (this.nosisSmsEvaluation != null) {
                this.nosisSmsEvaluation.setNosisSms(null);
            }
        } else {
            nosisSmsEvaluation.setNosisSms(this);
        }
        this.nosisSmsEvaluation = nosisSmsEvaluation;
    }

    @Override
    public String toString() {
        return "NosisSms{" +
                "id=" + id +
                ", smsLastState='" + smsLastState + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                ", smsTx='" + smsTx + '\'' +
                ", phone='" + phone + '\'' +
                ", nosisSmsValidation=" + nosisSmsValidation +
                ", nosisSmsEvaluation=" + nosisSmsEvaluation +
                '}';
    }
}
