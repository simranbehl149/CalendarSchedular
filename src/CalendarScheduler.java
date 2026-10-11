import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDate;


public class CalendarScheduler {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Calendar Scheduler");
        ImageIcon img = new ImageIcon("CalendarScheduler-master/resources/calendar-icon.png");
        frame.setIconImage(img.getImage());
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.WHITE);


        Database database = new Database();

        Home homePanel = new Home(frame, database, LocalDate.now());
        frame.getContentPane().add(homePanel);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                database.closeConnection();
                System.exit(0);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        frame.setVisible(true);
    }
}
