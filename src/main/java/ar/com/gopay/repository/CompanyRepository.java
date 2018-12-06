package ar.com.gopay.repository;

import ar.com.gopay.domain.Company;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CompanyRepository extends UserRepository<Company> {

}
