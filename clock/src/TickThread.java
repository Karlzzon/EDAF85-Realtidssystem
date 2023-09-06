import java.util.concurrent.*;

import clock.io.*;

public class TickThread extends Thread {
    ClockOutput out;
    Semaphore timeSemaphore;
    Time time;

    public TickThread(Time time, ClockOutput out, Semaphore timeSemaphore) {
        this.out = out;
        this.timeSemaphore = timeSemaphore;
        this.time = time;
    }

    @Override
    public void run() {
        while (true) {
            try {
                timeSemaphore.acquire();
                out.displayTime(time.getHours(), time.getMinutes(), time.getSeconds());
                time.tick();
                timeSemaphore.release();
                Thread.sleep(985); // sida 32 -34 ekvation

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
