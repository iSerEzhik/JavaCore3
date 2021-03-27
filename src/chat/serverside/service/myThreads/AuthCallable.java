package chat.serverside.service.myThreads;

import chat.serverside.service.ClientHandler;
import chat.serverside.service.MyServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

public class AuthCallable implements Callable {
    private final MyServer myServer;
    private final DataInputStream dis;
    private final ClientHandler handler;

    @Override
    public String call() throws Exception {
        while (true) {
            String str = dis.readUTF();
            if (str.startsWith("/auth")) {
                String[] arr = str.split("\\s");
                String nick = myServer
                        .getAuthService()
                        .getNickByLoginAndPassword(arr[1], arr[2]);
                if (nick != null) {
                    if (!myServer.isNickBusy(nick)) {
                        handler.setName(nick);
                        return "/authok";
                    } else {
                        handler.sendMessage("Nick is busy");
                    }
                } else {
                    handler.sendMessage("Wrong login and password");
                }
            } else {
                handler.sendMessage("Вы не авторизовались");
            }
        }
    }

    public AuthCallable(MyServer server, DataInputStream dis, ClientHandler handler) {
        this.myServer = server;
        this.dis = dis;
        this.handler = handler;
    }
}