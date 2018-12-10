package ar.com.gopay.repository;

import ar.com.gopay.domain.nosispayment.NosisVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NosisPaymentVariableRepository extends JpaRepository<NosisVariable, Integer> {

    NosisVariable findByName(String name);

}