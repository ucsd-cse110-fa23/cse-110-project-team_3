package cse.project.team_3.server;

import com.google.common.io.Files;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class RequestHandler implements HttpHandler {
    private final Map<String, String> data;

    public RequestHandler(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
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

    private String handlePut(HttpExchange httpExchange) {
        return "PUT request handled";
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        String response = "Invalid GET request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {

            // Get the data from the map
            String recipeID = parseQueryParam(query, "id");

            // Check if the recipeID is not null
            if (recipeID != null) {
                // Retrieve the recipe based on the ID from the data map
                String recipe = data.getOrDefault(recipeID, "Recipe not found");
                return recipe;
            } else {
                return "Invalid GET request: Missing 'id' parameter";
            }
        } else {
            return "Invalid GET request";
        }
    }

    private String parseQueryParam(String query, String paramName) {
        if (query == null || paramName == null) {
            return null;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length > 1 && keyValue[0].equals(paramName)) {
                return keyValue[1];
            }
        }
        return null;
    }

    /*
     * public void processAudioFiles() {
     * boolean serverReadyForGet = true;
     * }
     */

    // Generate Unique ID For each recipe
    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public String handlePost(HttpExchange t) throws IOException {
        String CRLF = "\r\n";
        int fileSize = 0;

        String currentDirectory = System.getProperty("user.dir");
        String combinedFilePath = currentDirectory + File.separator + "combinedAudio.wav";
        File combinedAudioFile = new File(combinedFilePath);
        if (!combinedAudioFile.exists()) {
            return "Combined audio file not available";
        }

        InputStream input = t.getRequestBody();
        String nextLine = "";
        do {
            nextLine = readLine(input, CRLF);
            if (nextLine.startsWith("Content-Length:")) {
                fileSize = Integer.parseInt(
                        nextLine.replaceAll(" ", "").substring(
                                "Content-Length:".length()));
            }
            System.out.println(nextLine);
        } while (!nextLine.equals(""));

        byte[] wavFileByteArray = new byte[fileSize];
        int readOffset = 0;
        while (readOffset < fileSize) {
            int bytesRead = input.read(wavFileByteArray, readOffset, fileSize);
            readOffset += bytesRead;
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(combinedAudioFile));

        bos.write(wavFileByteArray, 0, fileSize);
        bos.flush();

        t.sendResponseHeaders(200, 0);
        try {
            String transcriptionResult = Whisper.transcribeAudio(combinedAudioFile);
            System.out.println("Transcribed Audio: \n" + transcriptionResult);
            String generatedRecipe = ChatGPT.generateResponse(transcriptionResult);
            System.out.println("Generated Recipe: \n" + generatedRecipe);

            String generatedID = generateUniqueId();

            // attaching it to the local data map for testing
            data.put(generatedID, generatedRecipe);
    
            // GET here using the generated ID
            System.out.println("\nquery: " + generatedID);
            handleGet(t);
            
            String retrievedRecipe = data.getOrDefault(generatedID, "Recipe not found");
            return retrievedRecipe;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "Error processing the request";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing the request";
        }
    }

    private static String readLine(InputStream is, String lineSeparator)
            throws IOException {

        int off = 0, i = 0;
        byte[] separator = lineSeparator.getBytes("UTF-8");
        byte[] lineBytes = new byte[1024];

        while (is.available() > 0) {
            int nextByte = is.read();
            if (nextByte < -1) {
                throw new IOException(
                        "Reached end of stream while reading the current line!");
            }

            lineBytes[i] = (byte) nextByte;
            if (lineBytes[i++] == separator[off++]) {
                if (off == separator.length) {
                    return new String(
                            lineBytes, 0, i - separator.length, "UTF-8");
                }
            } else {
                off = 0;
            }

            if (i == lineBytes.length) {
                throw new IOException("Maximum line length exceeded: " + i);
            }
        }

        throw new IOException(
                "Reached end of stream while reading the current line!");
    }
}
