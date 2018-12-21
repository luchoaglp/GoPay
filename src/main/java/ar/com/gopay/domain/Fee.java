package ar.com.gopay.domain;

public class Fee {

    private int quantity;
    private double amount;

    public Fee() {}

    public Fee(int quantity, double amount) {
        this.quantity = quantity;
        this.amount = calculateAmount(quantity, amount);
    }

    private double calculateAmount(int quantity, double amount) {
        return Math.round((amount / quantity) * 100d) / 100d;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAmount() {
        return amount;
    }
}
