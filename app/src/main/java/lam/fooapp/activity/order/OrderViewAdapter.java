package lam.fooapp.activity.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lam.fooapp.R;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderItem;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.OrderViewCardHolder> {

    ArrayList<Order> orders = new ArrayList<>();

    public OrderViewAdapter(Context context, ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.order_view_card, viewGroup, false);
        return new OrderViewAdapter.OrderViewCardHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewCardHolder orderViewCardHolder, int i) {
        Order order = orders.get(i);
        orderViewCardHolder.setOrder(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewCardHolder extends RecyclerView.ViewHolder{

        private Order order;
        TextView tableNo,orderNum;
        List<Food> foods;
        RecyclerView.Adapter adapter;
        RecyclerView foodListView;
        public OrderViewCardHolder(@NonNull View itemView) {
            super(itemView);
            tableNo = (TextView) itemView.findViewById(R.id.order_view_table);
            orderNum = (TextView)itemView.findViewById(R.id.order_num_text_view);
        }
        public void setOrder(Order order)
        {
            System.out.println("fuck "+new Gson().toJson(order,Order.class));
            if(tableNo!=null)
            tableNo.setText(order.getTableNo().toString());
            if(orderNum!=null)
                orderNum.setText(order.getOrderNo().toString());
            ArrayList<Food> foods = new ArrayList<>();
            for(OrderItem item:order.getOrder_items())
            {
                Food food = new Food();
                food.setTitle(item.getTitle());
                food.setPrice(item.getPrice());
                food.setSerial(item.getFoodSerial());
                foods.add(food);
            }
            this.foods=foods;
            adapter = new OrderedFoodAdapter(itemView.getContext(), foods,null);
            foodListView = (RecyclerView) itemView.findViewById(R.id.order_foodlist_view);
            foodListView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            foodListView.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            foodListView.setLayoutManager(linearLayoutManager);
        }

    }
}
