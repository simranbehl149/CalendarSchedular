import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Tasks extends JPanel {

    public Tasks(LocalDate date, Database database, JFrame frame, JPanel mainPanel) {
        //Set up tasks panel
        setPreferredSize(new Dimension(400, 400));
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 10, 15, 10));

        //Add tasks to panel
        createTasksSection(date, database, frame, mainPanel);
    }

    private void createTasksSection(LocalDate date, Database database, JFrame frame, JPanel mainPanel) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(("dd-MM-yyyy"));

        ArrayList<Task> tasks = database.getTasks(dateFormatter.format(date));

        int rows = 4;

        if (tasks.size() > 4) {
            rows = tasks.size();
        }

        JPanel list = new JPanel(new GridLayout(rows, 1, 10, 5));
        list.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(list);

        for (int i = 0; i < tasks.size(); i++) {
            final int j = i;
            // Panel to store a task
            JPanel task = new JPanel(new GridLayout(2, 2));
            task.setPreferredSize(new Dimension(400,80));
            task.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20), BorderFactory.createMatteBorder(0, 10, 0, 0, Color.decode(getTaskColor(tasks.get(i).getCategory())))));
            task.setBackground(Color.decode("#e3deca"));
            task.setCursor(new Cursor(Cursor.HAND_CURSOR));
            task.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    new TaskEditor(tasks.get(j), database, frame, mainPanel);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });

            JPanel taskTop = new JPanel(new BorderLayout());
            taskTop.setBackground(null);

            JLabel title = new JLabel(tasks.get(i).getTitle());
            title.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
            title.setFont(new Font("Helvetica", Font.PLAIN, 20));
            title.setForeground(Color.decode("#5c2c0c"));
            taskTop.add(title, BorderLayout.WEST);

           //Icon notSelected = new ImageIcon("resources/check-box-not-selected.png");
            Icon notSelected = new ImageIcon("CalendarScheduler-master/resources/check-box-not-selected.png");
            //Icon selected = new ImageIcon("resources/check-box-selected.png");
            Icon selected = new ImageIcon("CalendarScheduler-master/resources/check-box-selected.png");
            JCheckBox checkBox = new JCheckBox();
            checkBox.setBackground(null);
            checkBox.setSelected(tasks.get(i).isDone());
            checkBox.setIcon(notSelected);
            checkBox.setSelectedIcon(selected);
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    // Writes to database when checkbox state is changed.
                    Task t = tasks.get(j);
                    t.setDone(checkBox.isSelected());
                    database.updateTask(t);
                }
            });
            taskTop.add(checkBox, BorderLayout.EAST);

            // Set up time label to be shown under taskTop(title and checkbox).
            JLabel time = new JLabel(tasks.get(i).getDateTimeToString());
            time.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            time.setFont(new Font("Helvetica", Font.PLAIN, 15));
            time.setForeground(Color.DARK_GRAY);
            task.add(taskTop);
            task.add(time);
            list.add(task);
        }
        add(scrollPane, BorderLayout.CENTER);

        // Set up new button to add new task from home page
        JButton newTaskButton = new JButton("New");
        newTaskButton.setFont(new Font("Helvetica", Font.PLAIN, 15));
        newTaskButton.setBackground(Color.decode("#dda35d"));
        newTaskButton.setForeground(Color.WHITE);
        newTaskButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        newTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Opens task editor to add new task
                new TaskEditor(new Task(date), database, frame, mainPanel);
            }
        });
        add(newTaskButton, BorderLayout.SOUTH);
    }


    private String getTaskColor(String category) {
        switch (category) {
            case "General":
                return "#666822";
            case "Holiday":
                return "#c67713";
            case "Personal":
                return "#c1380a";
            case "Meeting":
                return "#742505";
            case "Social":
                return "#4d2508";
            default:
                return "#666822";
        }
    }
}
