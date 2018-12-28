package lam.fooapp;

import android.app.Application;

import lam.fooapp.communication.Communicator;

public class MangoApplication extends Application {

    public static Communicator communicator;// = new Communicator();


    @Override
    public void onCreate() {
        System.out.println("Starting create App");
        communicator=new Communicator();
        if(!communicator.createSocket()) {
            System.out.println("can not create new socket");
            return;
        }
        super.onCreate();
        System.out.print("App created");


    }
}
