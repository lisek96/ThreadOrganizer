package zad3;

import java.util.concurrent.*;

public class MyFutureTask extends FutureTask implements Future {
    private int ordinalNumber;
    private String name;
    private boolean running = false;

    public MyFutureTask(String name, Callable callable) {
        super(callable);
        this.name = name;
    }

    public void setOrdinalNumber(int i) {
        ordinalNumber = i;
    }

    public void setRunning(boolean b) {
        running = true;
    }

    public boolean getRunning() {
        return running;
    }

    public String toString() {
        String msgFromThread = ordinalNumber + ": " + "Name: " + name + "|| State: ";
        if (isCancelled()) return msgFromThread + "Aborted:(";
        if (isDone()) {
            try {
                return msgFromThread + "Successfully done!" + " || Result: " + get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (running) return msgFromThread + "Running...";
        return msgFromThread + "Ready:)";
    }


}
