package com.company.test.steps.images;

import com.company.framework.utils.Context;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.imagecomparison.*;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class OccurrenceLookupSteps {

    private final Context context;
    private AppiumDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(OccurrenceLookupSteps.class);
    private static OccurrenceMatchingResult result;
    private static SoftAssertions softly;
    private Scenario sce;

    public OccurrenceLookupSteps(Context context) {
        this.context = context;
        driver = null;
    }

    @Before
    public void tearDown(Scenario scenario) {
        sce = scenario;
    }

    @When("^I compare two images by using occurrence lookup$")
    public void compare_image_using_image_type() throws Throwable {
        instantiate_page();

        File sourceImg = new File(System.getProperty("user.dir") +
                       "/templates/finding_wally/finding_wally_source.png");

        File originalImg = new File(System.getProperty("user.dir") +
                       "/templates/finding_wally/finding_wally_template.png");

        //byte[] screenshot = Base64.encodeBase64(driver.getScreenshotAs(OutputType.BYTES));
        result = driver.findImageOccurrence(sourceImg, originalImg, new OccurrenceMatchingOptions()
                       .withEnabledVisualization());

    }

    @Then("^Validate visualization image length is greater than 0 in occurrence lookup$")
    public void validate_length_visualization_image() throws Throwable {

        softly = new SoftAssertions();
        softly.assertThat(result.getVisualization().length).isGreaterThan(0);

        result.storeVisualization(new File(System.getProperty("user.dir") + "/reports/visual_image_testing/" +
                sce.getName()+".jpg"));
    }

    @Then("^A match should be found$")
    public void a_match_should_be_found() throws Throwable {

        softly.assertThat(result.getRect()).isNotNull();
    }


    private void instantiate_page() {
        driver = (AppiumDriver) context.getValue("driver");
    }
}
