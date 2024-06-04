import java.time.LocalDateTime;

public class Task {
    private String title;
    private String description;
    private Priorities priority;
    private LocalDateTime deadline;
    private Categories category;
    private int taskId;

    public Task(String title, String description, String priority, int taskId) {
        this.title = title;
        this.description = description;
        this.priority = StringToEnum(priority);
        this.taskId = taskId;
    }

    public Task(){

    }

    private Priorities StringToEnum(String theString){
        return Priorities.valueOf(theString);
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
