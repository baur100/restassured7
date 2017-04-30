package org.megaprint;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import static io.restassured.RestAssured.given;

/**
 * Created by Baurz on 4/29/2017.
 */
public class CommonFunctions{
    public String login() throws IOException {
        Properties prop=new Properties();
        Response resp;
        String session=null;
        FileInputStream fis=new FileInputStream("data\\env.properties");
        prop.load(fis);
        RestAssured.baseURI=prop.getProperty("HOST");
        System.out.println(prop.getProperty("LOGIN_NFO"));
        resp=given().
                header("Content-Type","application/json").
                body(prop.getProperty("LOGIN_NFO")).
        when().
                post("/rest/auth/1/session").
        then().
                assertThat().
                statusCode(200).
        extract().
                response();
        JsonPath jpath=new JsonPath(resp.asString());
        return jpath.getString("session.name")+"="+jpath.getString("session.value");
    }
    public String createReport(String session, String issueTitle, String issueBody, String projectName){
        String key=null;
        Response resp;
        resp=given().
                header("Content-Type","application/json").
                header("Cookie",session).
                body("{\n\"fields\": {\n\"project\": {\n\"key\":\""+projectName+"\"\n" +
                        "},\n\"summary\":\""+issueTitle+"\",\n\"description\":\""+issueBody+"\",\n" +
                        "\"issuetype\":{\n\"name\":\"Bug\"\n}\n}\n}").
        when().
                post("/rest/api/2/issue").
        then().
                assertThat().
                statusCode(201).
        extract().
                response();
        return new JsonPath(resp.asString()).getString("key");
    }
}

