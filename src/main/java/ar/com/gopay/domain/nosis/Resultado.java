package ar.com.gopay.domain.nosis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Resultado {

    @JsonProperty("Estado")
    private int estado;

    @JsonProperty("Novedad")
    private String novedad;

    @JsonProperty("Transaccion")
    private String transaccion;

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getNovedad() {
        return novedad;
    }

    public void setNovedad(String novedad) {
        this.novedad = novedad;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    @Override
    public String toString() {
        return "Resultado{" +
                "estado=" + estado +
                ", novedad='" + novedad + '\'' +
                ", transaccion='" + transaccion + '\'' +
                '}';
    }
}
