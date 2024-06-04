
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Utility {
    private final Scanner scanner;
    private final Task task;
    private final String dbName;
    private final String uname;
    private final String pwd;

    private Collection<Task> taskCollection;
    private final DatabaseConnectionManager dcm;

    public Utility(){
        this.task = new Task();
        this.scanner = new Scanner(System.in);
        this.dbName = System.getenv("POSTGRES_DB");
        this.uname = System.getenv("POSTGRES_UNM");
        this.pwd = System.getenv("POSTGRES_PSWD");
        this.dcm = new DatabaseConnectionManager("localhost",this.dbName, this.uname , this.pwd, "5432");
    }

    /**
     * Prints a formatted string with asterisks and an optional prompt.
     *
     * @param ask The string to be printed.
     * @param typeOrChoice If 1, prints "Type: " after the string.
     */
    private void formattedAskString(String ask, int typeOrChoice) {
        String star = "*";
        String askStr = "* " + ask + " *";
        int sizeOfAsk = askStr.length();
        System.out.println(star.repeat(sizeOfAsk));
        System.out.printf("%s%n", askStr);
        System.out.println(star.repeat(sizeOfAsk));
        if (typeOrChoice == 1) {
            System.out.print("Type: ");
        }
    }

    /**
     * Interacts with the user to gather and set task details.
     */
    public void addTasks() {
        formattedAskString("Task title", 1);
        String title = this.cleanInput();
        formattedAskString("Task description", 1);
        String description = this.cleanInput();
        formattedAskString("Task priority", 2);
        Priorities priority = pickPriority();
        formattedAskString("Task category", 2);
        Categories category = pickCategory();
        LocalDate date = getDate();
        LocalTime time = getTime();
        LocalDateTime deadline = LocalDateTime.of(date,time);
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDeadline(deadline);
        task.setCategory(category);
        try(Connection connection = this.dcm.getConnection()) {
           this.rowInsert(connection);
        } catch (SQLException e){
            e.printStackTrace();
        }
        appProcess();
    }

    public void viewTasks(){
        try(Connection connection = this.dcm.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM TASK");
            this.taskCollection = new LinkedList<>();
            while (resultSet.next()){
                this.taskCollection.add(new Task(resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getString("priority"),
                        resultSet.getString("category"),
                        resultSet.getInt("task_id"),
                        resultSet.getString("deadline")
                        ));
            }
            int viewingManager;
            do{
                viewingManager = viewingTasks();
                switch (viewingManager){
                    case 1 -> allTasksFormatted(this.taskCollection);
                    case 2 -> System.out.println("sort by title check if already in desc then do asc");
                    case 3 -> System.out.println("sort by deadline, create separate functions to handle it");
                    case 4 -> System.out.println("sort by priority");
                    default -> System.out.println("Exiting...");
                }
            } while (viewingManager !=5);

        } catch (SQLException | NumberFormatException e){
            System.out.println("Exiting... fix this with parse int and while loop | recursion");
        }
        appProcess();
    }

    private void allTasksFormatted(Collection<Task> tasks){
        try {
            tasks.forEach(t -> {
                System.out.printf("Title: %s%n",t.getTitle().substring(0,1).toUpperCase() + t.getTitle().substring(1));
                System.out.printf("Desc: %s%n", t.getDescription().substring(0,1).toUpperCase() + t.getDescription().substring(1));
                System.out.printf("Priority: %s%n", t.getPriority().name().replace("_", " "));
                System.out.printf("Category: %s%n", t.getCategory().name().replace("_", " "));
                System.out.printf("Due: %s%n", t.getDeadline());
                System.out.println(" ");
            });

        } catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
    }

    private void sortTaskByTitle(Collection<Task> tasks){

    }

    /**
     * Reads user input and ensures it meets a minimum length requirement.
     *
     * @return A valid user input string.
     */
    private String cleanInput() {
        String userInput = scanner.nextLine();

        if(userInput.length() < 5) {
            System.out.println("String length must be >= 5");
            //using recursion for funsies
            return cleanInput();
        }

        return userInput;
    }


    /**
     * Prompts the user to pick a category from a list of available categories.
     *
     * @return The selected category.
     */
    private Categories pickCategory(){
        int i = 1;
        for(Categories category: Categories.values()){
            System.out.printf("Type %d - %s%n", i, category);
            i++;
        }
        System.out.print("Choice: ");
        int userInput = pickValueInRange(Categories.values().length);
        return Categories.values()[userInput -1];
    }

    /**
     * Prompts the user to pick a category from a list of available priorities.
     *
     * @return The selected priority.
     */

    private Priorities pickPriority(){
        int i = 1;
        for(Priorities priority: Priorities.values()){
            System.out.printf("Type %d - %s%n", i, priority);
            i++;
        }
        System.out.print("Choice: ");
        int userInput = this.pickValueInRange(Priorities.values().length);
        return Priorities.values()[userInput -1];

    }

    /**
     * Prompts the user to pick a value within a specified range.
     *
     * @param maxLengthToCheck The maximum valid value.
     * @return A valid user input within the range.
     */
    private int pickValueInRange(int maxLengthToCheck){
        int userInput = Integer.parseInt(scanner.nextLine());
        while((userInput > maxLengthToCheck) || (userInput <= 0)){
            System.out.printf("Try again, value must be > %d or <= %d%n",0, maxLengthToCheck);
            userInput = Integer.parseInt(scanner.nextLine());;
        }
        return userInput;
    }

    /**
     * Prompts the user to input a date for the task in the format dd/MM/yyyy.
     * If the input is invalid, it recursively calls itself until a valid date is entered.
     *
     * @return The selected date.
     */
    private LocalDate getDate() {
        try {
            System.out.println("Pick date of task dd/MM/yyyy");
            String date = scanner.nextLine();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(date, dateFormat);
        } catch (Exception e){
            e.getStackTrace();
            return getDate();
        }
    }

    /**
     * Prompts the user to input a time for the task in the format HH:mm.
     * If the input is invalid, it recursively calls itself until a valid time is entered.
     *
     * @return The selected time.
     */
    private LocalTime getTime() {
        try {
            System.out.println("Time of Task hh:mm");
            String time = scanner.nextLine();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(time, timeFormatter);
        } catch (Exception e){
            e.getStackTrace();
            return getTime();
        }
    }


    /**
     * Inserts a task into the db table in the database.
     * The task details (title, description, priority, deadline, category) are retrieved from the task object.
     *
     * @param connection The database connection.
     */
    public void rowInsert(Connection connection) {
        try{
            String sqlInsert = "INSERT INTO TASK (TITLE, DESCRIPTION, PRIORITY, DEADLINE, CATEGORY) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
            preparedStatement.setString(1, this.task.getTitle());
            preparedStatement.setString(2, this.task.getDescription());
            preparedStatement.setString(3, this.task.getPriority().name());
            preparedStatement.setObject(4, this.task.getDeadline());
            preparedStatement.setString(5, this.task.getCategory().name());
            preparedStatement.execute();
            System.out.println("Row inserted!");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }



public void appProcess(){
    try {
        formattedAskString("Pick choice", 2);
        System.out.println("""
                Type 1: Add Tasks
                Type 2: View Tasks
                Type 3: Edit Task
                Type 4: Delete task
                Type 5: Exit""");
        System.out.print("Choice: ");
        int interactWith = Integer.parseInt(scanner.nextLine());
        switch (interactWith){
            case 1 -> addTasks();
            case 2 -> viewTasks();
            case 3 -> System.out.println("Edit task");
            case 4 -> System.out.println("Delete task");
            case 5 -> System.out.println("Exiting...");
            default -> appProcess();
        }
    }catch (InputMismatchException e){
        System.out.println(e.getMessage());

    }

}

private int viewingTasks(){
    try {
        formattedAskString("Pick how to view your tasks", 2);
        System.out.println("""
                Type 1: View all tasks
                Type 2: Sort tasks by title
                Type 3: Sort tasks by deadline
                Type 4: Sort task by priority
                Type 5: Exit""");
        System.out.print("Choice: ");
        int interactWith = Integer.parseInt(scanner.nextLine());
        return switch (interactWith){
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> 4;
            default ->  5;
        };
    }catch (InputMismatchException e){
        System.out.println(e.getMessage());

    }

    return 0;
}


}
