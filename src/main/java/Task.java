import java.time.LocalDateTime;

public class Task {
    private String title;
    private String description;
    private Priorities priority;
    private LocalDateTime deadline;
    private Categories category;
    private int taskId;

    public Task(String title, String description, Priorities priority, LocalDateTime deadline, Categories category, int taskId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.category = category;
        this.taskId = taskId;
    }

    public Task(){

    }

    public Task(String title, String description, Object priority, Object deadline, String category, int task_id) {
    }

    public int getTaskIdd() {
        return taskId;
    }

    public void setTask_id(int task_id) {
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
