import java.io.*;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.*;
import java.util.*;


public class Lesson6NetworkingServer {
    public static void main(String[] args) throws IOException {
        String serverPort = null;
        if (args.length != 2){
            System.out.println("Must specify --port PORT");
            System.exit(1);
        }
        if (Objects.equals(args[0], "--port")){
            serverPort = args[1];
        }
        if (serverPort == null){
            System.out.println("Must specify --port PORT");
            System.exit(1);
        }

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(serverPort));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HTML);
        System.out.println("Listening for connection on port 8080 ....");
        while (true) {
            try (Socket socket = serverSocket.accept()) {
                String httpResponse = "HTTP/1.0 200 OK\r\n\r\n";
                String contentType = "Content-Type: text/html\r\n\r\n";
                socket.getOutputStream().write(httpResponse.getBytes(StandardCharsets.UTF_8));
                socket.getOutputStream().write(contentType.getBytes(StandardCharsets.UTF_8));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(HTML);
            }

        }
    }

    static final String HTML = "<html>\n<head><title>Java Networking</title></head>\n<body>\n<h1>Java Networking</h1>\n</body>\n</html>\n";
}