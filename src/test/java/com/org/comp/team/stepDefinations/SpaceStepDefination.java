package com.org.comp.team.stepDefinations;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class SpaceStepDefination {
    RequestSpecification res;
    String response;

//    ResponseSpecification respec=new ResponseSpecBuilder().expectStatusCode(200).log(LogDetail.ALL).build();

    @Given("^when user wants to fetch the details$")
    public void when_user_wants_to_fetch_the_details() throws Throwable {
        RequestSpecification req=new RequestSpecBuilder().setBaseUri("https://api.spacexdata.com/v4/launches/latest").log(LogDetail.ALL).build();
        res= given().spec(req);
//      res.when().get().then().spec(respec);
    }

    @When("^connects to the API to validate the content type$")
    public void connects_to_the_api_to_validate_the_content_type() throws Throwable {
        response= res.when().get().then().assertThat().statusCode(200)
                .body("success",equalTo(true))
                .body("name",equalTo("Starlink-28 (v1.0)"))
                .header("Server","Caddy")
                .header("Content-Type",equalTo("application/json; charset=utf-8"))
                .extract().response().asString();

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%"+ response+"%%%%%%%%%%%%%%%%%%%%%%%");
    }

    @Then("^user validates the contents to of the API$")
    public void user_validates_the_contents_to_of_the_api() throws Throwable {
        JsonPath js= new JsonPath(response);

        System.out.println("The name of the flight is  " + js.getString("name"));

        System.out.println("The status of fairings reused is  " + js.getString("fairings.reused"));

        System.out.println("The number of ships is "+js.getInt("ships.size()"));

        int count=js.getInt("ships.size()");
        for(int i=0;i<count;i++){
            System.out.println("The id of the : " + i + " Ship is : "+js.get("ships["+i+"]"));
        }
    }


}
