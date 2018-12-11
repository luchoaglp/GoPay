package ar.com.gopay.service;

import ar.com.gopay.domain.PaymentLink;
import ar.com.gopay.domain.nosis.Datos;
import ar.com.gopay.domain.nosis.Nosis;
import ar.com.gopay.domain.nosis.Resultado;
import ar.com.gopay.domain.nosis.Sms;
import ar.com.gopay.domain.nosispayment.NosisSmsData;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NosisSmsService {

    public boolean validateNosisSms(PaymentLink paymentLink, Nosis nosisWs2) {

        Resultado resultado = nosisWs2.getContenido().getResultado();
        Datos datos = nosisWs2.getContenido().getDatos();

        NosisSmsData nosisSmsData;

        if(paymentLink.getNosisSmsData() == null) {

            nosisSmsData = new NosisSmsData();

        } else {

            nosisSmsData = paymentLink.getNosisSmsData();

            nosisSmsData.setLastModifiedDate(new Date());
        }

        Integer serverState = resultado.getEstado();

        nosisSmsData.setServerState(serverState);
        nosisSmsData.setServerDetail(resultado.getNovedad());

        if(serverState == 200) {

            Sms sms = datos.getSms();

            if(sms.getTokenEnviado() != null &&
                    sms.getTokenEnviado()) {

                nosisSmsData.setSmsTx(datos.getConsultaId());
                nosisSmsData.setSmsState(sms.getEstado());
                nosisSmsData.setSmsDetail(sms.getNovedad());

                paymentLink.setNosisSmsData(nosisSmsData);

                return true;

            } else {

                paymentLink.setNosisSmsData(nosisSmsData);

                return  false;
            }

        } else {

            nosisSmsData.setSmsTx(null);

            paymentLink.setNosisSmsData(nosisSmsData);

            return false;
        }
    }



}
