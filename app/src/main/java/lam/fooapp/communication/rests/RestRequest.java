package lam.fooapp.communication.rests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import lam.fooapp.communication.FoodApi;
import lam.fooapp.communication.SpringFoodApi;
import lam.fooapp.model.Food;

public class RestRequest{

    public RestRequest (){

    }

    public interface DataCallback<DataType>{
        void onDataRecieved(String result);
        void onError();
    }

    public void getData(final String url,final DataCallback dataCallback)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestRequest restRequest = new RestRequest();
                String result = restRequest.sendRequest(url);
                if(result!=null)dataCallback.onDataRecieved(result);
                else dataCallback.onError();
            }
        }).start();
    }

    public String sendRequest(String requestUrl){
        try{
            URL url= new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
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
            return builder.toString();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    //TEST
    public static void main(String[] args){
        FoodApi foodApi = new SpringFoodApi();
        Long start = System.currentTimeMillis();
        //String s = foodApi.getFoodById("1");
        RestRequest restRequest = new RestRequest();
     /*   restRequest.getData("http://localhost:12001/food/all?page=0&pageSize=3", new DataCallback<List<Food>>() {
            @Override
            public void onDataRecieved(String result) {
                Type listType = new TypeToken<List<Food>>(){}.getType();
                new Gson().fromJson(result,listType);
            }
        });*/
      //  for(Food s1:foodApi.getFoods(1,2))
      //  System.out.println(s1.getSerial());
        Long latency=System.currentTimeMillis()-start;
       System.out.println("Latency: "+latency);
   //     RestReqestParam param=new RestReqestParam("serial","1");
    }

}
