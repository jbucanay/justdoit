import java.util.Scanner;

public class Utility {
    private static final Scanner scanner = new Scanner(System.in);
    private Task task = new Task();

    public static void userInteraction(){
        System.out.println("Type title of task");
        String title = scanner.nextLine();
        System.out.println(title);
    }
}
