package cse.project.team_3.server;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

import org.json.JSONObject;

public class RequestHandler implements HttpHandler {
    private final Map<String, String> data;
    private File combinedAudioFile;

    public RequestHandler(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;
        String method = httpExchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                response = handleGet(httpExchange);
                String query = httpExchange.getRequestURI().getRawQuery();
                if (query != null && query.contains("type=recipeImage")) {
                    httpExchange.getResponseHeaders().set("Content-Type", "text/html");
                } else {
                    httpExchange.getResponseHeaders().set("Content-Type", "text/plain");
                }
            } else if (method.equals("POST")) {
                response = handlePost(httpExchange);
                // Set content type if necessary
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
                // Set content type if necessary
            } else {
                throw new Exception("Not Valid Request Method");
            }
        } catch (Exception e) {
            response = "Error processing request: " + e.getMessage();
            httpExchange.getResponseHeaders().set("Content-Type", "text/plain");
            e.printStackTrace();
        }

        byte[] responseBytes = response.getBytes("UTF-8");
        httpExchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream outStream = httpExchange.getResponseBody()) {
            outStream.write(responseBytes);
        }
    }

    private String handleGet(HttpExchange httpExchange) throws IOException {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query == null) {
            return "Invalid GET request: No query parameters provided";
        }

        // Check if the GET request is for recipe image
        if (query.contains("type=recipeImage")) {
            String recipeID = parseQueryParam(query, "id");
            if (recipeID == null) {
                return "Invalid GET request: Missing 'id' parameter for recipe image";
            }
            String combinedData = data.get(recipeID);
            if (combinedData != null && combinedData.contains(";;")) {
                String[] parts = combinedData.split(";;");
                String recipeText = parts[0];
                String imageUrl = parts.length > 1 ? parts[1] : "Image not available";
                // Generate and return HTML response
                return generateHtmlResponse(recipeText, imageUrl);
            } else {
                return "Recipe not found or image not available";
            }
        } else {
            // Handle other GET requests
            String recipeID = parseQueryParam(query, "id");
            if (recipeID == null) {
                return "Invalid GET request: Missing 'id' parameter";
            }
            String response = data.getOrDefault(recipeID, "Recipe not found");
            return response;
        }
    }

    private String generateHtmlResponse(String recipeText, String imageUrl) {
        String htmlResponse = "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Recipe</title>" +
                "<style>" +
                "  body { font-family: 'Arial', sans-serif; line-height: 1.6; margin: 0; padding: 0; background: #f4f4f4; }"
                +
                "  .container { max-width: 800px; margin: auto; padding: 20px; }" +
                "  .recipe-title { color: #333; }" +
                "  .recipe-text { background: #fff; padding: 20px; margin-bottom: 20px; border-radius: 10px; box-shadow: 0 5px 15px rgba(0,0,0,0.1); }"
                +
                "  .recipe-image { width: 100%; height: auto; border-radius: 10px; }" +
                "  .not-available { color: #cc0000; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1 class='recipe-title'>Recipe</h1>" +
                "<div class='recipe-text'>" + recipeText.replace("\n", "<br>") + "</div>" +
                (imageUrl.equals("Image not available") ? "<p class='not-available'>Image not available</p>"
                        : "<img src='" + imageUrl + "' alt='Recipe Image' class='recipe-image'>")
                +
                "</div>" +
                "</body>" +
                "</html>";
        return htmlResponse;
    }

    private String handlePut(HttpExchange httpExchange) {
        try {
            String transcriptionResult = Whisper.transcribeAudio(combinedAudioFile);
            System.out.println("Transcribed Audio: \n" + transcriptionResult);

            String temp = transcriptionResult.replace(",", ";");
            temp = temp.replace(".", ";");
            String[] recipe = temp.split(";", 2);
            String mealType = recipe[0];
            if (mealType.startsWith("B") || mealType.startsWith("b")) {
                mealType = "Breakfast";
            }
            System.out.println("Meal Type: " + mealType);
            String ingredients = recipe[1];

            String generatedRecipe = ChatGPT.generateResponse(
                    "give me a " + mealType + " recipe using " + ingredients + " without using fractions");
            System.out.println("Generated Recipe: \n" + generatedRecipe);

            String imageURL = DallE.generateImage(generatedRecipe);
            System.out.println("Generated Image URL: \n" + imageURL);

            String generatedID = generateUniqueId();

            String recipeLink = generatedRecipe + ";;" + imageURL;

            // attaching it to the local data map for testing
            data.put(generatedID, recipeLink);

            // GET here using the generated ID
            System.out.println("\nquery: " + generatedID);
            handleGet(httpExchange);

            // String retrievedRecipe = data.getOrDefault(generatedID, "Recipe not found");
            // RecipeImagePair pair = new RecipeImagePair(generatedRecipe, imageURL);
            // JSONObject pairJson = pairToJson(pair);
            return generatedRecipe + ";" + mealType.trim() + ";" + ingredients.trim();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseQueryParam(String query, String paramName) {
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
        combinedAudioFile = new File(combinedFilePath);
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
        return "POST Request handled";
    }

    private static String pairToJson(RecipeImagePair pair) {
        JSONObject json = new JSONObject();
        json.put("recipe", pair.getRecipe());
        json.put("imageURL", pair.getImageUrl());

        return json.toString();
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