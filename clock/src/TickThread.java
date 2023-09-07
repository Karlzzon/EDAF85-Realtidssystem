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

    long t = System.currentTimeMillis();
    long diff;

    @Override
    public void run() {
        while (true) {
            try {
                timeSemaphore.acquire();
                time.tick();
                out.displayTime(time.getHours(), time.getMinutes(), time.getSeconds());
                if (time.match()) {
                    alarmSemaphore.release(); // for signaling the alarmThread(to avoid having time inside the
                                              // alarmThread)
                }

                timeSemaphore.release();
                t += 1000;
                diff = t - System.currentTimeMillis();
                if (diff > 0)
                    Thread.sleep(diff);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
