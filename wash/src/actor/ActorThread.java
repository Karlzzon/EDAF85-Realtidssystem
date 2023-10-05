package actor;

import java.util.concurrent.LinkedBlockingQueue;

public class ActorThread<M> extends Thread {

    private LinkedBlockingQueue<M> ayalla;

    public ActorThread() {
        ayalla = new LinkedBlockingQueue<M>();
    }

    /** Called by another thread, to send a message to this thread. */
    public void send(M message) {
        try {
            ayalla.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /** Returns the first message in the queue, or blocks if none available. */
    protected M receive() throws InterruptedException {
        return ayalla.take();
    }

    /**
     * Returns the first message in the queue, or blocks up to 'timeout'
     * milliseconds if none available. Returns null if no message is obtained
     * within 'timeout' milliseconds.
     */
    protected M receiveWithTimeout(long timeout) throws InterruptedException {
        return ayalla.poll(timeout, java.util.concurrent.TimeUnit.MILLISECONDS);
    }
}