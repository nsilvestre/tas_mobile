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
import java.io.*;



public class BasedComparisonSteps {

    private final Context context;
    private AppiumDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(BasedComparisonSteps.class);
    private static FeaturesMatchingResult result;
    private static SoftAssertions softly;
    private Scenario sce;

    public BasedComparisonSteps(Context context) {
        this.context = context;
        driver = null;
    }

    @Before
    public void tearDown(Scenario scenario) {
        sce = scenario;
    }

    @When("^I compare two images by using feature-based comparison$")
    public void compare_image_using_image_type() throws Throwable {
        instantiate_page();

        File sourceImg = new File(System.getProperty("user.dir") +
                      "/templates/credit_card/credit_card_source.jpg");

        File originalImg = new File(System.getProperty("user.dir") +
                      "/templates/credit_card/credit_card_template.jpg");

        //byte[] sourceImg = Base64.encodeBase64(driver.getScreenshotAs(OutputType.BYTES));
        result = driver.matchImagesFeatures(sourceImg, originalImg, new FeaturesMatchingOptions()
                       .withDetectorName(FeatureDetector.ORB)
                       .withGoodMatchesFactor(40)
                       .withMatchFunc(MatchingFunction.BRUTE_FORCE_HAMMING)
                       .withEnabledVisualization());
    }

    @Then("^Validate visualization image length is greater than 0 in based comparison$")
    public void validate_visualization_image() throws Throwable {

        softly = new SoftAssertions();
        softly.assertThat(result.getVisualization().length).isGreaterThan(0);

        result.storeVisualization(new File(System.getProperty("user.dir") + "/reports/visual_image_testing/" +
                sce.getName()+".jpg"));
    }

    @Then("^Images should have the same Feature-based comparison$")
    public void images_should_have_the_same_features_based_comparison() throws Throwable {

        softly.assertThat(result.getCount()).isGreaterThan(0);
        softly.assertThat(result.getTotalCount()).isGreaterThan(0);
        softly.assertThat(result.getPoints1()).isNotEmpty();
        softly.assertThat(result.getRect1()).isNotNull();
        softly.assertThat(result.getPoints2()).isNotEmpty();
        softly.assertThat(result.getRect2()).isNotNull();
    }


    private void instantiate_page() {
        driver = (AppiumDriver) context.getValue("driver");
    }
}
