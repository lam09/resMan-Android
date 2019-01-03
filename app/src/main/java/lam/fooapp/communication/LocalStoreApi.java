package lam.fooapp.communication;

import lam.fooapp.model.Food;

public interface LocalStoreApi {
    Food saveFood(Food food);
    Food getFoodById(String id);
    Food getFoodBySerial(Integer serial);
    Food deleteFoodFromLocal(Food food);
    boolean synchronizeFoodListWithServer();
}
