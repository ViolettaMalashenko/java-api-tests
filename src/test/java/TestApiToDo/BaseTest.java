package TestApiToDo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class BaseTest {

    protected static final String BASE_URL = "http://localhost:8080/";
    protected RequestSpecification requestSpecification;
    public static final String DETAIL_FIELD_NAME = "detail";
    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);


    @BeforeEach
    void setUp() {
        String username = System.getenv("USER_NAME");
        String password = System.getenv("PASSWORD");

        // Setup a common request specification
        requestSpecification = RestAssured.given()
                .baseUri(BASE_URL)
                .auth()
                .basic(username, password)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    // Setup a log response details
    protected void logResponseDetails(Response response) {
        logger.info("Current Time: {}", LocalDateTime.now());
        logger.info("Response Code: {}", response.getStatusCode());
        logger.info("Response Body: {}", response.getBody().asString());
    }

    public enum EnumHttpStatus {
        UNAUTHORIZED(401, "Not authenticated"),
        SUCCESSFUL(200, "OK"),
        FORBIDDEN(403, "Incorrect email or password"),
        CONFLICT(409, "Conflict"),
        NOT_FOUND(404, "Resource not found"),
        NOT_ALLOWED(405, "Method Not Allowed"),
        UNPROCESSABLE(422, "Unprocessable Entity");

        private final int code;
        private final String detailMessage;

        EnumHttpStatus(int code, String detailMessage) {
            this.code = code;
            this.detailMessage = detailMessage;
        }

        public int getStatusCode() {
            return code;
        }

        public String getDetailMessage() {
            return detailMessage;
        }
    }
}
