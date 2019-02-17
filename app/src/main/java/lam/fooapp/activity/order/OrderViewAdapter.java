package lam.fooapp.activity.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderItem;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.OrderViewCardHolder> implements OrderActivity.OnFilterOrder {

    ArrayList<Order> orders = new ArrayList<>();
    ArrayList<Order> ordersFiltered = new ArrayList<>();

    @Override
    public void onFilterSelected(Order.OrderState orderState) {
        ordersFiltered.clear();
        for(Order o:orders)
        {
            if(o.getOrderState().compareTo(orderState)==0)
                ordersFiltered.add(o);
        }
        notifyDataSetChanged();
    }


    public OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view,Order order);
    }

    public OrderViewAdapter(Context context, ArrayList<Order> orders,OnItemClickListener onItemClickListener) {
        this.orders = orders;
        this.ordersFiltered=this.orders;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public OrderViewCardHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.order_view_card, viewGroup, false);
//                .inflate(R.layout.order_view_expandablecard, viewGroup, false);
        return new OrderViewAdapter.OrderViewCardHolder(v,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewCardHolder orderViewCardHolder, int i) {
       // Order order = ordersFiltered.get(i);
        Order order = orders.get(i);
        orderViewCardHolder.setOrder(order);

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewCardHolder extends RecyclerView.ViewHolder {

        private Order order;
        TextView tableNo,orderNum;
        List<Food> foods;
        View tableRow;
        RecyclerView.Adapter adapter;
        RecyclerView foodListView;
        OnItemClickListener onItemClickListener;
        public OrderViewCardHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener=onItemClickListener;
            tableNo = (TextView) itemView.findViewById(R.id.order_view_table);
            orderNum = (TextView)itemView.findViewById(R.id.order_num_text_view);
            tableRow=itemView.findViewById(R.id.order_view_table_row);
        }
        public void setOrder(final Order order)
        {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foodListView.setVisibility(View.VISIBLE);
                    onItemClickListener.onItemClick(v,order);
                }
            });
            //System.out.println("fuck "+new Gson().toJson(order,Order.class));
            if(tableRow!=null) tableRow.setBackgroundColor(MangoApplication.getOrderStateColor(order.getOrderState()));
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
            foodListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("On view onclick");
                }
            });
        }


    }
}
