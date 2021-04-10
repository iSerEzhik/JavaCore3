package chat.serverside.service.myThreads;

import chat.serverside.service.ClientHandler;
import chat.serverside.service.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReadMessage implements Runnable {


    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final MyServer myServer;
    private String name;
    private final ClientHandler handler;

    @Override
    public void run() {
        while (true) {
            String messageFromClient = null;
            try {
                messageFromClient = dis.readUTF();
                System.out.println(name + " send message " + messageFromClient);
            if (messageFromClient.trim().startsWith("/")) {

                if (messageFromClient.startsWith("/pm")) {
                    String[] arr = messageFromClient.split(" ", 3);
                    myServer.sendMessageToCertainClient(handler, arr[1], name + ": " + arr[2]);
                }

                if (messageFromClient.trim().startsWith("/list")) {
                    myServer.getOnlineUsersList(handler);
                }

                if (messageFromClient.trim().startsWith("/end")) {
                    return;
                }
                if (messageFromClient.trim().startsWith("/changeNick")) {
                    String newNick = messageFromClient.split(" ", 2)[1];
                    if (!myServer.isNickBusy(newNick)) {
                        myServer.getAuthService().changeNick(name, newNick);
                        name = newNick;
                        handler.setName(name);
                        dos.writeUTF("Теперь ваш ник: " + name);
                    } else {
                        dos.writeUTF("Данный ник занят!");
                    }
                }
                if (messageFromClient.trim().startsWith("/info")) {
                    dos.writeUTF("/pm <nick> - личное сообщение\n" +
                            "/list - список подключённых пользователей\n" +
                            "/changeNick - сменить ник\n" +
                            "/end - выйти из чата");
                }
            } else {
                myServer.broadcastMessage(name + ": " + messageFromClient);
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ReadMessage(MyServer myServer, DataInputStream dis, DataOutputStream dos, ClientHandler handler) {
        this.dis = dis;
        this.dos = dos;
        this.myServer = myServer;
        this.name = handler.getName();
        this.handler = handler;

    }
}
