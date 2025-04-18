package TestApiToDo;

import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static TestApiToDo.BaseTest.EnumHttpStatus.*;
import static config.Constants.ROOT_URL;
import static config.Constants.TODO_URL;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FunctionalBackendTests extends BaseTest {

    @Test
    @Order(1)
    public void testRootResourcePostMethodIncorrectUsage() {
        logger.info("Executing test with incorrect HTTP Method for Root Resource Access");
        Response response =
                given()
                        .when()
                        .post(ROOT_URL)
                        .then()
                        .extract()
                        .response();

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(NOT_ALLOWED.getStatusCode(), response.getStatusCode());
        assertEquals(NOT_ALLOWED.getDetailMessage(), detailMessage);
        logger.info("Finished test incorrect HTTP Method for Root Resource Access");
    }

    @Test
    @Order(2)
    public void testAddResourceWithExistingID() {
        logger.info("Executing test for creating a resource with an already existing ID");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "1");
        jsonObject.addProperty("item", "Read a book.");
        String jsonString = jsonObject.toString();

        Response response = requestSpecification
                .body(jsonString)
                .post(TODO_URL);

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(CONFLICT.getStatusCode(), response.getStatusCode());
        assertEquals(CONFLICT.getDetailMessage(), detailMessage);
        assertEquals("Resource already exists", detailMessage, "Expected 'Resource already exists' message");
        logger.info("Finished test for creating resource with existing ID");
    }

    @Test
    @Order(3)
    public void testAddNewDataWithoutBody() {
        logger.info("Executing test for adding new resource with missing required fields.");

        JsonObject jsonObject = new JsonObject();
        String jsonString = jsonObject.toString();

        Response response = requestSpecification
                .body(jsonString)
                .post(TODO_URL);

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(UNPROCESSABLE.getStatusCode(), response.getStatusCode());
        assertEquals(UNPROCESSABLE.getDetailMessage(), detailMessage);
        logger.info("Finished test for adding new resource with missing required fields.");
    }

    @Test
    @Order(4)
    public void testAddNewDataWithIncorrectData() {
        logger.info("Executing test for adding new resource with incorrect data. Syntax error.");

        JsonObject jsonObject = new JsonObject();
        JsonObject idObject = new JsonObject();
        idObject.addProperty("1", 1);
        jsonObject.add("id", idObject);
        jsonObject.addProperty("todo", "");
        String jsonString = jsonObject.toString();

        Response response = requestSpecification
                .body(jsonString)
                .post(TODO_URL);

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(UNPROCESSABLE.getStatusCode(), response.getStatusCode());
        assertEquals(UNPROCESSABLE.getDetailMessage(), detailMessage);
        logger.info("Finished test for adding new resource with incorrect data. Syntax error.");
    }

    @Test
    @Order(5)
    public void testOfAPutRequestAtAnEndpointWithoutSupport() {
        logger.info("Executing test for creating a resource processing of a PUT request at an endpoint without support.");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "6");
        jsonObject.addProperty("item", "jogging");
        String jsonString = jsonObject.toString();

        Response response = requestSpecification
                .body(jsonString)
                .put(TODO_URL);

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(NOT_ALLOWED.getStatusCode(), response.getStatusCode());
        assertEquals(NOT_ALLOWED.getDetailMessage(), detailMessage);
        logger.info("Finished test for creating a resource processing of a PUT request at an endpoint without support.");
    }

    @Test
    @Order(6)
    public void testOfUpdatingResourceWithANonExistentIdentifier() {
        logger.info("Executing a test to update a resource with a non-existent identifier.");
        int nonExistentIDtoUpdate = 999;
        String itemDescription = "love jogging";

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", nonExistentIDtoUpdate);
        jsonObject.addProperty("item", itemDescription);
        String jsonString = jsonObject.toString();

        Response response = requestSpecification
                .body(jsonString)
                .put(TODO_URL + "/" + nonExistentIDtoUpdate);

        logResponseDetails(response);

        assertEquals(NOT_FOUND.getStatusCode(), response.getStatusCode());
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);
        assertEquals("Todo with id 999 not found.", detailMessage, "Expected todo with id 999 not found.");
        logger.info("Finished test to update a resource with a non-existent identifier.");
    }

    @Test
    @Order(7)
    public void testDeletingResourceWithANonExistentIdentifier() {
        logger.info("Executing test to delete a resource with a non-existent identifier.");

        int nonExistentID = 800;

        Response response = requestSpecification
                .delete(TODO_URL + "/" + nonExistentID);

        logResponseDetails(response);
        assertEquals(CONFLICT.getStatusCode(), response.getStatusCode());
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);
        assertEquals("Todo with id 800 not found.", detailMessage, "Expected todo with id 800 not found.");
        logger.info("Finished test to delete a resource with a non-existent identifier.");
    }
}
