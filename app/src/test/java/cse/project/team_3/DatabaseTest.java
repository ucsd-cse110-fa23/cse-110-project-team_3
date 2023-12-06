package cse.project.team_3;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;

import cse.project.team_3.client.Model;

import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.eq;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

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

    @Test
    public void testNewAccountInvalid() throws IOException {
        Model model = new Model();
        assertFalse(model.createIsValid("TestUser", "TestPass"));
    }

    @Test
    public void testNewAccountValid() throws IOException {
        Model model = new Model();
        assertTrue(model.createIsValid("NewUsername", "NewPassword"));
        // delete new account so after we create it so test works every time
        String uri = "mongodb+srv://sminowada1:4j5atYmTK9suF0Rp@cluster0.l0dnisn.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase recipeDB = mongoClient.getDatabase("RecipeDB");
        MongoCollection<Document> recipeCollection = recipeDB.getCollection("NewUserName");
        recipeCollection.drop();
    }

}
