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
                while (blinkCount < 3) {
                    out.alarm();
                    blinkCount++;
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            blinkCount = 0;
        }
    }
}
