import lift.LiftView;

public class monitor {
    private int[] toEnter; // number of passengers waiting to enter the lift at each floor
    private int[] toExit; // number of passengers (in lift) waiting to exit at each floor
    private int currentFloor;
    private int nextFloor;
    private int passengersInLift;
    private String direction;
    private LiftView view;
    private int MAX_PASSENGERS;
    private int NBR_FLOORS;
    private boolean doorsOpen;
    private int transitioning;

    public monitor(LiftView view, int NBR_FLOORS, int MAX_PASSENGERS) {
        this.toEnter = new int[NBR_FLOORS];
        this.toExit = new int[NBR_FLOORS];
        this.currentFloor = 0;
        this.nextFloor = 1;
        this.view = view;
        this.direction = "UP";
        this.MAX_PASSENGERS = MAX_PASSENGERS;
        this.doorsOpen = false;
        this.transitioning = 0;
        this.NBR_FLOORS = NBR_FLOORS;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getNextFloor() {
        return nextFloor;
    }

    public synchronized boolean isFull() {
        return (passengersInLift == MAX_PASSENGERS) ? true : false;
    }

    public synchronized void preMove() {
        try {
            while ((toEnter[currentFloor] > 0 && !isFull()) || toExit[currentFloor] > 0 || transitioning > 0) {
                if (!doorsOpen) {
                    view.openDoors(currentFloor);
                    doorsOpen = true;
                    notifyAll();
                }
                wait();
            }
            if (doorsOpen) {
                view.closeDoors();
                doorsOpen = false;
            }
            notifyAll();
        } catch (Exception e) {
        }
    }

    public synchronized void postMove() {
        if (direction.compareTo("UP") == 0) {
            currentFloor++;
            nextFloor++;
        } else {
            currentFloor--;
            nextFloor--;
        }
        if (currentFloor == NBR_FLOORS - 1) {
            direction = "DOWN";
            nextFloor -= 2;
        }
        if (currentFloor == 0) {
            direction = "UP";
            nextFloor += 2;
        }
        notifyAll();
    }

    public synchronized void boardingHandler(int fromFloor, int toFloor) {
        toEnter[fromFloor]++;
        try {
            while (fromFloor != currentFloor || isFull() || !doorsOpen) {
                wait();
            }
        } catch (Exception e) {
        }
        toEnter[fromFloor]--;
        toExit[toFloor]++;
        passengersInLift++;
        transitioning++;
        notifyAll();
    }

    public synchronized void exitingHandler(int toFloor) {
        while (toFloor != currentFloor || !doorsOpen) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        toExit[toFloor]--;
        passengersInLift--;
        transitioning++;
        notifyAll();

    }

    public synchronized void stopped() {
        transitioning--;
        notifyAll();
    }

}
