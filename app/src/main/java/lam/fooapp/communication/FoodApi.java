package lam.fooapp.communication;

import java.util.List;

import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;

public interface FoodApi {
    String getFoodById(String id);
//    List<Food> getFoods(Integer page, Integer pageSize, RestRequest.DataCallback dataCallback);
    public void getFoods(Integer page, Integer pageSize, RestRequest.DataCallback dataCallback);

    List<String> getAllFood();
    String updateFood(String food);
    void sendOrder(OrderForm orderForm, RestRequest.DataCallback dataCallback );
    void getOrderToday(RestRequest.DataCallback dataCallback);
}
