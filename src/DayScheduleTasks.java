import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DayScheduleTasks extends JPanel {

    public DayScheduleTasks(LocalDate date, Database database, JFrame frame, JPanel parentPanel) {


        setPreferredSize(new Dimension(400, 300));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(("dd-MM-yyyy"));
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        topPanel.setBackground(null);

        JLabel tasksLabel = new JLabel("Tasks");
        tasksLabel.setFont(new Font("Helvetica", Font.BOLD, 25));
        topPanel.add(tasksLabel, BorderLayout.WEST);

        JLabel addButton = new JLabel(new ImageIcon("CalendarScheduler-master/resources/add.png"));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                new TaskEditor(new Task(date), database, frame, parentPanel);
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
        topPanel.add(addButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        ArrayList<Task> tasks = database.getTasks(dateFormatter.format(date));
        int rows = 6;
        if (tasks.size() > 5) {
            rows = tasks.size() + 1;
        }


        JPanel list = new JPanel(new GridLayout(rows, 1, 2, 2));
        list.setBackground(Color.WHITE);



        JPanel header = new JPanel(new GridLayout(1, 6));
        header.setPreferredSize(new Dimension(350, 40));
        header.setBackground(Color.decode("#aa6231"));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        header.add(createHeaderLabel(""));
        header.add(createHeaderLabel("Time"));
        header.add(createHeaderLabel("Task"));
        header.add(createHeaderLabel("Category"));
        header.add(createHeaderLabel("Done"));
        list.add(header);


        for (int i = 0; i < tasks.size(); i++) {
            final int j = i;

            JPanel task = new JPanel(new GridLayout(1, 6));
            task.setPreferredSize(new Dimension(350, 30));
            task.setBackground(Color.decode("#f0f0f0"));
            task.setCursor(new Cursor(Cursor.HAND_CURSOR));
            task.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Opens task editor to edit selected task
                    new TaskEditor(tasks.get(j), database, frame, parentPanel);
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

            // add the task detail labels to the task panel
            task.add(createBodyLabel(Integer.toString(i + 1), "#000000"));
            task.add(createBodyLabel(tasks.get(i).getTimeToString(), "#000000"));
            task.add(createBodyLabel(tasks.get(i).getTitle(), "#000000"));
            task.add(createBodyLabel(tasks.get(i).getCategory(), getTaskColor(tasks.get(i).getCategory())));



            Icon notSelected = new ImageIcon("CalendarScheduler-master/resources/check-box-not-selected.png");
            Icon selected = new ImageIcon("CalendarScheduler-master/resources/check-box-selected.png");
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(tasks.get(i).isDone());
            checkBox.setIcon(notSelected);
            checkBox.setSelectedIcon(selected);
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    Task t = tasks.get(j);
                    t.setDone(checkBox.isSelected());
                    database.updateTask(t);
                    parentPanel.revalidate();
                }
            });
            task.add(checkBox);
            list.add(task);
        }


        JScrollPane scrollPane = new JScrollPane(list);
        add(scrollPane, BorderLayout.CENTER);
    }


    private JLabel createHeaderLabel(String label) {
        JLabel headerLabel = new JLabel(label);
        headerLabel.setFont(new Font("Helvetica", Font.BOLD, 15));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return headerLabel;
    }


    private JLabel createBodyLabel(String label, String fontColor) {
        JLabel bodyLabel = new JLabel(label);
        bodyLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        bodyLabel.setFont(new Font("Helvetica", Font.PLAIN, 15));
        bodyLabel.setForeground(Color.decode(fontColor));
        return bodyLabel;
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
