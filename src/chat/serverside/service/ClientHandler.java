package chat.serverside.service;

import chat.serverside.service.myThreads.AuthCallable;
import chat.serverside.service.myThreads.MyTimer;
import chat.serverside.service.myThreads.ReadMessage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ClientHandler {

    private MyServer myServer;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String name;
    ExecutorService auth = Executors.newFixedThreadPool(2);
    ExecutorService readMessage =Executors.newSingleThreadExecutor();

    public ClientHandler(MyServer myServer, Socket socket) {
        try {

            this.myServer = myServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            dos.writeUTF("Вы не авторизованы\nДля авторизации у вас есть 2 минуты");

            Future<String> authentication = auth.submit(new AuthCallable(myServer, dis, this));
            Future timer = auth.submit(new MyTimer());
            if(authentication.get().startsWith("/authok") && !timer.isDone() ) {
                auth.shutdownNow();
                dos.writeUTF("/authok");
                sendMessage("К нам подключился " + name);
                myServer.broadcastMessage("Hello " + name);
                myServer.subscribe(this);
                Future read = readMessage.submit(new ReadMessage(myServer,dis,dos,this));
                read.get();
            }
            else {
                closeConnection();
            }
            return;

        } catch (IOException e) {
            closeConnection();
            throw new RuntimeException("Problem with ClientHandler");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            closeConnection();
        }
    }


    public void sendMessage(String message) {
        try {
            dos.writeUTF(message);
        } catch (IOException ignored) {
        }
    }

    private void closeConnection() {
        myServer.broadcastMessage(name + " Leave chat");
        myServer.unsubscribe(this);

        try {
            dis.close();
            dos.close();
            socket.close();
        } catch (IOException ignored) {
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
