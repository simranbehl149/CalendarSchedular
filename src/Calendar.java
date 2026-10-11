import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;


public class Calendar extends JPanel {

    public Calendar(
            int year,
            int month,
            LocalDate selectedDay,
            JFrame frame,
            JPanel parentPanel,
            Database database) {

        setPreferredSize(new Dimension(400, 400));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 0));
        setBackground(null);

        JPanel top = new JPanel(new BorderLayout());
        top.setPreferredSize(new Dimension(350, 50));
        top.setBackground(null);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        JLabel date = new JLabel(LocalDate.of(year, month, 1).format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        date.setHorizontalAlignment(JLabel.CENTER);
        date.setFont(new Font("Helvetica", Font.BOLD, 25));
        date.setForeground(Color.decode("#c1380a"));
        top.add(date, BorderLayout.CENTER);

        JLabel left = new JLabel(new ImageIcon("CalendarScheduler-master/resources/arrow-left.png"));
        left.setCursor(new Cursor(Cursor.HAND_CURSOR));
        left.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentPanel.removeAll();

                if (month != 1) {
                    resetMainPanel(frame, parentPanel, selectedDay, database, new Calendar(year, month - 1, selectedDay, frame, parentPanel, database));
                } else {
                    resetMainPanel(frame, parentPanel, selectedDay, database, new Calendar(year - 1, 12, selectedDay, frame, parentPanel, database));
                }
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
        top.add(left, BorderLayout.WEST);

        JLabel right = new JLabel(new ImageIcon("CalendarScheduler-master/resources/arrow-right.png"));
        right.setCursor(new Cursor(Cursor.HAND_CURSOR));
        right.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (month != 12) {
                    resetMainPanel(frame, parentPanel, selectedDay, database, new Calendar(year, month + 1, selectedDay, frame, parentPanel, database));
                } else {
                    resetMainPanel(frame, parentPanel, selectedDay, database, new Calendar(year + 1, 1, selectedDay, frame, parentPanel, database));
                }
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
        top.add(right, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);


        JPanel days = new JPanel(new GridLayout(7, 7));
        days.setBackground(null);

        Color header = Color.decode("#aa6231");
        days.add(new DayLabel("S", header, Color.white, false));
        days.add(new DayLabel("M", header, Color.white, false));
        days.add(new DayLabel("T", header, Color.white, false));
        days.add(new DayLabel("W", header, Color.white, false));
        days.add(new DayLabel("T", header, Color.white, false));
        days.add(new DayLabel("F", header, Color.white, false));
        days.add(new DayLabel("S", header, Color.white, false));

        String[] weekDays = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};


        LocalDate firstDay = LocalDate.of(year, month, 1);
        int j = 0;


        while (!firstDay.getDayOfWeek().toString().equals(weekDays[j])) {
            days.add(new DayLabel("", Color.decode("#e3deca"), Color.BLACK, false));
            j++;
        }


        int daysNum = YearMonth.of(year, month).lengthOfMonth();

        for (int i = 1; i <= daysNum; i++) {
            final int day = i;
            DayLabel dayLabel;

            if (selectedDay.getYear() == year && selectedDay.getMonthValue() == month && selectedDay.getDayOfMonth() == i) {

                dayLabel = new DayLabel(i + "", Color.decode("#bfbeba"), Color.BLACK, true);
            } else if (database.hasTasks(dateFormatter.format(LocalDate.of(year, month, i)))) {

                dayLabel = new DayLabel(i + "", Color.decode("#dda35d"), Color.WHITE, true);
            } else {

                dayLabel = new DayLabel(i + "", Color.decode("#e3deca"), Color.BLACK, true);
            }

            LocalDate today = LocalDate.now();
            if (today.getYear() == year && today.getMonthValue() == month && today.getDayOfMonth() == i) {
                dayLabel = new DayLabel(i + "", Color.decode("#3c3a1e"), Color.WHITE, true);
            }

            dayLabel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    LocalDate selected = LocalDate.of(year, month, day);
                    resetMainPanel(frame, parentPanel, selected, database, new Calendar(year, month, selected, frame, parentPanel, database));

                    if (e.getClickCount() == 2) {
                        parentPanel.setVisible(false);
                        DaySchedule daySchedulePanel = new DaySchedule(frame, selected, database);
                        daySchedulePanel.setVisible(true);
                        frame.add(daySchedulePanel);
                    }
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
            days.add(dayLabel);
        }

        for (int i = 0; i < (42 - (j + daysNum)); i++) {
            days.add(new DayLabel("", Color.decode("#e3deca"), Color.BLACK, false));
        }

        add(days, BorderLayout.CENTER);
    }

    private static void resetMainPanel(JFrame parentFrame, JPanel mainPanel, LocalDate selectedDay, Database database, Calendar c) {
        mainPanel.removeAll();
        LocalDate date = LocalDate.now();
        String dateString = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, EEEE"));
        JLabel todayLabel = new JLabel(dateString);
        todayLabel.setFont(new Font("Helvetica", Font.BOLD, 40));
        todayLabel.setForeground(Color.decode("#4D2508"));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(5, 15, 5, 15);
        mainPanel.add(todayLabel, constraints);

        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(c, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        mainPanel.add(new Tasks(selectedDay, database, parentFrame, mainPanel), constraints);
        mainPanel.revalidate();
    }
}
