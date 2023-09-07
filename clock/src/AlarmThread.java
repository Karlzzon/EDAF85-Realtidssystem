import java.util.concurrent.Semaphore;
import clock.io.ClockOutput;

public class AlarmThread extends Thread {
    Semaphore alarmSemaphore;
    ClockOutput out;
    int blinkCount;

    public AlarmThread(Semaphore t, ClockOutput out) {
        this.alarmSemaphore = t;
        this.out = out;
        this.blinkCount = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                alarmSemaphore.acquire();
                long t = System.currentTimeMillis();
                long diff;
                while (blinkCount < 20) {
                    out.alarm();
                    blinkCount++;

                    t += 1000;
                    diff = t - System.currentTimeMillis();
                    if (diff > 0)
                        Thread.sleep(diff);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            blinkCount = 0;
        }
    }
}
