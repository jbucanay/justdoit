import java.util.Scanner;

public class Utility {
    private static final Scanner scanner = new Scanner(System.in);
    private Task task = new Task();

    public void userInteraction() {
        System.out.println("Type title of task");
        String title = this.cleanInput();
        System.out.println(title instanceof String);
    }

    //validates if the input value is longer than five characters
    private String cleanInput(){
        while(true){
            try{
                String userInput = scanner.nextLine();
                if(userInput.length() < 5){
                    throw new Exception();
                }
                return userInput;
            } catch (Exception e){
                System.out.println("length must be greater than 5");
            }
        }
    }
}
