package ar.com.gopay.bootstrap;

import ar.com.gopay.domain.Client;
import ar.com.gopay.domain.Company;
import ar.com.gopay.domain.PaymentLink;
import ar.com.gopay.domain.nosispayment.NosisVariable;
import ar.com.gopay.repository.ClientRepository;
import ar.com.gopay.repository.CompanyRepository;
import ar.com.gopay.repository.NosisPaymentVariableRepository;
import ar.com.gopay.repository.PaymentLinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

import static ar.com.gopay.domain.PaymentLinkState.PE;
import static ar.com.gopay.domain.nosis.NombreVariable.*;

@Slf4j
@Component
public class GoPayBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final PaymentLinkRepository paymentLinkRepository;
    private final NosisPaymentVariableRepository nosisPaymentVariableRepository;
    private final PasswordEncoder passwordEncoder;

    public GoPayBootstrap(ClientRepository clientRepository, CompanyRepository companyRepository, PaymentLinkRepository paymentLinkRepository, NosisPaymentVariableRepository nosisPaymentVariableRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.paymentLinkRepository = paymentLinkRepository;
        this.nosisPaymentVariableRepository = nosisPaymentVariableRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        try {

            Company company1 = new Company("despegar",
                    "despegar@email.com",
                    passwordEncoder.encode("123456"),
                    "despegar.com",
                    "https://www.staticontent.com/shifu/static/logos/despegar.svg");

            Company company2 = new Company("potiers",
                    "potiershome@email.com",
                    passwordEncoder.encode("123456"),
                    "Potiers Home",
                    "https://www.potiershome.com/images/logo.png");

            companyRepository.save(company1);
            companyRepository.save(company2);

            String token1 = UUID.randomUUID().toString();
            String token2 = UUID.randomUUID().toString();
            String token3 = UUID.randomUUID().toString();
            String token4 = UUID.randomUUID().toString();

            PaymentLink link1 = paymentLinkRepository.save(new PaymentLink(token1,
                    "Producto 1",
                    20000.0,
                    "123",
                    company1, PE));

            PaymentLink link2 = paymentLinkRepository.save(new PaymentLink(token2,
                    "Producto 2",
                    1250.0,
                    "456",
                    company2, PE));

            PaymentLink link3 = paymentLinkRepository.save(new PaymentLink(token3,
                    "Producto 3",
                    1500000.0,
                    "789",
                    company1, PE));

            PaymentLink link4 = paymentLinkRepository.save(new PaymentLink(token4,
                    "Producto 4",
                    15.0,
                    "abc",
                    company2, PE));

            clientRepository.saveAll(
                    Arrays.asList(
                            new Client("cliente1@email.com",
                                    passwordEncoder.encode("123456"),
                                    "Luciano",
                                    "Giannelli",
                                    "25458501", "2215560423"),
                            new Client("cliente2@email.com",
                                    passwordEncoder.encode("123456"),
                                    "Chuck",
                                    "Norris",
                                    "23788091", "1112345678"),
                            new Client("cliente3@email.com",
                                    passwordEncoder.encode("123456"),
                                    "Fabricio",
                                    "Aiello",
                                    "31454449", "3831234567"),
                            new Client("cliente4@email.com",
                                    passwordEncoder.encode("123456"),
                                    "Rocky",
                                    "Balboa",
                                    "10101010", "2966151234")
                    )
            );

            nosisPaymentVariableRepository.saveAll(
                    Arrays.asList(
                            new NosisVariable(CI_Vig_PeorSit.toString(),
                                    "Peor situación", 1, 2),
                            new NosisVariable(CI_Vig_Total_CantBcos.toString(),
                                    "Cantidad bancos", 0, 6),
                            new NosisVariable(CI_12m_PeorSit.toString(),
                                    "Peor situación - Últ. 12 Meses", 0, 3),
                            new NosisVariable(VR_Vig_CapacidadEndeu_Deuda.toString(),
                                    "Disponibilidad mensual, para nuevo endeudamiento",
                                    "value > (amount / 3)"),
                            new NosisVariable(SCO_Vig.toString(),
                                    "Score", 300, Integer.MAX_VALUE)
                    )
            );

            log.info("http://localhost:8000/gopay/payment-link/" +
                    link1.getId() + "/" + link1.getToken());
            log.info("http://localhost:8000/gopay/payment-link/" +
                    link2.getId() + "/" + link2.getToken());
            log.info("http://localhost:8000/gopay/payment-link/" +
                    link3.getId() + "/" + link3.getToken());
            log.info("http://localhost:8000/gopay/payment-link/" +
                    link4.getId() + "/" + link4.getToken());

        } catch (DataIntegrityViolationException ex) {
            log.info("Previously created data");
        }
    }
}
