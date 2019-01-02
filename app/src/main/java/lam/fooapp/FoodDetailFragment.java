package lam.fooapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;

import lam.fooapp.model.Food;

public class FoodDetailFragment extends Fragment {
    Food food;
    public FoodDetailFragment()
    {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText editText = (EditText)view.findViewById(R.id.editTextFoodName);
        editText.setText(food.getTitle());
        editText = (EditText)view.findViewById(R.id.editTextFoodPrice);
        editText.setText(food.getPrice());
        editText = (EditText)view.findViewById(R.id.editTextFoodDescription);
        editText.setText(food.getDescription());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String strtext = getArguments().getString("currentFood");
        food=new Gson().fromJson(strtext,Food.class);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
