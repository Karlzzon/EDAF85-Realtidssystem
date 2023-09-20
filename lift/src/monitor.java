import lift.LiftView;

public class monitor {
    private int[] toEnter; // number of passengers waiting to enter the lift at each floor
    private int[] toExit; // number of passengers (in lift) waiting to exit at each floor

    private int currentFloor;
    private int nextFloor;

    private int passengersInLift;
    private boolean moving;
    private String direction;

    private LiftView view;

    private int MAX_PASSENGERS;
    int isWalking = 0;

    public monitor(LiftView view, int NBR_FLOORS, int MAX_PASSENGERS) {
        this.toEnter = new int[NBR_FLOORS];
        this.toExit = new int[NBR_FLOORS];
        this.currentFloor = 0;
        this.nextFloor = 1;
        this.view = view;
        this.direction = "UP";
        this.MAX_PASSENGERS = MAX_PASSENGERS;
        this.moving = true;
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
            view.openDoors(currentFloor);
            notifyAll();
            while ((toEnter[currentFloor] > 0 && !isFull()) || toExit[currentFloor] > 0 || isWalking > 0) {
                this.moving = false;
                wait();
            }
            view.closeDoors();
            this.moving = true;
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
        if (currentFloor == 6) {
            direction = "DOWN";
            nextFloor -= 2;
        }
        if (currentFloor == 0) {
            direction = "UP";
            nextFloor += 2;
        }
    }

    public synchronized void arriveOnFloor(int fromFloor) {
        toEnter[fromFloor]++;
        view.showDebugInfo(toEnter, toExit);
    }

    public synchronized void enteredOnFloor(int fromFloor, int toFloor) {
        passengersInLift++;
        toEnter[fromFloor]--;
        toExit[toFloor]++;
        view.showDebugInfo(toEnter, toExit);
        isWalking++;
        notifyAll();
    }

    public synchronized void waitForEntrance(int floor) {
        while (floor != currentFloor || isFull() || moving) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized void waitforExit(int toFloor) {
        while (toFloor != currentFloor) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized void exitOnFloor(int floor) {
        toExit[floor]--;
        view.showDebugInfo(toEnter, toExit);
        passengersInLift--;
        isWalking++;
        notifyAll();
    }

    public synchronized void stoppedWalking() {
        isWalking--;
        notifyAll();
    }

}
