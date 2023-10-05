package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;
import wash.io.WashingIO.Spin;
import static wash.control.WashingMessage.Order.*;

public class SpinController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    private WashingIO io;
    private String direction;
    private boolean isSpinningSlow;

    public SpinController(WashingIO io) {
        // TODO
        this.io = io;
        direction = "RIGHT";
        isSpinningSlow = false;

    }

    @Override
    public void run() {

        // this is to demonstrate how to control the barrel spin:
        // io.setSpinMode(Spin.IDLE);

        try {
            while (true) {
                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(60000 / Settings.SPEEDUP);

                // if m is null, it means a minute passed and no message was received
                if (m != null) {
                    System.out.println("got " + m);
                    switch (m.order()) {
                        case SPIN_SLOW:
                            direction = "LEFT";
                            isSpinningSlow = true;
                            io.setSpinMode(Spin.LEFT);
                            break;
                        case SPIN_FAST:
                            isSpinningSlow = false;
                            io.setSpinMode(Spin.FAST);
                            break;
                        case SPIN_OFF:
                            isSpinningSlow = false;
                            io.setSpinMode(Spin.IDLE);
                            break;
                        default:
                            break;
                    }
                    m.sender().send(new WashingMessage(this, ACKNOWLEDGMENT));
                }
                if (isSpinningSlow) {
                    if (direction.equals("LEFT")) {
                        direction = "RIGHT";
                        io.setSpinMode(Spin.RIGHT);
                    } else {
                        direction = "LEFT";
                        io.setSpinMode(Spin.LEFT);
                    }
                }
            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
            throw new Error(unexpected);
        }
    }
}
