package ar.com.gopay.service;

import ar.com.gopay.domain.Client;
import ar.com.gopay.domain.PaymentLink;
import ar.com.gopay.domain.nosis.Nosis;
import ar.com.gopay.exception.RestTemplateResponseErrorHandler;
import ar.com.gopay.payload.NosisEvaluationRequest;
import ar.com.gopay.payload.NosisValidationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NosisService {

    @Autowired
    private RestTemplateBuilder builder;

    @Value("${nosis.user}")
    private String nosisUser;

    @Value("${nosis.token}")
    private String nosisToken;

    @Value("${nosis.ws1}")
    private String nosisWs1;

    @Value("${nosis.ws2}")
    private String nosisWs2;

    public Nosis getNosisByClient(Client client) {

        RestTemplate restTemplate = builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();

        StringBuilder nosisUrl = new StringBuilder();

        nosisUrl.append(nosisWs1 + "/variables?")
                .append("Usuario=").append(nosisUser)
                .append("&Token=").append(nosisToken)
                .append("&Documento=").append(client.getDni())
                .append("&Format=JSON")
                .append("&VR=VI_Nombre,VI_Apellido,CI_Vig_PeorSit,CI_Vig_Total_CantBcos,CI_12m_PeorSit,VR_Vig_CapacidadEndeu_Deuda,SCO_Vig");

        return restTemplate.exchange(nosisUrl.toString(),
                HttpMethod.GET, null,
                Nosis.class).getBody();
    }

    public Nosis validation(Client client) {

        RestTemplate restTemplate = builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();

        HttpEntity<NosisValidationRequest> request = new HttpEntity<>(
                new NosisValidationRequest(
                        nosisUser,
                        nosisToken,
                        client.getDni(),
                        client.getPhone()
                )
        );

        return restTemplate.exchange(nosisWs2 + "/validacion",
                HttpMethod.POST, request, Nosis.class)
                .getBody();
    }

    public Nosis evaluation(PaymentLink paymentLink, String pin) {

        RestTemplate restTemplate = builder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();

        HttpEntity<NosisEvaluationRequest> request = new HttpEntity<>(
                new NosisEvaluationRequest(
                        nosisUser,
                        nosisToken,
                        paymentLink.getNosisSms().getSmsTx(),
                        pin
                )
        );

        return restTemplate.exchange(nosisWs2 + "/evaluacion",
                HttpMethod.POST, request, Nosis.class)
                .getBody();
    }

}
