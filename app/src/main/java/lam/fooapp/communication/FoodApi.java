package lam.fooapp.communication;

import java.util.List;

import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Food;

public interface FoodApi {
    String getFoodById(String id);
//    List<Food> getFoods(Integer page, Integer pageSize, RestRequest.DataCallback dataCallback);
    public void getFoods(Integer page, Integer pageSize, RestRequest.DataCallback dataCallback);

    List<String> getAllFood();
    String updateFood(String food);
}
