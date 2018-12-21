package ar.com.gopay.domain.nosispayment;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@Table(name = "nosis_variables")
public class NosisVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer min;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer max;

    private String criterion;

    public NosisVariable() { }

    public NosisVariable(String name, String description, Integer min, Integer max) {
        this.name = name;
        this.description = description;
        this.min = min;
        this.max = max;
        this.criterion = min + " >= value <= " + max;
    }

    public NosisVariable(String name, String description, String criterion) {
        this.name = name;
        this.description = description;
        this.min = min;
        this.max = max;
        this.criterion = criterion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public String getCriterion() {
        return criterion;
    }

    public void setCriterion(String condition) {
        this.criterion = condition;
    }
}
