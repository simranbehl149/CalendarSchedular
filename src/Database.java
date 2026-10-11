import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private String url = "jdbc:mysql://localhost:3306/calendar";
    private String user = "root";
    private String pass = "Simran123";
    private Connection connection;

    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void createDatabase() {
        String createTaskTable = "CREATE TABLE IF NOT EXISTS task (" +
                "ID BIGINT NOT NULL AUTO_INCREMENT, " +
                "title VARCHAR(255) NOT NULL, " +
                "description VARCHAR(255), " +
                "category VARCHAR(255), " +
                "isDone BOOLEAN NOT NULL, " +
                "date VARCHAR(255) NOT NULL, " +
                "time VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (ID))";

        try (PreparedStatement pstmt = connection.prepareStatement(createTaskTable)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String createQuoteTable = "CREATE TABLE IF NOT EXISTS quotes (" +
                "ID BIGINT NOT NULL AUTO_INCREMENT, " +
                "quote VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (ID))";

        try (PreparedStatement pstmt = connection.prepareStatement(createQuoteTable)) {
            pstmt.executeUpdate();
            loadQuotes(); // Load quotes from the file into the database
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String createNotesTable = "CREATE TABLE IF NOT EXISTS notes (" +
                "ID BIGINT NOT NULL AUTO_INCREMENT, " +
                "date VARCHAR(255) NOT NULL, " +
                "note VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (ID))";

        try (PreparedStatement pstmt = connection.prepareStatement(createNotesTable)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load quotes from a file and insert them into the database
    public void loadQuotes() {
        File file = new File("CalendarScheduler-master/database/quotes.txt");
        if (file.isFile()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String quote;
                while ((quote = br.readLine()) != null) {
                    quote = quote.replace("'", "''"); // Escape single quotes
                    String insert = "INSERT INTO quotes(quote) VALUES (?)";
                    try (PreparedStatement pstmt = connection.prepareStatement(insert)) {
                        pstmt.setString(1, quote);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Task> getTasks(String date) {
        ArrayList<Task> tasks = new ArrayList<>();
        String select = "SELECT * FROM task WHERE date = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(select)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Task t = new Task();
                t.setID(rs.getInt("ID"));
                t.setTitle(rs.getString("title"));
                t.setDescription(rs.getString("description"));
                t.setCategory(rs.getString("category"));
                t.setDone(rs.getBoolean("isDone"));
                t.setDateTimeFromString(rs.getString("date") + " | " + rs.getString("time"));
                tasks.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public boolean hasTasks(String date) {
        boolean hasEvents = false;
        String select = "SELECT * FROM task WHERE date = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(select)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            hasEvents = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasEvents;
    }

    public void createTask(Task t) {
        String insert = "INSERT INTO task(title, description, category, isDone, date, time) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insert)) {
            pstmt.setString(1, t.getTitle().replace("'", "''"));
            pstmt.setString(2, t.getDescription().replace("'", "''"));
            pstmt.setString(3, t.getCategory());
            pstmt.setInt(4, t.isDone() ? 1 : 0);
            pstmt.setString(5, t.getDateToString());
            pstmt.setString(6, t.getTimeToString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(Task t) {
        String update = "UPDATE task SET title=?, description=?, category=?, isDone=?, date=?, time=? WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(update)) {
            pstmt.setString(1, t.getTitle().replace("'", "''"));
            pstmt.setString(2, t.getDescription().replace("'", "''"));
            pstmt.setString(3, t.getCategory());
            pstmt.setInt(4, t.isDone() ? 1 : 0);
            pstmt.setString(5, t.getDateToString());
            pstmt.setString(6, t.getTimeToString());
            pstmt.setInt(7, t.getID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(int ID) {
        String delete = "DELETE FROM task WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(delete)) {
            pstmt.setInt(1, ID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getQuote(int day) {
        String select = "SELECT quote FROM quotes WHERE ID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(select)) {
            pstmt.setInt(1, day);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("quote");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean hasNotes(String date) {
        boolean hasNotes = false;
        String select = "SELECT * FROM notes WHERE date = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(select)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            hasNotes = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasNotes;
    }


    public String getNotes(String date) {
        String select = "SELECT note FROM notes WHERE date = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(select)) {
            pstmt.setString(1, date);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("note");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void createNotes(String date, String note) {
        note = note.replace("'", "''");
        String insert = "INSERT INTO notes(date, note) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insert)) {
            pstmt.setString(1, date);
            pstmt.setString(2, note);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNotes(String date, String note) {
        note = note.replace("'", "''");
        String update = "UPDATE notes SET note=? WHERE date = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(update)) {
            pstmt.setString(1, note);
            pstmt.setString(2, date);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Database db = new Database();
        db.createDatabase();
    }
}
