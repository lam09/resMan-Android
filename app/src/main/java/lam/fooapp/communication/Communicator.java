package lam.fooapp.communication;


import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import lam.fooapp.Utils.Constant;
import lam.fooapp.Utils.Utils;
import lam.fooapp.communication.socketio.CustomListener;
import lam.fooapp.model.AccountRegisterFrom;
import lam.fooapp.model.AuthenticationRequest;
import lam.fooapp.model.EventData;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;
import lam.fooapp.model.RestaurantRegisterForm;


import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

public class Communicator {

    private FoodApi foodApi;
    public Communicator()
    {
        foodApi = new SpringFoodApi();
    }

    public void login(AuthenticationRequest authenticationRequest,Communicator.DataReceiverCallback receiver){
        foodApi.login(authenticationRequest,receiver);
    }
    public void signup(AccountRegisterFrom authenticationRequest, Communicator.DataReceiverCallback receiver){
        foodApi.signup(authenticationRequest,receiver);
    }

    public void createNewRestaurant(RestaurantRegisterForm form, DataReceiverCallback cb){
        foodApi.createNewRestaurant(form,cb);
    }
    public void getRestaurants(DataReceiverCallback cb){
        System.out.println("Get restaurant List communicator");
        foodApi.getRestaurants(cb);
    }
    public void authenticateByToken(String token, DataReceiverCallback receiver){
        foodApi.authenticate(token,receiver);
    }

    public void getFoods(Integer page, Integer pageSize, DataReceiverCallback  dataCallback){
        foodApi.getFoods(page,pageSize,dataCallback);
    }

    public void updateFood(Food food, DataReceiverCallback  dataCallback){
        foodApi.updateFood(food,dataCallback);
    }
    public void sendOrder(OrderForm orderForm, DataReceiverCallback  dataCallback ){
        foodApi.sendOrder(orderForm,dataCallback);
    }
    public void getOrderToday(Integer page, Integer pageSize, Order.OrderState orderState, DataReceiverCallback  dataCallback){
        foodApi.getOrderToday(page,pageSize,orderState,dataCallback);
    }

    public interface DataReceiverCallback<DataType>{
        void onDataRecieved(String result);
        void onError();
        default void onNoAcces(){
            return;
        };
    }
}
