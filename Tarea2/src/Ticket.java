package Tarea2.src;

public class Ticket {
    
    private int id_hogar;
    private double income;
    private double expend;
    private int quantity;
    private double [] prices;
    private int [] brands;


    public Ticket(int id_hogar, double income, double expend, int quantity, double[] prices, int[] brands) {
        this.id_hogar = id_hogar;
        this.income = income;
        this.expend = expend;
        this.quantity = quantity;
        this.prices = prices;
        this.brands = brands;
    }

    public int getId_hogar() {
        return id_hogar;
    }

    public void setId_hogar(int id_hogar) {
        this.id_hogar = id_hogar;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpend() {
        return expend;
    }

    public void setExpend(double expend) {
        this.expend = expend;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double[] getPrices() {
        return prices;
    }

    public void setPrices(double[] prices) {
        this.prices = prices;
    }

    public int[] getBrands() {
        return brands;
    }

    public void setBrands(int[] brands) {
        this.brands = brands;
    }

    @Override
    public String toString() {
        return "Ticket [expend=" + expend + ", id_hogar=" + id_hogar + ", income=" + income
                +" quantity=" + quantity + "]";
    }
}