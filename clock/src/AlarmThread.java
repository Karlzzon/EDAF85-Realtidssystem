import clock.io.ClockOutput;

public class AlarmThread extends Thread {
    Time time;
    ClockOutput out;

    public AlarmThread(Time t, ClockOutput out) {
        this.time = t;
        this.out = out;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (time.getHours() == time.getAlarmHours() && time.getMinutes() == time.getAlarmMinutes()
                        && time.getSeconds() == time.getAlarmSeconds()) {
                    for (int i = 0; i < 10; i++) {
                        out.alarm();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
