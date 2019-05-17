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

import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.Utils.Utils;
import lam.fooapp.activity.BasicMangoActivity;
import lam.fooapp.communication.AuthenticationTask;
import lam.fooapp.communication.Communicator;
import lam.fooapp.model.Account;


public class MainActivity extends BasicMangoActivity  implements Communicator.DataReceiverCallback{


    private DrawerLayout mDrawerLayout;


    String username = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        setContentLayout(R.layout.activity_main);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.app_name),Context.MODE_PRIVATE);
        String defaultValue = "";
        String token = sharedPref.getString(getString(R.string.saved_token), defaultValue);

        if(MangoApplication.user_account==null){
            MangoApplication.communicator.authenticateByToken(token,this);
        }
    }



    @Override
    public void onDataRecieved(String result) {
        System.out.print(result);
        MangoApplication.user_account = Utils.fromJson(result, Account.class);
    }

    @Override
    public void onError() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
