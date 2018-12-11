package ar.com.gopay.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NosisValidationRequest {

    @JsonProperty("Usuario")
    private String usuario;

    @JsonProperty("Token")
    private String token;

    @JsonProperty("Documento")
    private String documento;

    @JsonProperty("Celular")
    private String celular;

    @JsonProperty("NroGrupoVID")
    private String nroGrupoVID;

    public NosisValidationRequest(String usuario, String token, String documento, String celular) {
        this.usuario = usuario;
        this.token = token;
        this.documento = documento;
        this.celular = celular;
        this.nroGrupoVID = "1";
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getNroGrupoVID() {
        return nroGrupoVID;
    }

    public void setNroGrupoVID(String nroGrupoVID) {
        this.nroGrupoVID = nroGrupoVID;
    }
}
