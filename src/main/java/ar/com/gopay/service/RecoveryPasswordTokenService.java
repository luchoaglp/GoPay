package ar.com.gopay.service;

import ar.com.gopay.repository.RecoveryPasswordTokenRepository;
import ar.com.gopay.security.RecoveryPasswordToken;
import org.springframework.stereotype.Service;

@Service
public class RecoveryPasswordTokenService {

    private final RecoveryPasswordTokenRepository recoveryPasswordTokenRepository;

    public RecoveryPasswordTokenService(RecoveryPasswordTokenRepository recoveryPasswordTokenRepository) {
        this.recoveryPasswordTokenRepository = recoveryPasswordTokenRepository;
    }

    public RecoveryPasswordToken save(RecoveryPasswordToken recoveryPasswordToken) {
        return recoveryPasswordTokenRepository.save(recoveryPasswordToken);
    }
}
