package cse.project.team_3;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class ChatGPTTest {

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