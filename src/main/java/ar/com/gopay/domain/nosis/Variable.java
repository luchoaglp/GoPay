package ar.com.gopay.domain.nosis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Variable {

    @JsonProperty("Nombre")
    private NombreVariable nombre;

    @JsonProperty("Valor")
    private String valor;

    @JsonProperty("Descripcion")
    private String descripcion;

    @JsonProperty("criterio")
    private String criterio;

    public NombreVariable getNombre() {
        return nombre;
    }

    public void setNombre(NombreVariable nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "nombre=" + nombre +
                ", valor='" + valor + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", criterio='" + criterio + '\'' +
                '}';
    }
}
