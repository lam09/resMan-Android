package lam.fooapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import lam.fooapp.Utils.Utils;
import lam.fooapp.communication.model.EventData;

public class MainActivity extends AppCompatActivity {


    RecyclerView mRecyclerView;
//  RecyclerViewAdapter mRcvAdapter;
    RecyclerView.Adapter mRcvAdapter;
    List<String> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    @Override
    protected void onStart() {
        super.onStart();
 //       MangoApplication.communicator.socketio.on("new-food",this);
    }
/*
    private void addFood(String newFoodName) {
        data.add(newFoodName);
        mRcvAdapter.notifyItemInserted(data.size() - 1);
        scrollToBottom();
    }
    private void scrollToBottom() {
        mRecyclerView.scrollToPosition(mRcvAdapter.getItemCount() - 1);
    }
    @Override
    public void call(Object... args) {
        EventData e = Utils.fromJson(args[0].toString(),EventData.class);
        final Long latency= System.currentTimeMillis()-e.timeStamp;
        System.out.println("client "+MangoApplication.communicator.clientId+" recieved :"+args[0].toString() + " latency "+latency);
        final String data=args[0].toString();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addFood(data);
            }
        });
    }*/
}
