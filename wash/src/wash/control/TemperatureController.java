package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;
import static wash.control.WashingMessage.Order.*;

public class TemperatureController extends ActorThread<WashingMessage> {

    private WashingIO io;
    private int upperBound;
    private int lowerBound;
    private final double upperMargin = 0.678;
    private final double lowerMargin = 0.2952;
    private boolean idle;
    private ActorThread<WashingMessage> sender;
    private boolean startingUp;

    public TemperatureController(WashingIO io) throws InterruptedException {
        this.io = io;
        this.lowerBound = 0;
        this.upperBound = 0;
        this.idle = true;
        this.sender = null;
        this.startingUp = false;
    }

    @Override
    public void run() {
        try {

            while (true) {
                WashingMessage m = receiveWithTimeout(10000 / Settings.SPEEDUP);

                if (m != null) {
                    sender = m.sender();
                    switch (m.order()) {
                        case TEMP_IDLE:
                            io.heat(false);
                            idle = true;
                            sender.send(new WashingMessage(this, ACKNOWLEDGMENT));
                            break;
                        case TEMP_SET_40:
                            startingUp = true;
                            idle = false;
                            upperBound = 40;
                            lowerBound = 38;
                            io.heat(true);
                            break;
                        case TEMP_SET_60:
                            startingUp = true;
                            idle = false;
                            upperBound = 60;
                            lowerBound = 58;
                            io.heat(true);
                            break;
                        default:
                            break;
                    }
                }
                if ((io.getTemperature() + upperMargin >= upperBound) && !idle) {
                    io.heat(false);
                    if (startingUp) {
                        sender.send(new WashingMessage(this, ACKNOWLEDGMENT));
                        startingUp = false;
                    }
                }
                if ((io.getTemperature() - lowerMargin <= lowerBound) && !idle) {
                    io.heat(true);
                }
            }
        } catch (InterruptedException x) {
        }
    }
}
