import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleHttpServer {

    public static void main(String[] args) throws Exception {
        // Създаваме сървър на порт 8050
        HttpServer server = HttpServer.create(new InetSocketAddress(8050), 0);
        
        // Пътя към директорията с файловете
        String folderPath = "D:/v1/xml";

        // Създаваме обработчик за заявките
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String requestURI = exchange.getRequestURI().toString();
                String filePath = folderPath + requestURI;

                // Проверяваме дали файлът съществува и го зареждаме
                if (Files.exists(Paths.get(filePath))) {
                    byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
                    exchange.sendResponseHeaders(200, fileContent.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(fileContent);
                    os.close();
                } else {
                    // Ако файлът не съществува, връщаме 404
                    String response = "File not found!";
                    exchange.sendResponseHeaders(404, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        });

        // Стартираме сървъра
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }
}
