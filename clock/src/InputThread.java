import java.util.concurrent.Semaphore;
import clock.io.*;
import clock.io.ClockInput.UserInput;

public class InputThread extends Thread {
    ClockInput in;
    ClockOutput out;
    Time time;
    Semaphore timeSemaphore;
    boolean help;

    public InputThread(Time time, ClockInput in, ClockOutput out, Semaphore timeSemaphore) {
        this.in = in;
        this.time = time;
        this.timeSemaphore = timeSemaphore;
        this.out = out;
        this.help = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                in.getSemaphore().acquire();
                UserInput userInput = in.getUserInput();
                Choice c = userInput.choice();
                if (c == Choice.SET_TIME) {
                    timeSemaphore.acquire();
                    time.setHours(userInput.hours());
                    time.setMinutes(userInput.minutes());
                    time.setSeconds(userInput.seconds());
                    timeSemaphore.release();
                } else if (c == Choice.SET_ALARM) {
                    System.out.println("input for set alarm");
                    time.setAlarmHours(userInput.hours());
                    time.setAlarmMinutes(userInput.minutes());
                    time.setAlarmSeconds(userInput.seconds());
                } else if (c == Choice.TOGGLE_ALARM) {
                    System.out.println("input for toggle alarm");
                    help = !help;
                    out.setAlarmIndicator(help);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
