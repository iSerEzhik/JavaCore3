package chat.serverside.interfaces;

public interface AuthService {
    void start();
    void stop();
    String getNickByLoginAndPassword(String login, String password);
    void changeNick(String oldNick,String newNick);
}