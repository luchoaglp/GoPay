package ar.com.gopay.domain.nosis.ws1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contenido {

    @JsonProperty("Pedido")
    private Pedido pedido;

    @JsonProperty("Resultado")
    private Resultado resultado;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("Datos")
    private Datos datos;

    /*
    @JsonProperty("status")
    private Status status;
    */

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public Datos getDatos() {
        return datos;
    }

    public void setDatos(Datos datos) {
        this.datos = datos;
    }
/*
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
*/

    @Override
    public String toString() {
        return "Contenido{" +
                "pedido=" + pedido +
                ", resultado=" + resultado +
                ", datos=" + datos +
                '}';
    }
}
