package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

import static wash.control.WashingMessage.Order.*;

public class WaterController extends ActorThread<WashingMessage> {
    private enum waterState {
        IDLE, FILL, DRAIN
    }

    private WashingIO io;
    private waterState state;
    private ActorThread<WashingMessage> sender;
    private boolean centrifuge;

    public WaterController(WashingIO io) {
        this.io = io;
        state = waterState.IDLE;
        sender = null;
    }

    @Override
    public void run() {
        try {
            while (true) {
                WashingMessage message = receiveWithTimeout(5000 / Settings.SPEEDUP);

                if (message != null) {
                    switch (message.order()) {
                        case WATER_IDLE:
                            io.fill(false);
                            io.drain(false);
                            break;
                        case WATER_FILL:
                            state = waterState.FILL;
                            sender = message.sender();
                            io.fill(true);
                            break;
                        case WATER_DRAIN:
                            state = waterState.DRAIN;
                            sender = message.sender();
                            io.drain(true);
                            if (io.getWaterLevel() == 0) {
                                centrifuge = true;
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (io.getWaterLevel() >= 10 && state == waterState.FILL) {
                    state = waterState.IDLE;
                    io.fill(false);
                    sender.send(new WashingMessage(this, ACKNOWLEDGMENT));
                }
                if (io.getWaterLevel() == 0 && state == waterState.DRAIN && !centrifuge) {
                    state = waterState.IDLE;
                    io.drain(false);
                    sender.send(new WashingMessage(this, ACKNOWLEDGMENT));
                }
            }
        } catch (InterruptedException e) {
        }
    }
}
