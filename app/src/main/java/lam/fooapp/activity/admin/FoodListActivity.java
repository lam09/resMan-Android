package lam.fooapp.activity.admin;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import lam.fooapp.R;
import lam.fooapp.Utils.SlideAnimationUtil;
import lam.fooapp.Utils.Utils;
import lam.fooapp.activity.fragments.FoodFragment;
import lam.fooapp.activity.fragments.FoodListFragment;
import lam.fooapp.model.Food;

public class FoodListActivity extends AppCompatActivity implements FoodListFragment.OnFoodSelect {

    FoodListFragment foodListFragment;
    FoodFragment foodFragment;
    boolean foodFragmentActive = false;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        foodListFragment = (FoodListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentFoodList);
        foodListFragment.setFoodSelector(this);
        foodFragment = (FoodFragment) getSupportFragmentManager().findFragmentById(R.id.foodFragment);
        ctx = getApplicationContext();
    }


    @Override
    public void onFoodSelect(Food food) {
        Bundle bundle = new Bundle();
        bundle.putString("food",Utils.gson.toJson(food,Food.class));
        foodFragment.setData(food);
        foodFragmentActive = true;
        View v= findViewById(R.id.containerList);
        View v1= findViewById(R.id.containerFood);
        v1.setVisibility(View.VISIBLE);
        SlideAnimationUtil.slideInFromRight(ctx,v1);
        SlideAnimationUtil.slideOutToLeft(ctx,v);
        v.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        if(foodFragmentActive){
            foodFragmentActive = false;
            View v= findViewById(R.id.containerList);
            View v1= findViewById(R.id.containerFood);
            v1.setVisibility(View.GONE);
            v.setVisibility(View.VISIBLE);
            SlideAnimationUtil.slideInFromLeft(ctx,v);
            SlideAnimationUtil.slideOutToRight(ctx,v1);
        }
        else
        super.onBackPressed();
    }
}
