package lam.fooapp.activity.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Order;

public class OrderListActivity extends AppCompatActivity {
    RecyclerView orderListView;
    RecyclerView.Adapter orderListAdapter;
    ArrayList<Order>orders = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        MangoApplication.communicator.foodApi.getOrderToday(onOrderListReceived);
    }
    RestRequest.DataCallback<List<Order>> onOrderListReceived = new RestRequest.DataCallback<List<Order>>() {
        @Override
        public void onDataRecieved(final String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("received "+result);
                    Type orderListType = new TypeToken<List<Order>>(){}.getType();
                    orders.addAll((ArrayList<Order>) new Gson().fromJson(result,orderListType));
                    System.out.println(orders.size());

                    orderListAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onError() {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
       /* orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());
        orders.add(new Order());*/
        orderListAdapter = new OrderViewAdapter(getApplicationContext(),orders);
        orderListView = (RecyclerView) findViewById(R.id.activity_order_list);
        orderListView.setAdapter(orderListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        orderListView.setLayoutManager(linearLayoutManager);
    }

}
