package ar.com.gopay.domain.nosis;

import ar.com.gopay.domain.nosispayment.NosisSmsState;
import com.fasterxml.jackson.annotation.JsonProperty;

import static ar.com.gopay.domain.nosispayment.NosisSmsState.*;

public class Sms {

    @JsonProperty("TokenEnviado")
    private Boolean tokenEnviado;

    @JsonProperty("Novedad")
    private String novedad;

    @JsonProperty("Estado")
    private String estado;

    public Boolean getTokenEnviado() {
        return tokenEnviado;
    }

    public void setTokenEnviado(Boolean tokenEnviado) {
        this.tokenEnviado = tokenEnviado;
    }

    public String getNovedad() {
        return novedad;
    }

    public void setNovedad(String novedad) {
        this.novedad = novedad;
    }

    public String getEstado() {
        return estado;
    }

    public NosisSmsState getState() {
        switch (estado) {
            case "PENDIENTE":
                return PENDIENTE;
            case "APROBADO":
                return APROBADO;
            case "RECHAZADO":
                return RECHAZADO;
            case "ERROR":
                return ERROR;
        }

        return null;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Sms{" +
                "tokenEnviado=" + tokenEnviado +
                ", novedad='" + novedad + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}
