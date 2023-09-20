import lift.Passenger;
import lift.LiftView;

public class passengerThread extends Thread {
    private LiftView view;
    private Passenger passenger;
    private int fromfloor;
    private int tofloor;
    private monitor mon;

    public passengerThread(LiftView view, monitor mon) {
        this.mon = mon;
        this.view = view;
    }

    public void run() {
        while (true) {
            this.passenger = view.createPassenger();
            this.fromfloor = passenger.getStartFloor();
            this.tofloor = passenger.getDestinationFloor();

            passenger.begin();

            mon.arriveOnFloor(fromfloor);
            mon.waitForEntrance(fromfloor);
            passenger.enterLift();
            mon.stoppedWalking();
            mon.enteredOnFloor(fromfloor, tofloor);

            mon.waitforExit(tofloor);
            passenger.exitLift();
            mon.stoppedWalking();
            mon.exitOnFloor(tofloor);
            passenger.end();
        }
    }
}
