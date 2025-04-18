package TestApiToDo;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static TestApiToDo.BaseTest.EnumHttpStatus.*;
import static config.Constants.ROOT_URL;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorizationBackendTests extends BaseTest {

    @Test
    @Order(1)
    public void testRootEndpointUnauthorized() {
        logger.info("Executing GET root test without authorization");

        Response response =
                given()
                        .when()
                        .get(ROOT_URL)
                        .then()
                        .extract()
                        .response();

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(UNAUTHORIZED.getStatusCode(), response.getStatusCode());
        assertEquals(UNAUTHORIZED.getDetailMessage(), detailMessage);
        logger.info("Finished test GET root test without authorization");
    }

    @Test
    @Order(2)
    public void testRootEndpointAuthorized() {
        logger.info("Executing GET root test with authorization");

        Response response =
                requestSpecification // Use the request specification from BaseTest
                        .when()
                        .get(ROOT_URL)
                        .then()
                        .extract()
                        .response();

        logResponseDetails(response);
        String message = response.jsonPath().getString("message");

        assertEquals(SUCCESSFUL.getStatusCode(), response.getStatusCode());
        assertEquals("Welcome to your todo list foo", message);
        logger.info("Finished test GET root test with authorization");
    }

    @Test
    @Order(3)
    public void testRootEndpointAuthorizedWithIncorrectData() {
        logger.info("Executing GET root test with incorrect authorization data");

        String username = "user";
        String password = "pass";

        Response response =
                given()
                        .auth()
                        .basic(username, password)
                        .when()
                        .get(ROOT_URL)
                        .then()
                        .extract()
                        .response();

        logResponseDetails(response);
        String detailMessage = response.jsonPath().getString(DETAIL_FIELD_NAME);

        assertEquals(FORBIDDEN.getStatusCode(), response.getStatusCode());
        assertEquals(FORBIDDEN.getDetailMessage(), detailMessage);
        logger.info("Finished test with incorrect authorization data");
    }
}
