package lam.fooapp.activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.Utils.Utils;
import lam.fooapp.communication.Communicator;
import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Food;

public class FoodFragment extends Fragment {
    Food food;

    TextView name,price,description;
    Button button;
    public FoodFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_detail,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.print("on destroy view fragment");
    }

    public void setData(final Food food){
        this.food = food;
        name = (TextView) getActivity().findViewById(R.id.editTextFoodName);
        price = (TextView) getActivity().findViewById(R.id.editTextFoodPrice);
         description = (TextView) getActivity().findViewById(R.id.editTextFoodDescription);

         name.setText(food.getTitle());
         price.setText(food.getPrice());
         description.setText(food.getDescription());

        button = (Button) getActivity().findViewById(R.id.buttonFoodUpdate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food foodNew = food;
                foodNew.setTitle(name.getText().toString());
                foodNew.setPrice(price.getText().toString());
                foodNew.setDescription(description.getText().toString());
                name.clearFocus();price.clearFocus();description.clearFocus();
                MangoApplication.communicator.updateFood(foodNew,cb);
            }
        });
    }
    Communicator.DataReceiverCallback cb = new Communicator.DataReceiverCallback() {
        @Override
        public void onDataRecieved(String result) {
            Food received = Utils.gson.fromJson(result,Food.class);
            food.setTitle(received.getTitle());
            food.setPrice(received.getPrice());
            food.setDescription(received.getDescription());
            Snackbar.make(getView(), "Food is updated", Snackbar.LENGTH_LONG)
                   .setAction("Action", null).show();

        }

        @Override
        public void onError() {
            Snackbar.make(getView(), "Wrong!!! Food is not updated", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };
}
