package ar.com.gopay.domain.nosis.ws1;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Datos {

    @JsonProperty("Variables")
    private List<Variable> variables;

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return "Datos{" +
                "variables=" + variables +
                '}';
    }
}
