package lam.fooapp.communication;

import java.util.List;

import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;

public interface FoodApi {
    String getFoodById(String id);
//    List<Food> getFoods(Integer page, Integer pageSize, RestRequest.DataCallback dataCallback);
    public void getFoods(Integer page, Integer pageSize, RestRequest.DataCallback dataCallback);

    List<String> getAllFood();
    void updateFood(Food food,RestRequest.DataCallback dataCallback);
    void sendOrder(OrderForm orderForm, RestRequest.DataCallback dataCallback );
    void getOrderToday(Integer page, Integer pageSize, Order.OrderState orderState, RestRequest.DataCallback dataCallback);
}
