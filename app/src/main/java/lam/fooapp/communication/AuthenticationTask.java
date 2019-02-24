package lam.fooapp.communication;
/*
import android.media.session.MediaSession;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.TimerTask;

import lam.fooapp.Utils.Constant;
import lam.fooapp.model.AuthenticationRequest;
*/
public class AuthenticationTask  {
  /*  OnAuthenticationListener listenner;
    String token;
    public AuthenticationTask(String token, OnAuthenticationListener listenner){
        this.listenner = listenner;
        this.token=token;
    }

    @Override
    public void run() {
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                .setUri(Constant.MAIN_SERVER_URL+"auth/me")
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader("Authorization",token)
                .build();
        try {
           HttpResponse res = client.execute(request);
            res.getEntity().getContent();
        }catch (IOException e){

        }
    }
    public interface OnAuthenticationListener{
        void onSuccess(String username);
        void onError();
    }*/
}
