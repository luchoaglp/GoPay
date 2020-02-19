package ar.com.gopay.repository;

import ar.com.gopay.domain.PaymentLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentLinkRepository extends JpaRepository<PaymentLink, Long> {

    PaymentLink findByToken(String token);

    List<PaymentLink> findByClientIdOrderByIdDesc(Long clientId);
}