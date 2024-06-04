import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class Task {
    private String title;
    private String description;
    private Priorities priority;
    private LocalDateTime deadline;
    private Categories category;
    private int taskId;

    public Task(String title, String description, String priority, String category,  int taskId,String deadline) {
        this.title = title;
        this.description = description;
        this.priority = stringToPriorityEnum(priority);
        this.category = stringToCategoryEnum(category);
        this.deadline = stringToDate(deadline);
        this.taskId = taskId;
    }

    public Task(){

    }


    private LocalDateTime stringToDate(String theString){
        String[] twoArray = theString.split(" ");
        LocalDate date = LocalDate.parse(twoArray[0]);
        LocalTime time = LocalTime.parse(twoArray[1].substring(0,5));
        return LocalDateTime.of(date,time);
    }

    private Priorities stringToPriorityEnum(String theString) {
        return Priorities.valueOf(theString);
    }

    private Categories stringToCategoryEnum(String theString) {
        return Categories.valueOf(theString);
    }


    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int task_id) {
        this.taskId = task_id;
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

    public Priorities getPriority() {
        return priority;
    }

    public void setPriority(Priorities priority) {
        this.priority = priority;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}
