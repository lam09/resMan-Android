package lam.fooapp.communication;

import java.util.List;

import lam.fooapp.model.AccountRegisterFrom;
import lam.fooapp.model.AuthenticationRequest;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;
import lam.fooapp.model.Restaurant;
import lam.fooapp.model.RestaurantRegisterForm;

public interface FoodApi {
    String getFoodById(String id);
    public void login(AuthenticationRequest authenticationRequest,Communicator.DataReceiverCallback receiver);
    public void signup(AccountRegisterFrom form, Communicator.DataReceiverCallback receiver);
    public void authenticate(String token, Communicator.DataReceiverCallback receiver);
    public void createNewRestaurant(RestaurantRegisterForm form, Communicator.DataReceiverCallback receiverCallback);
    public void getFoods(Integer page, Integer pageSize, Communicator.DataReceiverCallback  dataCallback);
    public void getRestaurants( Communicator.DataReceiverCallback receiver);
    List<String> getAllFood();
    void updateFood(Food food,Communicator.DataReceiverCallback  dataCallback);
    void sendOrder(OrderForm orderForm, Communicator.DataReceiverCallback  dataCallback );
    void getOrderToday(Integer page, Integer pageSize, Order.OrderState orderState, Communicator.DataReceiverCallback  dataCallback);
}
