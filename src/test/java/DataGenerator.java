import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import org.junit.jupiter.api.BeforeAll;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();


    private static void sendRequest(User userInfo) {
        given()
                .spec(requestSpec)
                .body(userInfo)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    private DataGenerator() {
    }

    private static Faker faker = new Faker(new Locale("en"));

    public static String getRandomLogin() {
        String login = faker.name().username();
        return login;
    }

    public static String getRandomPassword() {
        String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        public static User getUser(String status) {
            User userInfo = new User(getRandomLogin(), getRandomPassword(), status);
            return userInfo;
        }

        public static User getRegisteredUser(String status) {
            User registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class User {
        String login;
        String password;
        String status;
    }
}