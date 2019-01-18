package lam.fooapp.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import lam.fooapp.Utils.Constant;
import lam.fooapp.communication.rests.RequestBuilder;
import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;


public class SpringFoodApi implements FoodApi {
    private RestRequest restRequest;
    private RequestBuilder builder;
    public SpringFoodApi(){
        builder=new RequestBuilder();
        restRequest = new RestRequest();
    }
    @Override
    public String getFoodById(String id) {
        String url = Constant.MAIN_SERVER_URL+"food/get?serial="+id;
        String result=restRequest.sendRequest(url);
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
    public void getFoods(Integer page, Integer pageSize, RestRequest.DataCallback callback) {
        String url = Constant.MAIN_SERVER_URL+"food/all?page="+page+"&pageSize="+pageSize;
        System.out.println("Sending "+url);
        restRequest.getData(url,callback);
    }


    @Override
    public List<String> getAllFood() {
        return null;
    }

    @Override
    public String updateFood(String food) {
        return null;
    }

    @Override
    public void sendOrder(final OrderForm orderForm,final RestRequest.DataCallback dataCallback) {
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
    public void getOrderToday(final RestRequest.DataCallback dataCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = builder.getOrderUrl(0,10,null,null);
                RestRequest restRequest = new RestRequest();
                String result = restRequest.sendRequest(url);
                if(result!=null)dataCallback.onDataRecieved(result);
                else dataCallback.onError();
            }
        }).start();
    }
}
