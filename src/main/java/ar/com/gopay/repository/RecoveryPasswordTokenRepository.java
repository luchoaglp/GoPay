package ar.com.gopay.repository;

import ar.com.gopay.security.RecoveryPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoveryPasswordTokenRepository extends JpaRepository<RecoveryPasswordToken, Long> {

}
