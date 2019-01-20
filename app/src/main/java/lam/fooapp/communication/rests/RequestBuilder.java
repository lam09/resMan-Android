package lam.fooapp.communication.rests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import lam.fooapp.Utils.Constant;
import lam.fooapp.model.Order;

public class RequestBuilder {
    StringBuilder builder;

    public String getFoodBySerialUrl(Integer serial)
    {
        builder = new StringBuilder();
        builder.append(Constant.MAIN_SERVER_URL);
        builder.append("food/get");
        builder.append("?serial="+serial);
        return builder.toString();
    }

    public String getOrderUrl(Integer page, Integer pageSize, Order.OrderState state, String date){
        builder = new StringBuilder();
        builder.append(Constant.MAIN_SERVER_URL);
        builder.append("order/get");
        builder.append("?page="+page+"&pageSize="+pageSize);
        if(state!=null) builder.append("&state="+state);
        if(date!=null)builder.append("&date="+date);
        return builder.toString();
    }

    public String getActiveOrders(Integer page,Integer pageSize)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        return getOrderUrl(page,pageSize,Order.OrderState.WAITING,dateFormat.format(date));
    }
}
