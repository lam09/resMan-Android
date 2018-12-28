package lam.fooapp.communication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestRequest {

    public RestRequest (){

    }
    public void sendRequest(String requestUrl){
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
            //String result;
            StringBuilder builder = new StringBuilder();
            String output;
            while ((output=br.readLine())!=null){
                System.out.println(output);
                builder.append(output);
            }
          //  System.out.println(builder.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public Byte[] getMedia(String requestUrl,String mediaType)
    {
        Byte[]result=null;
        return result;
    }

    public static void main(String[] args){
        RestRequest rest = new RestRequest();
        Long start = System.currentTimeMillis();
        rest.sendRequest("http://172.22.86.177:12001/admin/getMenu");
        Long latency=System.currentTimeMillis()-start;
        System.out.println("Latency: "+latency);
    }
}
