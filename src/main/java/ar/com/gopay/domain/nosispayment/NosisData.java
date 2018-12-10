package ar.com.gopay.domain.nosispayment;

import ar.com.gopay.domain.PaymentLink;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "nosis_payments_data")
public class NosisData {

    @JsonIgnore
    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("nosis_payment_variable")
    @OneToOne(targetEntity = NosisVariable.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "nosis_payment_variable_id")
    private NosisVariable nosisVariable;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private PaymentLink paymentLink;

    private Integer value;

    @JsonProperty("real_value")
    private String realValue;

    private NosisState state;

    public NosisData() { }

    public NosisData(NosisVariable nosisVariable) {
        this.nosisVariable = nosisVariable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NosisVariable getNosisVariable() {
        return nosisVariable;
    }

    public void setNosisVariable(NosisVariable nosisVariable) {
        this.nosisVariable = nosisVariable;
    }

    public PaymentLink getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(PaymentLink paymentLink) {
        this.paymentLink = paymentLink;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public NosisState getState() {
        return state;
    }

    public void setState(NosisState state) {
        this.state = state;
    }

    public String getRealValue() {
        return realValue;
    }

    public void setRealValue(String realValue) {
        this.realValue = realValue;
    }
}
