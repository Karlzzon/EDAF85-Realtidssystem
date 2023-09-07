import java.util.concurrent.*;
import clock.io.*;

public class TickThread extends Thread {
    ClockOutput out;
    Semaphore timeSemaphore;
    Time time;
    Semaphore alarmSemaphore;

    public TickThread(Time time, ClockOutput out, Semaphore timeSemaphore, Semaphore alarmSemaphore) {
        this.out = out;
        this.timeSemaphore = timeSemaphore;
        this.time = time;
        this.alarmSemaphore = alarmSemaphore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                timeSemaphore.acquire();
                out.displayTime(time.getHours(), time.getMinutes(), time.getSeconds());
                time.tick();
                if (time.match()) {
                    alarmSemaphore.release(); // for signaling the alarmThread(to avoid having time inside the
                                              // alarmThread)
                }

                timeSemaphore.release();
                Thread.sleep(985); // sida 32 -34 ekvation

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
