package ar.com.gopay.security;

import ar.com.gopay.domain.User;
import ar.com.gopay.repository.ClientRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientRepository clientRespository;

    public CustomUserDetailsService(ClientRepository clientRespository) {
        this.clientRespository = clientRespository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Let people login with email
        User user = clientRespository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email  : " + email)
                );

        return UserPrincipal.create(user);
    }

}
