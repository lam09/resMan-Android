package lam.fooapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lam.fooapp.activity.order.OrderListActivity;

public class NewOrderBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.print("open order list");
        Intent openOrderIntent =new Intent(context,OrderListActivity.class);
        openOrderIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openOrderIntent);
    }
}
