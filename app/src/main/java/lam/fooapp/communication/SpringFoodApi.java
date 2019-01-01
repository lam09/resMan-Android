package lam.fooapp.communication;

import java.util.List;

import lam.fooapp.communication.FoodApi;

public class SpringFoodApi implements FoodApi {
    @Override
    public String getFoodById(String id) {
        return null;
    }

    @Override
    public List<String> getFoods(Integer offset, Integer limit) {
        return null;
    }

    @Override
    public List<String> getAllFood() {
        return null;
    }

    @Override
    public String updateFood(String food) {
        return null;
    }
}
