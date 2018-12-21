package ar.com.gopay.domain.nosispayment;

import ar.com.gopay.domain.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "nosis_clients_data")
public class NosisClientData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String names;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("server_state")
    private Integer serverState;

    @JsonProperty("server_detail")
    private String serverDetail;

    private NosisState state;

    @JsonProperty("last_modified_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-03:00")
    @LastModifiedDate
    protected Date lastModifiedDate;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public NosisClientData() {
        this.lastModifiedDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public NosisState getState() {
        return state;
    }

    public void setState(NosisState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "NosisClientData{" +
                "id=" + id +
                ", names='" + names + '\'' +
                ", lastName='" + lastName + '\'' +
                ", serverState=" + serverState +
                ", serverDetail='" + serverDetail + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
