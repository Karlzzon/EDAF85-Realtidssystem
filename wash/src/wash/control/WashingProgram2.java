package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;
import static wash.control.WashingMessage.Order.*;

public class WashingProgram2 extends ActorThread<WashingMessage> {

    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;

    public WashingProgram2(WashingIO io,
            ActorThread<WashingMessage> temp,
            ActorThread<WashingMessage> water,
            ActorThread<WashingMessage> spin) {
        this.io = io;
        this.temp = temp;
        this.water = water;
        this.spin = spin;
    }

    @Override
    public void run() {
        try {
            // Lock the hatch
            io.lock(true);

            // Let water in
            water.send(new WashingMessage(this, WATER_FILL));
            WashingMessage waterfilled = receive();
            System.out.println("got " + waterfilled);

            // Heat to 40C and start spin
            temp.send(new WashingMessage(this, TEMP_SET_40));
            WashingMessage waitForSuffTemp = receive();
            System.out.println("got " + waitForSuffTemp);
            spin.send(new WashingMessage(this, SPIN_SLOW));
            WashingMessage spinningSlow = receive();
            System.out.println("got " + spinningSlow);
            Thread.sleep(20 * 60000 / Settings.SPEEDUP);
            temp.send(new WashingMessage(this, TEMP_IDLE));
            WashingMessage tempIdle = receive();
            System.out.println("got " + tempIdle);

            // Drain
            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage draining = receive();
            System.out.println("got " + draining);

            // Let water in
            water.send(new WashingMessage(this, WATER_FILL));
            WashingMessage waterfilled2 = receive();
            System.out.println("got " + waterfilled2);

            // Heat to 60C and start spin
            temp.send(new WashingMessage(this, TEMP_SET_60));
            WashingMessage waitForSuffTemp2 = receive();
            System.out.println("got " + waitForSuffTemp2);
            spin.send(new WashingMessage(this, SPIN_SLOW));
            WashingMessage spinningSlow2 = receive();
            System.out.println("got " + spinningSlow2);
            Thread.sleep(30 * 60000 / Settings.SPEEDUP);
            temp.send(new WashingMessage(this, TEMP_IDLE));
            WashingMessage tempIdle2 = receive();
            System.out.println("got " + tempIdle2);

            // Drain
            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage draining2 = receive();
            System.out.println("got " + draining2);

            // Rinse 5 times
            for (int i = 0; i < 5; i++) {
                // fill with cold
                water.send(new WashingMessage(this, WATER_FILL));
                WashingMessage coldWater = receive();
                System.out.println("got " + coldWater);
                // wait 2 min
                Thread.sleep(2 * 60000 / Settings.SPEEDUP);

                // drain
                water.send(new WashingMessage(this, WATER_DRAIN));
                WashingMessage draining3 = receive();
                System.out.println("got " + draining3);
            }
            // Centrifuge 5 min
            spin.send(new WashingMessage(this, SPIN_FAST));
            water.send(new WashingMessage(this, WATER_DRAIN));
            WashingMessage spinningFast = receive();
            System.out.println("got " + spinningFast);
            Thread.sleep(5 * 60000 / Settings.SPEEDUP);

            // Stop the program
            spin.send(new WashingMessage(this, SPIN_OFF));
            WashingMessage spinningOff = receive();
            System.out.println("got " + spinningOff);
            water.send(new WashingMessage(this, WATER_IDLE));

            // Unlock the hatch
            io.lock(false);

        } catch (InterruptedException e) {

            // If we end up here, it means the program was interrupt()'ed:
            // set all controllers to idle

            temp.send(new WashingMessage(this, TEMP_IDLE));
            water.send(new WashingMessage(this, WATER_IDLE));
            spin.send(new WashingMessage(this, SPIN_OFF));
            System.out.println("washing program terminated");
        }
    }
}