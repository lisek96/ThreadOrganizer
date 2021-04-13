package zad3;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JListOfTasks extends JFrame {
    ExecutorService threadPool;
    JList jList;
    JButton start;
    JButton cancel;
    JButton removeFromList;
    Thread checkingThread;
    static boolean alreadyAsked = false;
    static JListOfTasks listInstance;

    public static JListOfTasks summon(List<MyFutureTask> tasks){
        listInstance=new JListOfTasks(tasks);
        return listInstance;
    }

    private  JListOfTasks(List<MyFutureTask> tasks) {
        checkingThread = new Thread(() -> {
            Thread thread = Thread.currentThread();
            while (true) {
                if(thread.isInterrupted()) return;
                tasks.forEach(task -> {
                    if (task.isCancelled() || task.isDone()) {
                        task.setRunning(false);
                        repaint();
                    }
                });
            }
        });

        int option = 0;
        if (!alreadyAsked) {
            Object[] options = {"Yes, please", "No, thanks"};
            option = JOptionPane.showOptionDialog(null,
                    "Would you like to start all tasks automatically?",
                    "Go auto?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
        }


        threadPool = Executors.newFixedThreadPool(tasks.size());
        DefaultListModel<MyFutureTask> listModel = new DefaultListModel<>();
        for(int i=0; i<tasks.size(); i++){
            listModel.add(i, tasks.get(i));
        }
        jList = new JList(listModel);


        for (int i = 0; i < tasks.size(); i++) {
            MyFutureTask currentTask = tasks.get(i);
            currentTask.setOrdinalNumber(i + 1);
            if (!alreadyAsked && option == 0) {
                threadPool.execute(currentTask);
                currentTask.setRunning(true);
            }
        }


        alreadyAsked = true;

        JPanel pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = c.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 4;
        c.gridwidth = 3;
        pane.add(jList, c);
        start = new JButton("start");
        start.addActionListener((e) -> {
            MyFutureTask selectedTask = (MyFutureTask) jList.getSelectedValue();
            if (selectedTask!=null && !selectedTask.getRunning() && !selectedTask.isDone()) {
                threadPool.execute(selectedTask);
                selectedTask.setRunning(true);
                repaint();
            }
        });
        cancel = new JButton("cancel");
        cancel.addActionListener((e) -> {
            MyFutureTask selectedTask = (MyFutureTask) jList.getSelectedValue();
            if (selectedTask!=null && selectedTask.getRunning()) {
                selectedTask.cancel(true);
                selectedTask.setRunning(false);
                repaint();
            }
        });

        removeFromList = new JButton("remove");
        removeFromList.addActionListener((e) -> {
            checkingThread.interrupt();
            int selectedIndex = jList.getSelectedIndex();
            if(selectedIndex!=-1) {
                tasks.remove(selectedIndex);
                dispose();
                new JListOfTasks(tasks);
            }
        });
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 5;
        pane.add(start, c);
        c.gridx = 1;
        c.gridy = 5;
        pane.add(cancel, c);
        c.gridx = 2;
        c.gridy = 5;
        pane.add(removeFromList, c);

        add(pane);

        pack();
        setSize(new Dimension(500, 300));
        setTitle("List of tasks");
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        checkingThread.start();
    }
}

