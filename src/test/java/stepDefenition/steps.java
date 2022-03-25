package stepDefenition;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Затем;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static hooks.GetConfig.getConfigurationValue;
import static io.restassured.RestAssured.given;

public class steps {

        RequestSpecification requestSpec = new RequestSpecBuilder().build()
                .given().baseUri("https://rickandmortyapi.com")
                .contentType(ContentType.JSON)
                .log().all();

        ResponseSpecification responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();

    @Дано("^Получаем Характер Морти (.*)$")
    public void morti(String num) {

        Response response1 = given()
                .baseUri(getConfigurationValue("url_morty"))
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/api/character/" + num)
                .then()
                .spec(responseSpec).extract().response();

        String resp1 = response1.getBody().asString();
    }

    @Затем("^Получаем последний персонаж последнего эпизода (.*)$")
    public void last_episode_last_person(String num) {

        Response response2 = given()
                .baseUri(getConfigurationValue("url_morty"))
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/api/character/"+ num)
                .then()
                .spec(responseSpec).extract().response();


        String resp2 = response2.getBody().asString();

        JSONObject json = new JSONObject(resp2);
        int arrSize = json.getJSONArray("episode").length();
        String episode = json.getJSONArray("episode").getString (arrSize-1);

        //--------------- Получение последнего персонажа
        Response response3 = given()
                .baseUri(getConfigurationValue("url_morty"))
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(episode)
                .then()
                .spec(responseSpec).extract().response();

        String resp3 = response3.getBody().asString();

        JSONObject json2 = new JSONObject(resp3);
        int arrSize2 = json2.getJSONArray("characters").length();
        String characters = json2.getJSONArray("characters").getString (arrSize2-1);

    }

    @Затем("^Получаем последний эпизод с Морти (.*)$")
    public void morti_last_episode(String num) {

        Response response2 = given()
                .baseUri(getConfigurationValue("url_morty"))
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get("/api/character/" + num)
                .then()
                .spec(responseSpec).extract().response();

        String resp2 = response2.getBody().asString();


        JSONObject json = new JSONObject(resp2);
        int arrSize = json.getJSONArray("episode").length();
        String episode = json.getJSONArray("episode").getString (arrSize-1);

    }


    @Дано("Создаём пользователя")
    public void test2() throws IOException {
        String body = "{\"name\": \"morpheus\",\"job\": \"leader\"}";

        JSONObject requestBody = new JSONObject(new String(Files.readAllBytes(Paths.get("src/test/resources/json/user.json"))));
        requestBody.put("name", "Tomato");
        requestBody.put("job", "Eat maket");

        Response response4 = given()
                .baseUri(getConfigurationValue("url_reqres"))
                .contentType("application/json;charset=UTF-8")
                .log().all()
                .when()
                .body(requestBody.toString())
                .post("/api/users")
                .then()
                .statusCode(201)
                .log().all()
                .extract().response();

        String resp4 = response4.getBody().asString();
        JSONObject json = new JSONObject(resp4);
        Assertions.assertEquals(json.getString("name"), "Tomato");
        Assertions.assertEquals(json.getString("job"), "Eat maket");
    }

}
