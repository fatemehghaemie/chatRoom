import java.util.Scanner;
import java.util.ArrayList;
import java.net.Socket;
import java.util.List;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.io.IOException;


public class Main {
    private static int clientCounter = 1;
    private static List<Client> connectedClients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Chat Server is running on port 1234");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Client client = new Client(clientSocket,clientCounter);
                connectedClients.add(client);
                clientCounter++;
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Client extends Thread {
        private int clientId;
        private PrintWriter output;
        private Socket clientSocket;


        public Client(Socket socket, int clientId) {
            this.clientSocket = socket;
            this.clientId = clientId;
        }

        public void run() {
            try {
                Scanner input = new Scanner(clientSocket.getInputStream());

                output = new PrintWriter(clientSocket.getOutputStream(), true);

                output.println("Connected to chat server");

                while (true) {
                    if (input.hasNextLine()) {
                        String chatMessage = input.nextLine();
                        System.out.println("User" +clientId+ ": " +chatMessage);
                        broadcastMessage("User" +clientId+ ": " + chatMessage);
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        private void broadcastMessage(String message) {
            for (Client handler :connectedClients) {
                handler.output.println(message);
            }
        }
    }
}