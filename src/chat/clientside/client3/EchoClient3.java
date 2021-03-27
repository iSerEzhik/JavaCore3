package chat.clientside.client3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class EchoClient3 extends JFrame {

    private final Integer SERVER_PORT = 8083;
    private final String SERVER_ADDRESS = "localhost";
    private Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    boolean isAuthorized = false;
    private JTextField msgInputField;
    private JTextArea chatArea;
    Logs log;
    public EchoClient3() {
        prepareGUI();
        try {
            connection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void connection() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        log = new Logs();
        new Thread(() -> {
            try {
                while (true) {
                    String messageFromServer = dis.readUTF();
                    if (messageFromServer.startsWith("/authok")) {
                        isAuthorized = true;

                        chatArea.append(messageFromServer + "\n");
                        chatArea.setText("");
                        log.takeLog();
                        break;
                    }

                    chatArea.append(messageFromServer + "\n");
                }
                while (isAuthorized) {
                    String messageFromServer = dis.readUTF();
                    log.addToLog(messageFromServer);
                    chatArea.append(messageFromServer + "\n");

                }
            } catch (IOException ignored) {

            }
        }).start();
    }

    public void send() {
        if (msgInputField.getText() != null && !msgInputField.getText().trim().isEmpty()) {
            try {
                dos.writeUTF(msgInputField.getText());
                if (msgInputField.getText().equals("/end")) {
                    isAuthorized = false;
                    closeConnection();
                }
                msgInputField.setText("");
            } catch (IOException ignored) {
            }
        }
    }

    private void closeConnection() {
        try {
            dos.writeUTF("/end");
            log.saveLogs();
            dis.close();
            dos.close();
            socket.close();

        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }


    public void onAuthClcik(String log,String pas) {
        try {
            dos.writeUTF("/auth" + " " + log + " " + pas);
        } catch (IOException ignored) {
        }
    }


    public void prepareGUI() {

        setBounds(600, 300, 500, 500);
        setTitle("Клиент3");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                    closeConnection();
                    dispose();

            }
        });
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setAutoscrolls(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel topPanel  = new JPanel(new BorderLayout());
        JButton btnSendMsg = new JButton("Отправить");
        JButton btnLog = new JButton("Логин");
        JLabel topLab = new JLabel("Big Local Chat");
        topPanel.add(btnLog,BorderLayout.WEST);
        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
        msgInputField = new JTextField();
        add(topPanel,BorderLayout.NORTH);
        topPanel.add(topLab,BorderLayout.CENTER);
        topLab.setFont(new Font(topLab.getFont().getName(),Font.LAYOUT_RIGHT_TO_LEFT,16));
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(msgInputField, BorderLayout.CENTER);
        chatArea.setBackground(new Color(143, 224, 187));
        btnSendMsg.addActionListener(e -> {
            send();
        });

        msgInputField.addActionListener(e -> {
            send();
        });
        btnLog.addActionListener(e -> {
            new prepareLogWin();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

            }
        });

        setVisible(true);
    }
    private class prepareLogWin extends JFrame{
        public prepareLogWin(){
        setBounds(700,330,300,120);
        setTitle("Авторизация");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JButton btnLog = new JButton("Логин");
        JPanel botPan = new JPanel(new BorderLayout());
        JPanel mainPan = new JPanel();
        BoxLayout layoutLogWin = new BoxLayout(mainPan,BoxLayout.Y_AXIS);
        mainPan.setLayout(layoutLogWin);
        JPanel logPan = new JPanel();
        JPanel pasPan = new JPanel();
        logPan.setLayout(new BorderLayout());
        JLabel labLog = new JLabel("Логин    ");
        JTextField fieldLog = new JTextField();
        logPan.add(labLog,BorderLayout.WEST);
        logPan.add(fieldLog,BorderLayout.CENTER);
        pasPan.setLayout(new BorderLayout());
        JLabel labPas = new JLabel("Пароль");
        JTextField fieldPas = new JTextField();
        pasPan.add(labPas,BorderLayout.WEST);
        pasPan.add(fieldPas,BorderLayout.CENTER);
        add(mainPan,BorderLayout.CENTER);
        add(botPan,BorderLayout.SOUTH);
        botPan.add(btnLog,BorderLayout.CENTER);
        mainPan.add(logPan);
        mainPan.add(pasPan);
        setVisible(true);
        btnLog.addActionListener(e->{
            onAuthClcik(fieldLog.getText(),fieldPas.getText());
            fieldLog.setText("");
            fieldPas.setText("");
            dispose();
        });
        }
    }
    private class Logs{
        private final File myLog = new File("src//chat.clientside//client3//logs.txt");
        private ArrayList<String> logs = new ArrayList<>();


        public Logs() throws IOException {
            myLog.createNewFile();
        }

        public void takeLog() throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(myLog));
            while (reader.ready()){
                String str =reader.readLine();
                logs.add(str);
                chatArea.append(str + '\n');
            }
        }

        public void addToLog(String mes){
            if (logs.size()>100){
                logs.remove(0);
            }
            logs.add(mes);
        }

        public void saveLogs() throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(myLog));
            for (String str: logs) {
                writer.write(str + '\n');
            }
            writer.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EchoClient3();
        });
    }
}