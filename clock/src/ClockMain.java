import java.util.concurrent.Semaphore;
import clock.AlarmClockEmulator;
import clock.io.ClockInput;
import clock.io.ClockOutput;

public class ClockMain {
    public static void main(String[] args) throws InterruptedException {
        AlarmClockEmulator emulator = new AlarmClockEmulator();

        ClockInput in = emulator.getInput();
        ClockOutput out = emulator.getOutput();

        Semaphore timeSemaphore = new Semaphore(1);
        Semaphore alarmSemaphore = new Semaphore(0);
        Time t = new Time();

        InputThread inputThread = new InputThread(t, in, out, timeSemaphore);
        TickThread tickThread = new TickThread(t, out, timeSemaphore, alarmSemaphore);
        AlarmThread alarmThread = new AlarmThread(alarmSemaphore, out);

        tickThread.start();
        alarmThread.start();
        inputThread.start();

    }
}
