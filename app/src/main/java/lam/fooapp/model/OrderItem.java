package lam.fooapp.model;

public class OrderItem {
    Integer foodSerial;
    String title;
    String price;

    public OrderItem(){}

    public Integer getFoodSerial() {
        return foodSerial;
    }

    public void setFoodSerial(Integer foodSerial) {
        this.foodSerial = foodSerial;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
