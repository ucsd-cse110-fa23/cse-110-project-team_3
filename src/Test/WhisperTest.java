package Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

import app.Whisper;

public class WhisperTest {

    @Test
    public void testTranscribeAudio() {
        File audioFile = new File("./src/Test/recording.wav");

        try {
            String transcription = Whisper.transcribeAudio(audioFile);

            // Ensure that the transcription is not null or empty
            assertNotNull(transcription);
            assertFalse(transcription.isEmpty());

            // You can add more specific assertion checks based on your use case
        } catch (Exception e) {
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    @Test
    public void testHandleSuccessResponse() {
        // Create a mock success response
        String successResponse = "{\"text\": \"This is a test transcription\"}";

        try {
            // Modify this test to provide a HttpURLConnection with a mock input stream
            HttpURLConnection mockConnection = createMockConnection(successResponse);

            String transcription = Whisper.handleSuccessResponse(mockConnection);

            // Ensure that the transcription is as expected
            assertEquals("This is a test transcription", transcription);
        } catch (Exception e) {
            fail("An exception was thrown: " + e.getMessage());
        }
    }

    // @Test
    // public void testHandleErrorResponse() {
    // // Create a mock error response
    // String errorResponse = "Error: Something went wrong";

    // try {
    // // Modify this test to provide a HttpURLConnection with a mock error stream
    // HttpURLConnection mockConnection = createMockConnection(errorResponse);

    // String result = Whisper.handleErrorResponse(mockConnection);

    // // Ensure that the result is an empty string
    // assertTrue(result.isEmpty());
    // } catch (Exception e) {
    // fail("An exception was thrown: " + e.getMessage());
    // }
    // }

    // Create a mock HttpURLConnection with a provided response
    private HttpURLConnection createMockConnection(String response) throws IOException {
        return new HttpURLConnection(new URL("https://example.com")) {
            @Override
            public void disconnect() {
                // Do nothing
            }

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {
                // Do nothing
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public int getResponseCode() throws IOException {
                return HttpURLConnection.HTTP_OK;
            }
        };
    }
}