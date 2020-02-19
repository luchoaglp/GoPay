package ar.com.gopay.repository;

import ar.com.gopay.domain.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ClientRepository extends UserRepository<Client> {

    Optional<Client> findByEmail(String email);

    Boolean existsByDni(String dni);

    boolean existsByEmail(String email);
}
