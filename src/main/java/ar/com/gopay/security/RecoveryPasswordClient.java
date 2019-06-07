package ar.com.gopay.security;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RecoveryPasswordClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 7, max = 50)
    @Email
    private String email;

    @OneToOne
    private RecoveryPasswordToken recoveryPasswordToken;

    public RecoveryPasswordClient(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RecoveryPasswordClient{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
