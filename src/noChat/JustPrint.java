package noChat;

public class JustPrint {
    public synchronized void printA() {
        for (int i = 0; i < 5; i++) {
            try {
                System.out.print("A");
                notifyAll();
                wait();
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public synchronized void printB() {
        for (int i = 0; i < 5; i++) {
            try {
                System.out.print("B");
                notifyAll();
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public synchronized void printC() {
        for (int i = 0; i < 5; i++) {
            try {
                System.out.println("C");
                notifyAll();
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
