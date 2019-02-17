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
        //initSocketIO();
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

}
