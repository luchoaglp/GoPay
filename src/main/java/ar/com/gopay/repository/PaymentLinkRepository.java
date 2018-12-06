package ar.com.gopay.repository;

import ar.com.gopay.domain.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentLinkRepository extends JpaRepository<PaymentLink, Long> {

    PaymentLink findByToken(String token);

}