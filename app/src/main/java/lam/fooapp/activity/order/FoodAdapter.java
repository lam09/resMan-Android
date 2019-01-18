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

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    OrderListListenner orderListListenner;
    protected List<Food> mFoods;
    private int[] mUsernameColors;
    public FoodAdapter(Context context, List<Food> foods,OrderListListenner orderListListenner) {
        mFoods = foods;
        mUsernameColors = context.getResources().getIntArray(R.array.food_colors);
        this.orderListListenner=orderListListenner;
    }
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_food, viewGroup, false);
        return new FoodViewHolder(v,orderListListenner);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int i) {
        Food food = mFoods.get(i);
//        viewHolder.setFoodName(food.getTitle());
  //      viewHolder.setFoodSerial(food.getSerial().toString());
        viewHolder.setFood(food);
     //   viewHolder.setFoodInfo(food);

    }

    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView foodNameView,foodSerialView,foodPriceView,foodTypeView;
        private OrderListListenner orderListListenner;
        private Food food;
        public FoodViewHolder(View itemView,OrderListListenner orderListListenner) {
            super(itemView);
            this.orderListListenner = orderListListenner;
            foodNameView = (TextView) itemView.findViewById(R.id.foodName);
            foodSerialView = (TextView) itemView.findViewById(R.id.foodSerial);
            foodPriceView = (TextView) itemView.findViewById(R.id.foodPrice);
            foodTypeView = (TextView) itemView.findViewById(R.id.foodType);
        }
        public void setFood(final Food foodd){
            this.food=foodd;
            setFoodInfo(this.food);
            Button addToOrderBtn = (Button) itemView.findViewById(R.id.addFoodToOrder);
            addToOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderListListenner.addFoodToOrderList(food);
                }
            });

        }
        public void setFoodInfo(Food food){
            if (null == foodNameView) return;
            foodNameView.setText(food.getTitle());
            foodNameView.setTextColor(getUsernameColor(food.getTitle()));
            if (null == foodSerialView) return;
            foodSerialView.setText(food.getSerial().toString());
            foodSerialView.setTextColor(getUsernameColor(food.getSerial().toString()));
            if (null == foodPriceView) return;
            foodPriceView.setText(food.getPrice());
            foodPriceView.setTextColor(getUsernameColor(food.getPrice()));
            if (null == foodTypeView) return;
            foodTypeView.setText(food.getType());
            foodTypeView.setTextColor(getUsernameColor(food.getType()));

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
            foodNameView.setTextColor(getUsernameColor(username));
        }

        public void setFoodSerial(String serial)
        {
            if (null == foodNameView) return;
            foodSerialView.setText(serial);
            foodSerialView.setTextColor(getUsernameColor(serial));
        }
        private int getUsernameColor(String username) {
            int hash = 7;
            if(username==null) return mUsernameColors[0];
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }
}
