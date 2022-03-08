import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Lesson6NetworkingClient {
    public static void main(String[] args) {
        String serverAddress = null;
        String serverPort = null;
        if (args.length != 4){
            System.out.println("Must specify --server ADDRESS --port PORT");
            System.exit(1);
        }
        if (Objects.equals(args[0], "--server")){
            serverAddress = args[1];
        }
        if (Objects.equals(args[2], "--port")){
            serverPort = args[3];
        }
        if (serverPort == null || serverAddress == null){
            System.out.println("Must specify --server ADDRESS --port PORT");
            System.exit(1);
        }
        
        try (Socket s = new Socket(serverAddress,Integer.parseInt(serverPort));
             Scanner in = new Scanner(s.getInputStream(), "UTF-8")) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
