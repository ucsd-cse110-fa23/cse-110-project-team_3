package cse.project.team_3.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Paths;
import java.nio.file.Files;


public class ClientDallE {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations"; 
    private static final String API_KEY = "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";
    private static final String MODEL = "dall-e-2";

    public static String generateImage(String prompt) throws IOException, InterruptedException, URISyntaxException {
        // Set request parameters
        int n = 1;

        // Create a request body which you will pass into request object
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("prompt", prompt);
        requestBody.put("n", n);
        requestBody.put("size", "256x256");


        // Create the HTTP client
        HttpClient client = HttpClient.newHttpClient();


        // Create the request object
        HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", API_KEY))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();


        // Send the request and receive the response
        HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString()
        );


        // Process the response
        String responseBody = response.body();


        JSONObject responseJson = new JSONObject(responseBody);
        
        String generatedImageURL = responseJson.getJSONArray("data").getJSONObject(0).getString("url");
        
        System.out.println("DALL-E Response:");
        System.out.println(generatedImageURL);
        return generatedImageURL;

        // Download the Generated Image to Current Directory
        // try(
        //     InputStream in = new URI(generatedImageURL).toURL().openStream()
        // )
        // {
        //     Files.copy(in, Paths.get("image.jpg"));
        // }
    }
}
