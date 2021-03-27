package serverside.service;

import javax.swing.text.BadLocationException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandler {

    private final MyServer myServer;
    private final Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private String name;
    private volatile boolean endSession;
    private boolean isAuthorized;








    public ClientHandler(MyServer myServer, Socket socket) {
        try {

            this.myServer = myServer;
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            new Thread(() -> {
                try {
                    authentication();
                    readMessage();
                } catch (IOException | InterruptedException ignored) {
                } finally {
                    closeConnection();
                }

            }).start();

            new Thread(() -> {
                try {
                    dos.writeUTF("Вы не авторизованы\nДля авторизации у вас есть 2 минуты");
                    Thread.sleep(120000);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                if (!isAuthorized) {
                    closeConnection();
                }
            }

            ).start();

        } catch (IOException e) {
            closeConnection();
            throw new RuntimeException("Problem with ClientHandler");
        }
    }

    public void authentication() throws IOException, InterruptedException {
        while (true) {
            String str = dis.readUTF();
            if (str.startsWith("/auth")) { //  /auth login password
                String[] arr = str.split("\\s");
                String nick = myServer
                        .getAuthService()
                        .getNickByLoginAndPassword(arr[1], arr[2]);
                if (nick != null) {
                    if (!myServer.isNickBusy(nick)) {
                        isAuthorized = true;

                        name = nick;
                        dos.writeUTF("/authok");
                        sendMessage("К нам подключился " + nick);
                        myServer.broadcastMessage("Hello " + name);
                        myServer.subscribe(this);
                        return;
                    } else {
                        sendMessage("Nick is busy");
                    }
                } else {
                    sendMessage("Wrong login and password");
                }
            } else {
                if (!isAuthorized){
                    dos.writeUTF("Вы не авторизовались");
                }
            }
        }
    }
    public void readMessage() throws IOException {
        while (true) {
            String messageFromClient = dis.readUTF();
            System.out.println(name + " send message " + messageFromClient);
            if (messageFromClient.trim().startsWith("/")) {

                if (messageFromClient.startsWith("/pm")) {
                    String[] arr = messageFromClient.split(" ", 3);
                    myServer.sendMessageToCertainClient(this, arr[1], name + ": " + arr[2]);
                }

                if (messageFromClient.trim().startsWith("/list")) {
                    myServer.getOnlineUsersList(this);
                }

                if (messageFromClient.trim().startsWith("/end")) {
                    return;
                }
                if (messageFromClient.trim().startsWith("/changeNick")) {
                    String newNick = messageFromClient.split(" ", 2)[1];
                    if (!myServer.isNickBusy(newNick)) {
                        myServer.getAuthService().changeNick(name, newNick);
                        name = newNick;

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
}
