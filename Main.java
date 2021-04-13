/**
 * @author Wójcik Rafał S21235
 */

package zad3;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Main {

    public static void main(String[] args) {
        final Callable taskType1 = () -> {
            int result = 0;
            for (int i = 0; i < 15; i++) {
                result += 1;
                Thread.sleep(500);
            }
            return result;
        };

        final Callable taskType2 = ()->{
          for(int i=0; i<100000000; i++) Thread.sleep(10000000);
          return true;
        };

        final Callable taskType3 = () -> "I'm the fastest!";
        List<MyFutureTask> listOfTasks = new ArrayList<>();
        listOfTasks.add(new MyFutureTask("CountTo15", taskType1));
        listOfTasks.add(new MyFutureTask("CountTo15", taskType1));
        listOfTasks.add(new MyFutureTask("CountTo15", taskType1));
        listOfTasks.add(new MyFutureTask("BetterAbortMe...", taskType2));
        listOfTasks.add(new MyFutureTask("BetterAbortMe...", taskType2));
        listOfTasks.add(new MyFutureTask("Usain Bolt", taskType3));

        SwingUtilities.invokeLater(() -> {
            JListOfTasks.summon(listOfTasks);
        });
    }
}
