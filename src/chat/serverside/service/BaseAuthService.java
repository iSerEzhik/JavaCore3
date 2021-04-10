package chat.serverside.service;

import chat.serverside.interfaces.AuthService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseAuthService implements AuthService {

//    private final List<Entry> entryList;
    private Statement statement;



    public BaseAuthService() {
    }

    @Override
    public void start() {
        System.out.println("AuthService start");
        try {
            statement = Connect.getConnection().createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.out.println("AuthService stop");
    }

    @Override
    public String getNickByLoginAndPassword(String login,String password) {
        try {
            PreparedStatement ps = Connect.getConnection().prepareStatement("select nick from mySignUsers where login = ? and password = ?");
            ps.setString(1,login);
            ps.setString(2,password);
            ResultSet set = ps.executeQuery();
            if (set.next()){
                return set.getString("nick");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    @Override
    public void changeNick(String oldNick,String newNick){
        try {
            statement.executeUpdate("update mysignusers set nick = "+newNick+"where nick = "+oldNick);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}