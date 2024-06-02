import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

public class Utility {
    private final Scanner scanner;
    private final Task task;

    public Utility(){
        this.task = new Task();
        this.scanner = new Scanner(System.in);
    }

    //use user interaction with resourses to safely close the resources when done
    public void userInteraction() {
        System.out.println("Type title of task");
        String title = this.cleanInput();
        System.out.println("Type description");
        String description = this.cleanInput();
        System.out.println("Task priority");
        Priorities priority = pickPriority();
        System.out.println("Task category");
        Categories category = pickCategory();
        LocalDate date = noNullDate();
        LocalTime time = noNullTime();
        LocalDateTime deadline = LocalDateTime.of(date,time);
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDeadline(deadline);
        task.setCategory(category);

    }

    //validates if the input value is longer than five characters
    private String cleanInput(){
        String userInput = scanner.nextLine();
        if(userInput.length() < 5){
            while(true){
                try{
                    System.out.println("Length must be greater than 5");
                    userInput = scanner.nextLine();
                    if(userInput.length() >= 5){
                        break;
                    }
                } catch (Exception e){
                    System.out.println("Length must be greater than 5");
                }
            }
        }
        return userInput;
    }

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

    private int pickValueInRange(int maxLengthToCheck){
        int userInput = scanner.nextInt();
        while((userInput > maxLengthToCheck) || (userInput <= 0)){
            System.out.printf("Try again, value must be > %d or <= %d%n",0, maxLengthToCheck);
            userInput = scanner.nextInt();
        }
        return userInput;
    }

    private LocalDate getDate() {
        try {
            System.out.println("Pick date of task dd/MM/yyyy");
            String date = scanner.next();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(date, dateFormat);
        } catch (Exception e){
            e.getStackTrace();
            return null;
        }
    }

    private LocalDate noNullDate(){
        while (getDate() == null){
            return getDate();
        }
        return getDate();
    }

    private LocalTime getTime() {
        try {
            System.out.println("Time of Task hh:mm");
            String time = scanner.next();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(time, timeFormatter);
        } catch (Exception e){
            e.getStackTrace();
            return null;
        }
    }

    private LocalTime noNullTime(){
        while (getTime() == null){
            return getTime();
        }
        return getTime();
    }

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
            System.out.println("Row inserted");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


}
