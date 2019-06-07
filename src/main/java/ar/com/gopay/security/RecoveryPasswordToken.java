package ar.com.gopay.security;

import ar.com.gopay.domain.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RecoveryPasswordToken {

    private static final long EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String token;

    private LocalDateTime expiryDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RecoveryPasswordClient recoveryPasswordClient;

    @OneToOne(cascade = CascadeType.ALL)
    private Client client;

    @Transient
    private boolean tokenExpired;

    public RecoveryPasswordToken(String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate();
    }

    public RecoveryPasswordToken(String token, Client client) {
        this.token = token;
        this.client = client;
        this.expiryDate = calculateExpiryDate();
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(EXPIRATION);
    }

    private void updateToken(String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate();
    }

    public boolean isTokenExpired() {
        return expiryDate.isBefore(LocalDateTime.now());
    }

    public void setSignUpClient(RecoveryPasswordClient recoveryPasswordClient) {
        this.recoveryPasswordClient = recoveryPasswordClient;
        recoveryPasswordClient.setRecoveryPasswordToken(this);
    }

    @Override
    public String toString() {
        return "RecoveryPasswordToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", expiryDate=" + expiryDate +
                ", tokenExpired=" + tokenExpired +
                '}';
    }

}
