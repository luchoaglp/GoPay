package ar.com.gopay.config;

import ar.com.gopay.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    private final String[] PUBLIC_MATCHERS = {
            "/css/**", "/js/**", "/img/**", "/",
            "/payment-link/**",
            "/home", "/signin", "/signup",
            "/fee", // Only for development
            "/clients" // Only for development
            //h2-console/**"
    };

    private final String[] CLIENT_MATCHERS = {
            "/transactions/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable(); // Only for H2

        http
                //.addFilterBefore(authenticationFilter(),
                // UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS)
                .permitAll()

                //.antMatchers(CLIENT_MATCHERS).hasRole("CLIENT_ROLE")

                .anyRequest()
                .authenticated();

        http
                //.csrf().disable()
                .cors().disable()
                .formLogin()
                    .loginProcessingUrl("/login")
                    .loginPage("/signin").permitAll()
                    //.defaultSuccessUrl("/")
                    //.failureUrl("/login?error")
                    .successHandler(customAuthenticationSuccessHandler())
                    .failureHandler(customAuthenticationFailureHandler())
                .and()
                .logout()
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    //.addLogoutHandler(customLogoutHandler())
                    .logoutSuccessUrl("/home")
                /*.deleteCookies("remember-me").permitAll()
                .and()
                .rememberMe()
                */;
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}
