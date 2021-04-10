package chat.serverside.service.myThreads;

import java.util.concurrent.Callable;

    public class MyTimer implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(120000);
                if(Thread.currentThread().isInterrupted()){
                   return;
                }
            } catch (InterruptedException ignored) { ;
            }
        }
    }
