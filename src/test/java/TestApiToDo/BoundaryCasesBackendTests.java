package TestApiToDo;

import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static TestApiToDo.BaseTest.EnumHttpStatus.*;
import static config.Constants.TODO_URL;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoundaryCasesBackendTests extends BaseTest {

    @Test
    @Order(1)
    public void testMaxItemLength() {

        logger.info("Executing test to find the maximum length of the item description");

        int maxLengthOfDetailTodo = 1000;
        int iterationStep = 10;
        boolean foundLimit = false;

        for (int lengthOfDetailTodo = iterationStep; lengthOfDetailTodo <= maxLengthOfDetailTodo; lengthOfDetailTodo += iterationStep) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", "6");
            jsonObject.addProperty("item", "drink water".repeat(lengthOfDetailTodo));

            Response response = given(requestSpecification)
                    .body(jsonObject.toString())
                    .post(TODO_URL);

            logResponseDetails(response);

            if (response.getStatusCode() == CONFLICT.getStatusCode()) {
                String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);
                foundLimit = true;
                logger.info("Limit found at length: {} with message: {}", lengthOfDetailTodo, detailMessage);
                break;
            } else {
                assertEquals(200, response.getStatusCode(), "Expected success status code 200 for length: " + lengthOfDetailTodo);
            }
        }

        if (!foundLimit) {
            logger.warn("No conflict detected within the specified length range up to {}", maxLengthOfDetailTodo);
        }
        logger.info("Finished for finding the maximum length of item description");
    }

    @Test
    @Order(2)
    public void testMinItemLength() {
        logger.info("Executing test for minimum length of item description");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", "7");
        jsonObject.addProperty("item", "");
        String jsonString = jsonObject.toString();

        Response response = requestSpecification
                .body(jsonString)
                .post(TODO_URL);

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(SUCCESSFUL.getStatusCode(), response.getStatusCode());
        assertEquals(SUCCESSFUL.getDetailMessage(), detailMessage);
        logger.info("Finished test for creating resource with existing ID");
    }

    @Test
    @Order(3)
    public void testDeletingResourceWithAMinimumPossibleIdentifier() {
        logger.info("Executing test to delete a resource with minimum possible identifier.");

        Response response = requestSpecification
                .delete(TODO_URL + "/1");

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(SUCCESSFUL.getStatusCode(), response.getStatusCode());
        assertEquals(SUCCESSFUL.getDetailMessage(), detailMessage);
        logger.info("Finished test to delete a resource with a minimum possible identifier");
    }

    @Test
    @Order(4)
    public void testDeletingResourceWithAMaximumPossibleIdentifier() {
        logger.info("Executing test to delete a resource with maximum possible identifier.");

        Response response = requestSpecification
                .delete(TODO_URL + "/7");

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(SUCCESSFUL.getStatusCode(), response.getStatusCode());
        assertEquals(SUCCESSFUL.getDetailMessage(), detailMessage);
        logger.info("Finished test to delete a resource with a maximum possible identifier");
    }
}
