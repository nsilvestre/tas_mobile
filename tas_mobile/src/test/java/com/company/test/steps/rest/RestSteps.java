package com.company.test.steps.rest;


import com.company.framework.dtos.IssueDto;
import com.company.framework.reportPortalFiles.ReportPortal;
import com.company.framework.utils.Context;
import com.company.framework.utils.RestHelper;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.junit.AssumptionViolatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import static com.company.framework.base.Framework.process_setup_rest_handler;
import static org.assertj.core.api.Assertions.assertThat;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.matcher.RestAssuredMatchers.matchesXsd;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.lang.ClassLoader.getSystemResourceAsStream;


public class RestSteps {

    private final Context context;
    private static RequestSpecBuilder builder;
    private final RestHelper restHelper = new RestHelper();

    public RestSteps(Context context) {
        this.context = context;
    }

    @Before
    public void beforeScenario(Scenario sc) {
        Collection<String> tags = sc.getSourceTagNames();
        String tagName = null;
        for(Object tag : tags){
            tagName = tag.toString();
        }

        if (!tagName.equals("@working")) {
            throw new AssumptionViolatedException("Scenario not tagged as working, so skipping");
        }
    }

    private Response response;
    private RequestSpecification request;
    private String baseURI;
    private String apiKey;
    private String responseFormat;
    private static final Logger LOGGER = LoggerFactory.getLogger(RestSteps.class);
    private static Integer lastId;


    @Given("([^\"]*) Handler for \"([^\"]*)\"$")
    public void setup_handler(String configName, String typeTest) throws Exception {
        context.setValue("typeTest", typeTest);
        builder = process_setup_rest_handler(context, configName);
    }

    @Given("^ResponseFormat in \"([^\"]*)\"$")
    public void setup_driver(String respFormat) throws Exception {
        context.setValue("responseFormat", respFormat);
    }

    @When("^I send an insert issue request to the server$")
    public void send_insert_issue() throws IOException {
        request = given();

        IssueDto issueDto = restHelper.setIssueDetailsDto(1,
                                                          1,
                                                          1,
                                                          3,
                                                          "New IssueDto" + restHelper.getStringID(),
                                                          "Description example");

        apiKey = context.getValue("apiKey").toString();
        responseFormat = context.getValue("responseFormat").toString();
        baseURI = context.getValue("baseUri").toString();
        builder.addHeader("X-Redmine-API-Key", apiKey);
        RequestSpecification requestSpec = builder.build();

        response = request.spec(requestSpec).
                log().all().
                contentType("application/json").
                body(issueDto).
                when().
                post(baseURI + responseFormat);

        context.setValue("response", response);
        response.then().log().all().extract();
        lastId = response.path("issue.id");
        LOGGER.info("Extract of last id " + lastId);
    }

    @Then("^validate that status code is (\\d+)$")
    public void validate_status_code(int statusCode){

        response = (Response) context.getValue("response");
        response.then().statusCode(statusCode);
    }

    @When("^I send an update issue request to the server with id equal to (\\d+)$")
    public void send_insert_update(int id) throws IOException {
        request = given();

        IssueDto issueDto = restHelper.setIssueDetailsDto(1,
                                                          3,
                                                          1,
                                                          1,
                                                          "Description Updated 1",
                                                          "Description Updated 1");

        apiKey = context.getValue("apiKey").toString();
        baseURI = context.getValue("baseUri").toString();
        responseFormat = context.getValue("responseFormat").toString();

        builder.addHeader("X-Redmine-API-Key", apiKey);
        RequestSpecification requestSpec = builder.build();

        response = request.spec(requestSpec).
                log().all().
                contentType("application/json").
                body(issueDto).
                when().
                put(baseURI + id + responseFormat);

        context.setValue("response", response);
        response.then().log().all();
    }

    @When("^I send an delete issue request to the server with id (\\d+)$")
    public void send_delete_issue(int id) throws IOException {
        request = given();

        apiKey = context.getValue("apiKey").toString();
        baseURI = context.getValue("baseUri").toString();
        responseFormat = context.getValue("responseFormat").toString();

        builder.addHeader("X-Redmine-API-Key", apiKey);
        RequestSpecification requestSpec = builder.build();


        //Esta solo para que el test siempre encuentre un id existente.
        id = lastId;

        response = request.spec(requestSpec).
                log().all().
                contentType("application/json").
                when().
                delete(baseURI + id + responseFormat);

        context.setValue("response", response);
        response.then().log().all();
    }

    @When("^I get issues from redmine and fail$")
    public void get_issues_with_failed(){
        // This is just an example of how to send a message to ReportPortal Application.
        ReportPortal.emitLog("###### Test of message ####", "INFO", Calendar.getInstance().getTime());

        //This is just an example of how to send a message with file to ReportPortal Application.
        String OS = System.getProperty("os.name");
        File file = new File(System.getProperty("user.dir") +
                "/src/main/java/com/company/framework/reportPortalFiles/reportportal.png");
        ReportPortal.emitLog("***** Test of message with screenshot *****", "INFO", Calendar.getInstance().getTime(), file);

        assertThat(Boolean.FALSE).isTrue();
    }

    @When("^I get issues from redmine with id equal to (\\d+)$")
    public void get_issues_by_id(int id){
        request = given();

        apiKey = context.getValue("apiKey").toString();
        baseURI = context.getValue("baseUri").toString();
        responseFormat = context.getValue("responseFormat").toString();

        builder.addHeader("X-Redmine-API-Key", apiKey);
        RequestSpecification requestSpec = builder.build();

        response = request.spec(requestSpec).
                log().all().
                when().
                get(baseURI + id + responseFormat);

        context.setValue("response", response);
        response.then().log().all();
    }

    @Then("^validate json response with the following schema \"([^\"]*)\"$")
    public void validate_json_schema(String schemaPath){

        response = (Response) context.getValue("response");

        response.then().log().all().
                contentType("application/json").
                body(matchesJsonSchemaInClasspath(schemaPath));
    }

    @Then("^validate xml response with the following schema \"([^\"]*)\"$")
    public void validate_xml_schema(String schemaPath){

        response = (Response) context.getValue("response");

        response.then().log().all().
                contentType("application/xml").
                body(matchesXsd(getSystemResourceAsStream(schemaPath)).using(new ClasspathResourceResolver()));
    }


    @Then("I validate response data$")
    public void response_equals(Map<String,String> responseFields){

        response = (Response) context.getValue("response");

        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        for (Map.Entry<String, String> field : responseFields.entrySet()) {

            if(StringUtils.isNumeric(field.getValue())){
                assertThat(Integer.parseInt(field.getValue())).isEqualTo(jp.get(field.getKey()));
            }
            else{
                assertThat(field.getValue()).isEqualTo(jp.get(field.getKey()));
            }
        }
    }


    public class ClasspathResourceResolver implements LSResourceResolver {

    @Override
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        InputStream resource = getSystemResourceAsStream(systemId);
        return new DOMInputImpl(publicId, systemId, baseURI, resource, null);
    }
}
}
