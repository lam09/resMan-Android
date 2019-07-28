package lam.fooapp.activity.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.Utils.Utils;
import lam.fooapp.activity.BasicMangoActivity;
import lam.fooapp.communication.AuthenticationTask;
import lam.fooapp.communication.Communicator;
import lam.fooapp.model.Account;
import lam.fooapp.model.AuthenticationResponse;
import lam.fooapp.model.Restaurant;


public class MainActivity extends BasicMangoActivity  implements Communicator.DataReceiverCallback{


    private DrawerLayout mDrawerLayout;

    Button createRestaurant,getListRestaurant;
    Spinner res_dropdown;
    String username = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        setContentLayout(R.layout.activity_main);



        String defaultValue = null;
        String token = MangoApplication.sharedPref.getString(getString(R.string.saved_token), defaultValue);

        if(MangoApplication.current_username==null){
            MangoApplication.communicator.authenticateByToken(token,this);
        } else {
            MangoApplication.communicator.getRestaurants(onListRestaurantCb);
        }
        createRestaurant = (Button)findViewById(R.id.create_restaurant_btn);
        getListRestaurant = (Button) findViewById(R.id.restaurant_list_btn);
        createRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        registerRestaurant();
                    }
                });
            }
        });
    }

    private void registerRestaurant(){
        Intent intent = new Intent(this,RegisterRestaurantActivity.class);
        startActivity(intent);
    }
    private void editRestaurant(){
        Intent intent = new Intent(this,RegisterRestaurantActivity.class);
        startActivity(intent);
    }
    private void setDropDownList(ArrayList<String> items){
        System.out.println("received list restaurants "+ items.size());
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        MangoApplication.current_restaurant = MangoApplication.restaurants.get(0);
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position);
                MangoApplication.current_restaurant=MangoApplication.restaurants.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    Communicator.DataReceiverCallback onListRestaurantCb = new Communicator.DataReceiverCallback() {
        @Override
        public void onDataRecieved(String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("received list restaurants "+ result);
                    Type type = new TypeToken<ArrayList<Restaurant>>(){}.getType();
                    ArrayList<Restaurant> restaurants = new Gson().fromJson(result,type);
                    ArrayList<String> items = new ArrayList<>();
                    for(int i = 0;i<restaurants.size();i++) items.add(restaurants.get(i).getName());
                    MangoApplication.restaurants = restaurants;

                    setDropDownList(items);
                }
            });
        }

        @Override
        public void onError() {

        }
    };

    @Override
    public void onDataRecieved(String result) {
        System.out.print(result);
        MangoApplication.current_username = Utils.fromJson(result, AuthenticationResponse.class).getUsername();
    }



    @Override
    public void onError() {
        Intent intent = new Intent(this, LoginActivity.class);
//        Intent intent = new Intent(this, TestGoogleLoginActivity.class);
        startActivity(intent);
    }
}
