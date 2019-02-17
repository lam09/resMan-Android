package lam.fooapp.communication;

import java.util.List;

import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.AuthenticationRequest;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;

public interface FoodApi {
    String getFoodById(String id);
//    List<Food> getFoods(Integer page, Integer pageSize, RestRequest.DataCallback dataCallback);
    public void login(AuthenticationRequest authenticationRequest,Communicator.DataReceiverCallback receiver);

    public void getFoods(Integer page, Integer pageSize, Communicator.DataReceiverCallback  dataCallback);

    List<String> getAllFood();
    void updateFood(Food food,Communicator.DataReceiverCallback  dataCallback);
    void sendOrder(OrderForm orderForm, Communicator.DataReceiverCallback  dataCallback );
    void getOrderToday(Integer page, Integer pageSize, Order.OrderState orderState, Communicator.DataReceiverCallback  dataCallback);
}
