package ar.com.gopay.domain.nosis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Datos {

    @JsonProperty("IdConsulta")
    private String consultaId;

    @JsonIgnore
    @JsonProperty("Persona")
    private Persona persona;

    @JsonProperty("Sms")
    private Sms sms;

    @JsonProperty("Variables")
    private List<Variable> variables;

    public String getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(String consultaId) {
        this.consultaId = consultaId;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Sms getSms() {
        return sms;
    }

    public void setSms(Sms sms) {
        this.sms = sms;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

}
