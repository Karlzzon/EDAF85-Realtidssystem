import lift.LiftView;

public class liftThread extends Thread {
    private LiftView view;
    private monitor mon;

    public liftThread(LiftView view, monitor mon) {
        this.view = view;
        this.mon = mon;
    }

    public void run() {
        while (true) {
            mon.preMove();
            view.moveLift(mon.getCurrentFloor(), mon.getNextFloor());
            mon.postMove();
        }
    }

}
