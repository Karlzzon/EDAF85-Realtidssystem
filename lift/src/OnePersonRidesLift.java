import lift.LiftView;

public class OnePersonRidesLift {

    public static void main(String[] args) {

        final int NBR_FLOORS = 7, MAX_PASSENGERS = 4;

        LiftView view = new LiftView(NBR_FLOORS, MAX_PASSENGERS);
        monitor mon = new monitor(view, NBR_FLOORS, MAX_PASSENGERS);
        liftThread lift = new liftThread(view, mon);

        lift.start();
        for (int i = 0; i < 20; i++) {
            new passengerThread(view, mon).start();
        }

    }
}