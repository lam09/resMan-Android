package lam.fooapp;

import android.app.Application;

import lam.fooapp.communication.Communicator;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;

public class MangoApplication extends Application {


    public static Communicator communicator;// = new Communicator();

    public static OrderForm currentOrderForm = null;


    @Override
    public void onCreate() {
        System.out.println("Starting create App");
        communicator=new Communicator();
       /* if(!communicator.initSocketIO()) {
            System.out.println("can not create new socket");
            return;
        }*/
        super.onCreate();
        System.out.println("App created");
    }
}
