
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Utility {
    private final Scanner scanner;
    private final Task task;
    private final String dbName;
    private final String uname;
    private final String pwd;
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

    /**
     * Reads user input and ensures it meets a minimum length requirement.
     *
     * @return A valid user input string.
     */
    private String cleanInput() {
        String userInput = scanner.nextLine();

        if(userInput.length() < 5) {
            cleanInput();
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
            String sqlInsert = "INSERT INTO TODO (TITLE, DESCRIPTION, PRIORITY, DEADLINE, CATEGORY) VALUES (?,?,?,?,?)";
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

public void showAllTasks(){
    //            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM TODO");
//            while (resultSet.next()){
//                System.out.println(resultSet.getInt(1));
//            }
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
            case 2 -> System.out.println("view task");
            case 3 -> System.out.println("Edit task");
            case 4 -> System.out.println("Delete task");
            case 5 -> System.out.println("Exiting...");
            default -> appProcess();
        }
    }catch (InputMismatchException e){
        System.out.println(e.getMessage());

    }

}


}
