package lam.fooapp;

import android.app.Application;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import lam.fooapp.communication.Communicator;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;

public class MangoApplication extends Application {

    public static NumberFormat format = NumberFormat.getInstance();
    public static DateTimeFormatter formatter;// = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static Communicator communicator;// = new Communicator();
    public static OrderForm currentOrderForm = null;
    public static ArrayList<Food> currentSelectedFoods = new ArrayList<>();
    public static ArrayList<Food> foods;
    public static Integer selectedTableNo = 0; //pult id

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
    public static Double parser(String value)
    {
        try {
            Number number = format.parse(value);
            return number.doubleValue();
        }
        catch (Exception e){
            return null;
        }
    }
}
