package lam.fooapp.communication.rests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.apache.http.HttpHeaders;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import lam.fooapp.MangoApplication;
import lam.fooapp.Utils.Constant;
import lam.fooapp.communication.Communicator;
import lam.fooapp.communication.FoodApi;
import lam.fooapp.communication.SpringFoodApi;
import lam.fooapp.model.Food;

public class RestRequest{

    public enum Hearder{Content_Type("Content-type"),
                Authorization("Authorization"),
                Restaurant_id("restaurant-id")
        ;
        public String value;
        Hearder(String s) {
            this.value=s;
        }
    }


    public RestRequest (){

    }
/*
    public interface DataCallback<DataType>{
        void onDataRecieved(String result);
        void onError();
    }
*/
    public void getData(final String url,final Communicator.DataReceiverCallback dataCallback)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestRequest restRequest = new RestRequest();
                String result = restRequest.sendRequest(url,"GET",
                        Hearder.Content_Type.value,"application/json",
                        Hearder.Authorization.value,MangoApplication.current_token,
                        Hearder.Restaurant_id.value,MangoApplication.current_restaurant.getId()
                        );
                if(result!=null)dataCallback.onDataRecieved(result);
                else dataCallback.onError();
            }
        }).start();
    }




    public String sendRequest(String requestUrl,String method,String... headers){
        try{
            System.out.println( "send url:"+requestUrl);
            URL url= new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            if(headers.length>0)
            for(int i=0;i<headers.length;i+=2)
                if(headers[i+1]!=null)conn.setRequestProperty(headers[i],headers[i+1]);
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
    public String request(String url,String jsonData) {
       try {
           URL requestUrl=new URL(url);
           HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
           conn.setRequestMethod("POST");
           conn.setRequestProperty("Content-Type","application/json");
           conn.setDoOutput(true);
           conn.setDoInput(true);
           OutputStream outputStream = conn.getOutputStream();
           OutputStreamWriter writer = new OutputStreamWriter(outputStream,"UTF-8");
           writer.write(jsonData);
           writer.flush();
           writer.close();
           outputStream.close();

           String result;
           BufferedInputStream inputStream = new BufferedInputStream(conn.getInputStream());
           ByteArrayOutputStream buffer = new ByteArrayOutputStream();
           int result2 = inputStream.read();
           while (result2!=-1){
               buffer.write((byte)result2);
               result2=inputStream.read();
           }
           result=buffer.toString();
           return result;
       }catch (IOException e)
       {
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
