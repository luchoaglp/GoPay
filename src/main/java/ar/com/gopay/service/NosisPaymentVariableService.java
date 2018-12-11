package ar.com.gopay.service;

import ar.com.gopay.domain.*;
import ar.com.gopay.domain.nosis.*;
import ar.com.gopay.domain.nosispayment.NosisClientData;
import ar.com.gopay.domain.nosispayment.NosisData;
import ar.com.gopay.domain.nosispayment.NosisVariable;
import ar.com.gopay.repository.NosisPaymentVariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static ar.com.gopay.domain.PaymentLinkState.RE;
import static ar.com.gopay.domain.nosispayment.NosisState.APPROVED;
import static ar.com.gopay.domain.nosispayment.NosisState.REJECTED;
import static ar.com.gopay.domain.nosis.NombreVariable.*;

@Service
public class NosisPaymentVariableService {

    @Autowired
    private NosisPaymentVariableRepository nosisPaymentVariableRepository;

    public boolean validateNosisData(Client client, PaymentLink paymentLink, Nosis nosis, double amount) {

        Resultado resultado = nosis.getContenido().getResultado();
        Datos datos = nosis.getContenido().getDatos();

        NosisClientData nosisClientData;

        if(client.getNosisClientData() == null) {

            nosisClientData = new NosisClientData();

        } else {

            nosisClientData = client.getNosisClientData();

            nosisClientData.setLastModifiedDate(new Date());
        }

        Integer serverState = resultado.getEstado();

        nosisClientData.setServerState(serverState);
        nosisClientData.setServerDetail(resultado.getNovedad());

        boolean isValidTransaction = true;

        if(serverState == 200) {

            nosisClientData.setState(APPROVED);

            for (Variable variable : datos.getVariables()) {

                if (variable.getNombre().equals(VI_Nombre)) {

                    nosisClientData.setNames(variable.getValor());

                    if (!variable.getValor().contains(client.getFirstName().toUpperCase())) {
                        nosisClientData.setState(REJECTED);
                    }

                } else if (variable.getNombre().equals(VI_Apellido)) {

                    nosisClientData.setLastName(variable.getValor());

                    if (!variable.getValor().contains(client.getLastName().toUpperCase())) {
                        nosisClientData.setState(REJECTED);
                    }

                } else {

                    if(nosisClientData.getState().equals(APPROVED)) {

                        NosisData nosisData = null;

                        if (variable.getNombre().equals(CI_Vig_PeorSit)) {
                            nosisData = getNosisData(variable, CI_Vig_PeorSit);
                            paymentLink.addNosisData(nosisData);

                        } else if (variable.getNombre().equals(CI_Vig_Total_CantBcos)) {
                            nosisData = getNosisData(variable, CI_Vig_Total_CantBcos);
                            paymentLink.addNosisData(nosisData);

                        } else if (variable.getNombre().equals(CI_12m_PeorSit)) {
                            nosisData = getNosisData(variable, CI_12m_PeorSit);
                            paymentLink.addNosisData(nosisData);

                        } else if (variable.getNombre().equals(VR_Vig_CapacidadEndeu_Deuda)) {
                            nosisData = getNosisData(variable, VR_Vig_CapacidadEndeu_Deuda, amount);
                            paymentLink.addNosisData(nosisData);

                        } else if (variable.getNombre().equals(SCO_Vig)) {
                            nosisData = getNosisData(variable, SCO_Vig);
                            paymentLink.addNosisData(nosisData);
                        }

                        if(isValidTransaction && nosisData.getState().equals(REJECTED)) {
                            isValidTransaction = false;
                            paymentLink.setState(RE);
                        }

                    } else {

                        client.setNosisClientData(nosisClientData);
                        paymentLink.setState(RE);

                        return false;
                    }
                }
            }

        } else {

            nosisClientData.setNames(null);
            nosisClientData.setLastName(null);
            nosisClientData.setState(REJECTED);
            paymentLink.setState(RE);

            client.setNosisClientData(nosisClientData);

            return false;
        }

        client.setNosisClientData(nosisClientData);

        return isValidTransaction;
    }

    private NosisData getNosisData(Variable variable, NombreVariable nombreVariable, double amount) {

        NosisData nosisData = new NosisData(nosisPaymentVariableRepository.findByName(nombreVariable.toString()));

        int value = -1;

        if(!variable.getValor().isEmpty()) {
            value = Integer.valueOf(variable.getValor());
        }

        nosisData.setValue(value);
        nosisData.setRealValue(variable.getValor());
        nosisData.setState((value > (amount / 3) )
                ? APPROVED
                : REJECTED);

        return nosisData;
    }

    private NosisData getNosisData(Variable variable, NombreVariable nombreVariable) {

        NosisVariable nosisVariable = nosisPaymentVariableRepository.findByName(nombreVariable.toString());
        NosisData nosisData = new NosisData(nosisVariable);

        int value = -1;

        if(!variable.getValor().isEmpty()) {
            value = Integer.valueOf(variable.getValor());
        }

        nosisData.setValue(value);
        nosisData.setRealValue(variable.getValor());
        nosisData.setState((value >= nosisVariable.getMin() && value <= nosisVariable.getMax())
                ? APPROVED
                : REJECTED);

        return nosisData;
    }

}
