package lam.fooapp.communication;


import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import lam.fooapp.Utils.Constant;
import lam.fooapp.Utils.Utils;
import lam.fooapp.communication.socketio.CustomListener;
import lam.fooapp.model.EventData;


import java.net.URISyntaxException;
import java.util.UUID;

public class Communicator {
    public Socket socketio;
    public FoodApi foodApi;
    Manager socketManager;
    boolean connected=false;
    public String clientId;
    public Communicator()
    {
        clientId= UUID.randomUUID().toString();
        initSocketIO();
        foodApi = new SpringFoodApi();
    }
    public Communicator(String id)
    {
        clientId= id;
    }
    public boolean initSocketIO() {
        try {
            IO.Options opt=new IO.Options();
            opt.forceNew=true;
            opt.reconnection=true;
            opt.reconnectionDelay=1000;
            opt.reconnectionDelayMax=5000;
            opt.reconnectionAttempts=9999999;
            socketio = IO.socket(Constant.SERVER_URL,opt);
            System.out.println("connect to " + Constant.SERVER_URL);
            socketManager=socketio.io();
            if (socketio != null) {
                System.out.println("created socket io");
                socketio.on(Socket.EVENT_CONNECT, handleOnNewConnectionCreated);
                socketio.on(Socket.EVENT_CONNECT_ERROR, handleOnConnectionError);
                socketio.on(Socket.EVENT_DISCONNECT, handleOnDisconnection);
                socketio.connect();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
       // socketManager.reconnectionAttempts();
        return true;
    }


    CustomListener<EventData> handleOnNewConnectionCreated = new CustomListener<EventData>(EventData.class) {
        @Override
        public void call(Object... args) {
           // System
            System.out.println("client connected");
        }
    };
    Listener handleOnConnectionError = new Listener() {
        public void call(Object... objects) {

        }
    };
    Listener handleOnDisconnection = new Listener() {
        public void call(Object... objects) {
            System.out.println("client disconnected");
        }
    };
    Listener handleOnNewClientInit = new Listener() {
        public void call(Object... objects) {
            System.out.println(objects.length + "handleOnNewClientInit client "+clientId+" recieved :"+objects[0].toString());
            Ack ack = (Ack) objects[objects.length - 1];
            ack.call("success");
        }
    };

    Listener handleOnNewEvent1 = new Listener() {
        public void call(Object... objects) {
            EventData e = Utils.fromJson(objects[0].toString(),EventData.class);
            long latency= System.currentTimeMillis()-e.timeStamp;
            System.out.println("client "+clientId+" recieved :"+objects[0].toString() + " latency "+latency);
        }
    };

    public void initClient()
    {
        System.out.println("client initialize "+clientId);
        socketio.emit("init",Utils.toJson(new EventData(clientId,"login")));
      /*  socketio.emit("init", new EventData(clientId, "init"), new Ack() {
            public void call(Object... args) {
                System.out.println(args[0].toString());
            }
        });
*/    }
    public void sendEvent(String event)
    {
       // socketio.emit(event, Utils.toJson(new EventData(clientId, "new-client")).toString());
        socketio.emit(event, Utils.toJson(new EventData(clientId, "new-client")).toString(), new Ack() {
            public void call(Object... args) {
                System.out.println(System.currentTimeMillis()+" event success sent" + args[0].toString());
            }
        });
    }
    public void registerEventListener(String eventName, Listener listener)
    {
        socketio.on(eventName,listener);
    }

}
