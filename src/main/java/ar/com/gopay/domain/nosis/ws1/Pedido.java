package ar.com.gopay.domain.nosis.ws1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pedido {

    @JsonProperty("Usuario")
    private int usuario;

    @JsonProperty("Documento")
    private String documento;

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "usuario=" + usuario +
                ", documento='" + documento + '\'' +
                '}';
    }
}
