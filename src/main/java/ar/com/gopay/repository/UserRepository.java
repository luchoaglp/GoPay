package ar.com.gopay.repository;

import ar.com.gopay.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {


}
