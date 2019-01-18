package lam.fooapp.activity.order;

import lam.fooapp.model.Food;
import lam.fooapp.model.Order;

public interface OrderListListenner {
    void addFoodToOrderList(Food food);
    void removeFoodToOrderList(Food food);
}
