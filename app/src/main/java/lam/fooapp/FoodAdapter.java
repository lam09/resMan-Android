package lam.fooapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<String> mMessages;
    private int[] mUsernameColors;
    public FoodAdapter(Context context, List<String> messages) {
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.food_colors);
    }
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_food, viewGroup, false);
        return new FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int i) {
        String message = mMessages.get(i);
        viewHolder.setFoodName(message);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView foodNameView;


        public FoodViewHolder(View itemView) {
            super(itemView);

            foodNameView = (TextView) itemView.findViewById(R.id.foodName);
        }
        public void setFoodName(String username) {
            if (null == foodNameView) return;
            foodNameView.setText(username);
            foodNameView.setTextColor(getUsernameColor(username));
        }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }
    }
}
