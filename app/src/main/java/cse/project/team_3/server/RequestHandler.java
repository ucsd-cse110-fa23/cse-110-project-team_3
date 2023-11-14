package cse.project.team_3.server;

import com.sun.net.httpserver.*;
import cse.project.team_3.Whisper;
import java.io.*;
import java.net.*;
import java.util.*;


public class RequestHandler implements HttpHandler {
    private final Map<String, String> data;

    public RequestHandler(Map<String, String> data) {
    this.data = data;
    }

    @Override
    public void handle(HttpExchange  httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Sending back response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream outStream = httpExchange.getResponseBody();
        outStream.write(response.getBytes());
        outStream.close();
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String recipeID = parseQueryParam(query, "id");
            return data.getOrDefault(recipeID, "Recipe not found");
        } else {
            return "Invalid GET request";
        }
    }

    private String parseQueryParam(String query, String paramName) {
        // Implement logic to parse query parameters
        // This is a basic example, you may need to adapt it based on your requirements
        // Example: query = "id=123&name=recipe"
        // Return value for paramName = "id" is "123"
        // Return value for paramName = "name" is "recipe"
        // Return null if paramName is not found in the query
        // ...
        return null;
    }

    private String handlePost(HttpExchange httpExchange) throws IOException {
       try {
        // Get the boundary from the content-type header
        Headers headers = httpExchange.getRequestHeaders();
        String contentType = headers.getFirst("Content-Type");
        String boundary = extractBoundary(contentType);

        // Process the multipart/form-data request
        MultipartFormDataHandler formDataHandler = new MultipartFormDataHandler(httpExchange.getRequestBody(), boundary);

        // Get the audio file from the form data
        InputStream audioStream = formDataHandler.getFile("file");
        
        // Save the audio file to a temporary location
        String currentDirectory = System.getProperty("user.dir");
        String audioFilePath = currentDirectory + File.separator + "audiofile.wav";
        saveInputStreamToFile(audioStream, audioFilePath);

        // Call Whisper to transcribe the audio file
        String transcriptionResult = Whisper.transcribeAudio(new File(audioFilePath));

        // Return the transcription result as the response
        return transcriptionResult;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing the request";
        }
    }

    private String extractBoundary(String contentType) {
        // Extract the boundary from the content-type header
        String[] elements = contentType.split(";");
        for (String element : elements) {
            element = element.trim();
            if (element.startsWith("boundary=")) {
                return element.substring("boundary=".length());
            }
        }
        return null;
    }

    private void saveInputStreamToFile(InputStream inputStream, String filePath) throws FileNotFoundException, IOException {
        // Save the input stream to a file
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
    }
}
