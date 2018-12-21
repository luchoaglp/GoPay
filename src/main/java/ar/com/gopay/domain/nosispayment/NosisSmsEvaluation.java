package ar.com.gopay.domain.nosispayment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "nosis_sms_evaluation")
public class NosisSmsEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("server_state")
    private Integer serverState;

    @JsonProperty("server_detail")
    private String serverDetail;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nosis_sms_id")
    private NosisSms nosisSms;

    public NosisSmsEvaluation() {
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

    public NosisSms getNosisSms() {
        return nosisSms;
    }

    public void setNosisSms(NosisSms nosisSms) {
        this.nosisSms = nosisSms;
    }

    @Override
    public String toString() {
        return "NosisSmsEvaluation{" +
                "id=" + id +
                ", serverState=" + serverState +
                ", serverDetail='" + serverDetail + '\'' +
                '}';
    }
}
