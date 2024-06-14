//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 1234);// کلاینت
            Scanner input = new Scanner(socket.getInputStream());

            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            Scanner userInput = new Scanner(System.in);

            Thread messageListener = new Thread(() -> {
                try {
                    while (true) {
                        String chatMessage = input.nextLine();
                        System.out.println(chatMessage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            messageListener.start();

            while (true) {
                String chatMessage = userInput.nextLine();
                output.println(chatMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}