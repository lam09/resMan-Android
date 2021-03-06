package lam.fooapp.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpHeaders;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import lam.fooapp.MangoApplication;
import lam.fooapp.Utils.Constant;
import lam.fooapp.communication.rests.RequestBuilder;
import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.AccountRegisterFrom;
import lam.fooapp.model.AccountRegisterResponse;
import lam.fooapp.model.AuthenticationRequest;
import lam.fooapp.model.AuthenticationResponse;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;
import lam.fooapp.model.Restaurant;
import lam.fooapp.model.RestaurantRegisterForm;


public class SpringFoodApi implements FoodApi {
    private RestRequest restRequest;
    private RequestBuilder builder;
    public String token= null;
    private Gson gson = new Gson();
    public SpringFoodApi(){
        builder=new RequestBuilder();
        restRequest = new RestRequest();
    }


    @Override
    public void login(final AuthenticationRequest authenticationRequest,final Communicator.DataReceiverCallback receiver) {
        new Thread(() ->{
                String url = Constant.MAIN_SERVER_URL+"auth/login";
                RestRequest restRequest = new RestRequest();
                String jsonData = gson.toJson(authenticationRequest,AuthenticationRequest.class);
                System.out.println("sending "+jsonData);
                String result = restRequest.request(url,jsonData);
                if(result!=null){
                    AuthenticationResponse res = gson.fromJson(result,AuthenticationResponse.class);
                    token = res.getToken();
                    receiver.onDataRecieved(result);
                }
                else {
                    receiver.onError();
                }
            }).start();
    }

    @Override
    public void signup(AccountRegisterFrom form, Communicator.DataReceiverCallback receiver) {
        new Thread(() ->{
            String url = Constant.MAIN_SERVER_URL+"auth/register";
            RestRequest restRequest = new RestRequest();
            String jsonData = gson.toJson(form,AccountRegisterFrom.class);
            System.out.println("sending "+jsonData);
            String result = restRequest.request(url,jsonData);
            if(result!=null){
                //AccountRegisterResponse res = gson.fromJson(result,AccountRegisterResponse.class);
                receiver.onDataRecieved(result);
            }
            else {
                receiver.onError();
            }
        }).start();
    }

    @Override
    public void getRestaurants( Communicator.DataReceiverCallback receiver){
        new Thread(() ->{
            String url = Constant.MAIN_SERVER_URL+"restaurants";
            RestRequest restRequest = new RestRequest();
            String result = restRequest.sendRequest(url,"GET",
                  RestRequest.Hearder.Content_Type.value,"application/json",
                    RestRequest.Hearder.Authorization.value,MangoApplication.current_token
            //        RestRequest.Hearder.Restaurant_id.value,MangoApplication.current_restaurant.getId()
            );
            if(result!=null){
                //AccountRegisterResponse res = gson.fromJson(result,AccountRegisterResponse.class);
                //token = res.userId;
                receiver.onDataRecieved(result);
            }
            else {
                receiver.onError();
            }
        }).start();
    }
    @Override
    public void authenticate(final String token, final Communicator.DataReceiverCallback receiver) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url= new URL(Constant.MAIN_SERVER_URL+"auth/me");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", MangoApplication.current_token);
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    StringBuilder builder = new StringBuilder();
                    String output;
                    while ((output=br.readLine())!=null){
                        builder.append(output);
                    }
                    receiver.onDataRecieved(builder.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                    receiver.onError();
                }
        }}
        ).start();
    }

    @Override
    public void createNewRestaurant(final RestaurantRegisterForm form,final Communicator.DataReceiverCallback receiver) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url= new URL(Constant.MAIN_SERVER_URL+"restaurant/register");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", MangoApplication.current_token);
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : "
                                + conn.getResponseCode());
                    }
                    if(conn.getResponseCode() == 403) receiver.onNoAcces();
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    StringBuilder builder = new StringBuilder();
                    String output;
                    while ((output=br.readLine())!=null){
                        builder.append(output);
                    }
                    receiver.onDataRecieved(builder.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                    receiver.onError();
                }
            }}
        ).start();
    }

    @Override
    public String getFoodById(String id) {
        String url = Constant.MAIN_SERVER_URL+"food/get?serial="+id;
        String result=restRequest.sendRequest(url,"GET",
        RestRequest.Hearder.Content_Type.value,"application/json",
                RestRequest.Hearder.Authorization.value,MangoApplication.current_token,
                RestRequest.Hearder.Restaurant_id.value,MangoApplication.current_restaurant.getId()
        );
        if(result!=null)return result;
        else
            return null;
    }
/*
    @Override
    public List<Food> getFoods(Integer page, Integer pageSize, RestRequest.DataCallback callback) {
        String url = "http://localhost:12001/food/all?page="+page+"&pageSize="+pageSize;
        String result = restRequest.sendRequest(url);
        Type listType = new TypeToken<List<Food>>(){}.getType();
        return new Gson().fromJson(result,listType);
    }
*/
    @Override
    public void getFoods(Integer page, Integer pageSize, Communicator.DataReceiverCallback callback) {
        String url = Constant.MAIN_SERVER_URL+"food/all?page="+page+"&pageSize="+pageSize;
        System.out.println("Sending "+url);
        restRequest.getData(url,callback);
    }


    @Override
    public List<String> getAllFood() {
        return null;
    }

    @Override
    public void updateFood(final Food food,final Communicator.DataReceiverCallback cb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constant.MAIN_SERVER_URL+"food/update";
                RestRequest restRequest = new RestRequest();
                String jsonData = new Gson().toJson(food,Food.class);
                System.out.println("sending "+jsonData);
                String result = restRequest.request(url,jsonData);
                if(result!=null)cb.onDataRecieved(result);
                else cb.onError();
            }
        }).start();
    }

    @Override
    public void sendOrder(final OrderForm orderForm,final Communicator.DataReceiverCallback  dataCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Constant.MAIN_SERVER_URL+"order/newOrder";
                RestRequest restRequest = new RestRequest();
                String jsonData = new Gson().toJson(orderForm,OrderForm.class);
                System.out.println("sending new order: "+jsonData);
                String result = restRequest.request(url,jsonData);
                if(result!=null)dataCallback.onDataRecieved(result);
                else dataCallback.onError();
            }
        }).start();
    }

    @Override
    public void getOrderToday(final Integer page, final Integer pageSize, final Order.OrderState orderState, final Communicator.DataReceiverCallback  dataCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = builder.getOrderUrl(page,pageSize,orderState,null);
                RestRequest restRequest = new RestRequest();
                String result = restRequest.sendRequest(url,"GET",
                        RestRequest.Hearder.Content_Type.value,"application/json",
                        RestRequest.Hearder.Authorization.value,MangoApplication.current_token,
                        RestRequest.Hearder.Restaurant_id.value,MangoApplication.current_restaurant.getId()
                );
                if(result!=null)dataCallback.onDataRecieved(result);
                else dataCallback.onError();
            }
        }).start();
    }
}
