package cse.project.team_3;

import com.mongodb.client.*;
import org.bson.Document;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    @Test
    public void testInvalidLogin() throws IOException {
        String uri = "mongodb+srv://sminowada1:4j5atYmTK9suF0Rp@cluster0.l0dnisn.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase recipeDB = mongoClient.getDatabase("RecipeDB");
        MongoCollection<Document> recipeCollection = recipeDB.getCollection("Login");
        Document recipe = recipeCollection.find(new Document("UserFalse", "PassFalse")).first();
        assertNull(recipe);
    }

    @Test
    public void testValidLogin() throws IOException {
        String uri = "mongodb+srv://sminowada1:4j5atYmTK9suF0Rp@cluster0.l0dnisn.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase recipeDB = mongoClient.getDatabase("RecipeDB");
        MongoCollection<Document> recipeCollection = recipeDB.getCollection("Login");
        Document recipe = recipeCollection.find(new Document("TestUser", "TestPass")).first();
        assertNotNull(recipe);

    }

}
