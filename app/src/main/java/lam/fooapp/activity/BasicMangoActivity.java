package lam.fooapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import lam.fooapp.R;
import lam.fooapp.activity.admin.FoodListActivity;
import lam.fooapp.activity.admin.LoginActivity;
import lam.fooapp.activity.admin.MainActivity;

public class BasicMangoActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        NavigationView navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        int id = menuItem.getItemId();
                        if (id == R.id.nav_home) {
                            Intent intent= new Intent(getApplication(), MainActivity.class);
                            startActivity(intent);
                            return true;
                        }
                        if (id == R.id.nav_waiter) {
                            Intent intent= new Intent(getApplication(), WaiterActivity.class);
                            startActivity(intent);
                            return true;
                        }
                        if (id == R.id.nav_manage) {
                            Intent intent= new Intent(getApplication(), FoodListActivity.class);
                            startActivity(intent);
                            return true;
                        }
                        if (id == R.id.nav_signin) {
                            Intent intent= new Intent(getApplication(), LoginActivity.class);
                            startActivity(intent);
                            return true;
                        }                        // For example, swap UI fragments here

                        return true;
                    }
                });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.drawer_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        System.out.print("Item clicked" + item.getItemId());
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void setContentLayout(@LayoutRes int layout){
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layout,frameLayout);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExit();
    }
}
