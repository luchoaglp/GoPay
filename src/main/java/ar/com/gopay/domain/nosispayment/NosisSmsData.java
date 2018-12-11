package ar.com.gopay.domain.nosispayment;

import ar.com.gopay.domain.PaymentLink;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "nosis_sms_data")
public class NosisSmsData {

    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("server_state")
    private Integer serverState;

    @JsonProperty("server_detail")
    private String serverDetail;

    @JsonProperty("sms_state")
    private String smsState;

    @JsonProperty("sms_detail")
    private String smsDetail;

    @JsonProperty("sms_tx")
    private String smsTx;

    @JsonProperty("last_modified_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-03:00")
    @LastModifiedDate
    protected Date lastModifiedDate;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private PaymentLink paymentLink;

    public NosisSmsData() {
        this.lastModifiedDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getServerState() {
        return serverState;
    }

    public void setServerState(Integer serverState) {
        this.serverState = serverState;
    }

    public String getServerDetail() {
        return serverDetail;
    }

    public void setServerDetail(String serverDetail) {
        this.serverDetail = serverDetail;
    }

    public String getSmsState() {
        return smsState;
    }

    public void setSmsState(String smsState) {
        this.smsState = smsState;
    }

    public String getSmsDetail() {
        return smsDetail;
    }

    public void setSmsDetail(String smsDetail) {
        this.smsDetail = smsDetail;
    }

    public PaymentLink getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(PaymentLink paymentLink) {
        this.paymentLink = paymentLink;
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

    @Override
    public String toString() {
        return "NosisSmsData{" +
                "id=" + id +
                ", serverState=" + serverState +
                ", serverDetail='" + serverDetail + '\'' +
                ", smsState='" + smsState + '\'' +
                ", smsDetail='" + smsDetail + '\'' +
                ", smsTx='" + smsTx + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
