import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Task {
    private int ID;
    private String title;
    private String description;
    private String category;
    private boolean isDone;
    private LocalDateTime dateTime;


    public Task(int ID, String title, String description, String category, boolean isDone, LocalDateTime dateTime) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isDone = isDone;
        this.dateTime = dateTime;
    }


    public Task(LocalDate date) {

        this.dateTime = LocalDateTime.of(date, LocalTime.now());
    }

    public Task() {
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }



    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }


    public String getDateTimeToString() {
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm"));
    }


    public String getDateToString() {
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }


    public String getTimeToString() {
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }


    public void setDateTimeFromString(String dt) {
        this.dateTime = LocalDateTime.parse(dt, DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm"));
    }

    public void setTime(String time) {
        this.dateTime = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")));
    }
}
