package lam.fooapp.activity.order;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.activity.BasicMangoActivity;
import lam.fooapp.activity.EndlessRecyclerViewScrollListener;
import lam.fooapp.activity.WaiterActivity;
import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Order;

public class OrderListActivity extends BasicMangoActivity {
    RecyclerView orderListView;
    RecyclerView.Adapter orderListAdapter;
    EndlessRecyclerViewScrollListener scrollListener;
    ArrayList<Order>orders = new ArrayList<>();
    Order.OrderState orderStateFilter=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        MangoApplication.communicator.foodApi.getOrderToday(0,5,orderStateFilter,onOrderListReceived);
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
        orderListAdapter = new OrderViewAdapter(getApplicationContext(), orders, new OrderViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View v, Order order) {
               v.setVisibility(View.VISIBLE);
           }
        });
        orderListView = (RecyclerView) findViewById(R.id.activity_order_list);
        orderListView.setAdapter(orderListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        orderListView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        orderListView.addOnScrollListener(scrollListener);
        initOrderStateButtons();
    }

    private void initOrderStateButtons() {
        LayoutParams params =
                new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.MATCH_PARENT);
        LinearLayout view =(LinearLayout) findViewById(R.id.order_type_list_button);
        Order.OrderState states[]=Order.OrderState.values();
        for (Order.OrderState o: states){
            Button btn = new Button(this);
            btn.setText(o.toString());
            btn.setLayoutParams(params);
            //final int sdk = android.os.Build.VERSION.SDK_INT;
          //  if(sdk >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.order_state_button));
            //}
            //btn.setBackgroundColor(MangoApplication.getOrderStateColor(o));
            btn.setTextColor(MangoApplication.getOrderStateColor(o));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            view.addView(btn);

        }
    }
    private void loadNextDataFromApi(int page) {
        MangoApplication.communicator.foodApi.getOrderToday(page,5,orderStateFilter,onOrderListReceived);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,WaiterActivity.class);
        startActivity(intent);

        super.onBackPressed();
    }
}
