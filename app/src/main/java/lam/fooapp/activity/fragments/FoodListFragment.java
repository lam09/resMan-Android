package lam.fooapp.activity.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.activity.DoneOnEditorActionListener;
import lam.fooapp.activity.EndlessRecyclerViewScrollListener;
import lam.fooapp.activity.order.CustomOrderDialog;
import lam.fooapp.activity.order.FoodAdapter;
import lam.fooapp.activity.order.OrderListActivity;
import lam.fooapp.activity.order.OrderListListenner;
import lam.fooapp.activity.order.OrderedFoodAdapter;
import lam.fooapp.activity.order.TableEditDialog;
import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;

public class FoodListFragment extends Fragment implements OrderListListenner {

    private EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView foodListView;
    private RecyclerView.Adapter foodListAdapter;
    ArrayList<Food> foodList = new ArrayList<Food>();
    OnFoodSelect selector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.content_order, container, false);
    }

    public void updateItem(Food food){
        foodListAdapter.notifyItemChanged(foodList.indexOf(food),food);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MangoApplication.communicator.foodApi.getFoods(0,5,onFoodDataCallback);
        foodListAdapter = new FoodAdapter(view.getContext(), foodList,this);
        foodListView = (RecyclerView) view.findViewById(R.id.orderFoodMenuView);
        foodListView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        foodListView.setAdapter(foodListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        foodListView.setLayoutManager(linearLayoutManager);
        scrollListener=new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        foodListView.addOnScrollListener(scrollListener);
    }

    public void loadNextDataFromApi(Integer offset){
        System.out.println("load next data from Api " + offset);
        MangoApplication.communicator.foodApi.getFoods(offset,5,onFoodDataCallback);
    }



    RestRequest.DataCallback<List<Food>> onFoodDataCallback = new RestRequest.DataCallback<List<Food>>()  {
        @Override
        public void onDataRecieved(final String result) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Type listType = new TypeToken<List<Food>>(){}.getType();
                    List<Food>foodsReceived = (List<Food>)new Gson().fromJson(result,listType);
                    foodList.addAll(foodsReceived);
                    foodListAdapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public void onError() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

        }
    };

    @Override
    public void addFoodToOrderList(Food food) {
        selector.onFoodSelect(food);
    }

    @Override
    public void removeFoodToOrderList(Food food) {

    }

    public void setFoodSelector(OnFoodSelect selector){
        this.selector=selector;
    }

    public interface OnFoodSelect{
        void onFoodSelect(Food food);
    }
    public interface OnFilterOrder{
        void onFilterSelected(Order.OrderState orderState);
    }
}
