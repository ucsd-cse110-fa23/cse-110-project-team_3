package cse.project.team_3.server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;

public class DallE {

    // Set URL of API Endpoint
    private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
    private static final String API_KEY = "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"; // this key is from CSE 110 shared organization
    private static final String MODEL = "dall-e-2";
    // previous key used in ChatGPT.java and Whisper.java: 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                                        

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java DallE <prompt>");
            return;
        }

        String prompt = args[0];
    }

    public static String generateImage(String prompt) throws Exception {
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
                HttpResponse.BodyHandlers.ofString());

        // Return generated image URL
        JSONObject responseJson = new JSONObject(response.body());
        System.out.println(responseJson.getJSONArray("data").getJSONObject(0).getString("url"));
        return responseJson.getJSONArray("data").getJSONObject(0).getString("url");
    }
}