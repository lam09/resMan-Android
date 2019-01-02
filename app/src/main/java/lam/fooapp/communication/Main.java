package lam.fooapp.communication;


import java.util.Random;

import lam.fooapp.communication.rests.RestRequest;

import static java.lang.Thread.sleep;

public class Main  {

    public static void main(String[] args) {
      /*  Communicator communicator = new Communicator();
        communicator.initSocketIO();
        communicator.initClient();
        */
      MediaDownloader mediaDownloader=new MediaDownloader();
      mediaDownloader.downloadImage("http://www.yahoo.com/image_to_read.jpg");
      RestRequest request=new RestRequest();
        for(int i=0;i<1;i++) {
            try {
                sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Integer x = i;
            new Thread(new Runnable() {
                public void run() {
                    System.out.println("client ");
                    Communicator communicator = new Communicator(x.toString());
                    if(!communicator.initSocketIO()) {
                        System.out.println("can not create new socket");
                        return;
                    }
                    communicator.initClient();
                    Random rand= new Random();
                    while (true) {
                        String event="event1";//+ (rand.nextInt(2)+1);
                        System.out.println("Client " + communicator.clientId+" send " + event);
                        communicator.sendEvent(event);
//                        communicator.socketio.emit(event,Utils.toJson(new EventData("0",event)));
                      //    communicator.socketio.emit(event,new EventData(communicator.clientId,event));
                        //                       // System.out.println("Client " + communicator.clientId);
                        try {
                            sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //break;
                    }
                }
            }).start();
        }
//        request.sendRequest("http://localhost:12001/admin/getMenu");
    }
}
