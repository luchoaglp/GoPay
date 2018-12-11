package ar.com.gopay.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NosisEvaluationRequest {

    @JsonProperty("Usuario")
    private String usuario;

    @JsonProperty("Token")
    private String token;

    @JsonProperty("IdConsulta")
    private String consultaId;

    public NosisEvaluationRequest(String usuario, String token, String consultaId) {
        this.usuario = usuario;
        this.token = token;
        this.consultaId = consultaId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(String consultaId) {
        this.consultaId = consultaId;
    }
}
