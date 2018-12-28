package lam.fooapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import io.socket.emitter.Emitter;
import lam.fooapp.Utils.Utils;
import lam.fooapp.communication.model.EventData;

public class MainActivity extends AppCompatActivity implements Emitter.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //MangoApplication.communicator.socketio.on("new-food",this);
    }

    public void addButtonToScrollView()
    {
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.foodLinearView);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Button button=new Button(this);
        button.setText("new food");
        linearLayout.addView(button);

    }

    @Override
    public void call(Object... args) {
        EventData e = Utils.fromJson(args[0].toString(),EventData.class);
        long latency= System.currentTimeMillis()-e.timeStamp;
        System.out.println("client "+MangoApplication.communicator.clientId+" recieved :"+args[0].toString() + " latency "+latency);
        addButtonToScrollView();
    }
}
