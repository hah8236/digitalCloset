import java.util.Scanner;

// The AccessCloset class allows the user to
// access their unique closet
public class Main {

    public static Closet userCloset;
    private static final Scanner console = new Scanner(System.in);


    public static void main(String[] args) {
        System.out.println("Hello, welcome to your personal digital closet.");
        System.out.print("Before we begin, please enter your name: ");
        String name = console.nextLine();
        userCloset = new Closet(name);
        System.out.println("Thank you " + name + "!");
        System.out.println("Press enter to begin working with your closet.");
        console.nextLine();
        userCloset.mainWindow();
        console.close();
    }
}
