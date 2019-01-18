package lam.fooapp.activity.order;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import lam.fooapp.R;
import lam.fooapp.model.Food;

public class OrderedFoodAdapter extends RecyclerView.Adapter<OrderedFoodAdapter.OrderedFoodViewHolder>  {

    OrderListListenner orderListListenner;
    protected List<Food> mFoods;
    private int[] mUsernameColors;
    public OrderedFoodAdapter(Context context, List<Food> foods,OrderListListenner orderListListenner) {
        mFoods = foods;
        mUsernameColors = context.getResources().getIntArray(R.array.food_colors);
        this.orderListListenner=orderListListenner;
    }
    @NonNull
    @Override
    public OrderedFoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_food_selected, viewGroup, false);
        return new OrderedFoodViewHolder(v,orderListListenner);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedFoodViewHolder viewHolder, int i) {
        Food food = mFoods.get(i);
        viewHolder.setFood(food);
    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }


    public class OrderedFoodViewHolder extends RecyclerView.ViewHolder {
        private TextView foodNameView,foodSerialView,foodPriceView,foodTypeView;
        private Button removeBtn;
        private OrderListListenner orderListListenner;
        private Food food;
        public OrderedFoodViewHolder(View itemView, OrderListListenner orderListListenner) {
            super(itemView);
            this.orderListListenner = orderListListenner;
            foodNameView = (TextView) itemView.findViewById(R.id.food_selected_name);
           // foodSerialView = (TextView) itemView.findViewById(R.id.foodSerial);
            foodPriceView = (TextView) itemView.findViewById(R.id.selected_food_price);
            foodTypeView = (TextView) itemView.findViewById(R.id.selected_food_type);
        }
        public void setFood(final Food foodd){
            this.food=foodd;
            setFoodInfo(this.food);
            Button addToOrderBtn = (Button) itemView.findViewById(R.id.food_remove_btn);
            addToOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderListListenner.removeFoodToOrderList(food);
                }
            });

        }
        public void setFoodInfo(Food food){
            if (null == foodNameView) return;
            foodNameView.setText(food.getTitle());
          //  if (null == foodSerialView) return;
           // foodSerialView.setText(food.getSerial().toString());
            if (null == foodPriceView) return;
            foodPriceView.setText(food.getPrice());
            if (null == foodTypeView) return;
            foodTypeView.setText(food.getType());

         /*   foodNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FromActivity.this, ToActivity.class);
                    startActivity(intent);
                }
            });*/
        }
        public void setFoodName(String username) {
            if (null == foodNameView) return;
            foodNameView.setText(username);
        }

        public void setFoodSerial(String serial)
        {
            if (null == foodNameView) return;
            foodSerialView.setText(serial);
        }

    }
}
