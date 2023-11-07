package Test;

import app.ChatGPT;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

public class ChatGPTTests {

    @Test
    public void testGenerateResponse() {
        String prompt = "Give me a recipe with milk";

        try {
            String response = ChatGPT.generateResponse(prompt);

            // Ensure that the response is not null or empty
            assertNotNull(response);
            assertFalse(response.isEmpty());
        } catch (Exception e) {
            fail("An exception was thrown: " + e.getMessage());
        }
    }
}
