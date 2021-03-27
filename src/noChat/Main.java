package noChat;

public class Main {
    private static JustPrint print = new JustPrint();

    public static void main(String[] args) {
        Thread ta = new Thread(()->{
                print.printA();
        });
        Thread tb = new Thread(()->{
                print.printB();
        });
        Thread tc = new Thread(()->{
                print.printC();

        });
        ta.start();
        tb.start();
        tc.start();
    }
}
