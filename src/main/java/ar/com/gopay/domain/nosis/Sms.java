package ar.com.gopay.domain.nosis;

import com.fasterxml.jackson.annotation.JsonProperty;

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
