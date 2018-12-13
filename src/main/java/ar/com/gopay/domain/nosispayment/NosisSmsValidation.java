package ar.com.gopay.domain.nosispayment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "nosis_sms_validation")
public class NosisSmsValidation {

    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("server_state")
    private Integer serverState;

    @JsonProperty("server_detail")
    private String serverDetail;

    @JsonProperty("sms_detail")
    private String smsDetail;

    @JsonProperty("isSmsSent")
    private Boolean isSmsSent = false;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nosis_sms_id")
    private NosisSms nosisSms;

    public NosisSmsValidation() {
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

    public String getSmsDetail() {
        return smsDetail;
    }

    public void setSmsDetail(String smsDetail) {
        this.smsDetail = smsDetail;
    }

    public NosisSms getNosisSms() {
        return nosisSms;
    }

    public void setNosisSms(NosisSms nosisSms) {
        this.nosisSms = nosisSms;
    }

    public Boolean getSmsSent() {
        return isSmsSent;
    }

    public void setSmsSent(Boolean smsSent) {
        isSmsSent = smsSent;
    }

    @Override
    public String toString() {
        return "NosisSmsValidation{" +
                "id=" + id +
                ", serverState=" + serverState +
                ", serverDetail='" + serverDetail + '\'' +
                ", smsDetail='" + smsDetail + '\'' +
                ", isSmsSent=" + isSmsSent +
                '}';
    }
}
