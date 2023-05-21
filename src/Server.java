import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("shopping_list.txt"));
            StringBuilder shoppingListBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                shoppingListBuilder.append(line);
                shoppingListBuilder.append(System.lineSeparator());
            }
            String shoppingList = shoppingListBuilder.toString();
            reader.close();
            System.out.println(shoppingList);

            ServerSocket serverSocket = new ServerSocket(9000);
            System.out.println("Server listening on port " + 9000);
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(shoppingList);
                String clientInput;
                while ((clientInput = in.readLine()) != null) {
                    shoppingList = updateShoppingList(clientInput, in);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    private static String updateShoppingList(String input, BufferedReader in) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(input);
        while ((input = in.readLine()) != null){
            builder.append("\n");
            builder.append(input);
        }
        System.out.println(builder);

        BufferedWriter writer = new BufferedWriter(new FileWriter("shopping_list.txt"));
        writer.write(builder.toString());
        writer.close();
        return builder.toString();
    }
}