package wash.control;

import wash.io.WashingIO;
import wash.simulation.WashingSimulator;

public class Wash {

    public static void main(String[] args) throws InterruptedException {
        WashingSimulator sim = new WashingSimulator(Settings.SPEEDUP);

        WashingIO io = sim.startSimulation();

        TemperatureController temp = new TemperatureController(io);
        WaterController water = new WaterController(io);
        SpinController spin = new SpinController(io);

        temp.start();
        water.start();
        spin.start();

        WashingProgram1 one = new WashingProgram1(io, temp, water, spin);
        WashingProgram2 two = new WashingProgram2(io, temp, water, spin);
        WashingProgram3 three = new WashingProgram3(io, temp, water, spin);

        while (true) {
            int button = io.awaitButton();
            System.out.println("user selected program " + button);

            switch (button) {
                case 0:
                    one.interrupt();
                    two.interrupt();
                    three.interrupt();
                    break;

                case 1:
                    one.start();
                    break;
                case 2:
                    two.start();
                    break;
                case 3:
                    three.start();
            }
        }
    }
};
