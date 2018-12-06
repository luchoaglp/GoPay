package ar.com.gopay.repository;

import ar.com.gopay.domain.Client;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ClientRepository extends UserRepository<Client> {

    @Override
    Optional<Client> findByUsername(String username);

    Boolean existsByUsername(String username);
}
