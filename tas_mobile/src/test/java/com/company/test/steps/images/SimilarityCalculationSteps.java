package com.company.test.steps.images;

import com.company.framework.utils.Context;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.imagecomparison.SimilarityMatchingOptions;
import io.appium.java_client.imagecomparison.SimilarityMatchingResult;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class SimilarityCalculationSteps {

    private final Context context;
    private AppiumDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimilarityCalculationSteps.class);
    private static SimilarityMatchingResult result;
    private static SoftAssertions softly;
    private Scenario sce;

    public SimilarityCalculationSteps(Context context) {
        this.context = context;
        driver = null;
    }

    @Before
    public void tearDown(Scenario scenario) {
        sce = scenario;
    }

    @When("^I compare two images by using similarity calculation$")
    public void compare_image_using_image_type() throws Throwable {
        instantiate_page();

        File sourceImg = new File(System.getProperty("user.dir") +
                        "/templates/credit_card/credit_card_source.jpg");

        File originalImg = new File(System.getProperty("user.dir") +
                        "/templates/credit_card/credit_card_source_3.png");

        result = driver.getImagesSimilarity(sourceImg, originalImg, new SimilarityMatchingOptions()
                       .withEnabledVisualization());
    }

    @Then("^Validate visualization image length is greater than 0 in similarity calcularion$")
    public void validate_length_visualization_image() throws Throwable {

        softly = new SoftAssertions();
        softly.assertThat(result.getVisualization().length).isGreaterThan(0);

        result.storeVisualization(new File(System.getProperty("user.dir") + "/reports/visual_image_testing/" +
                sce.getName()+".jpg"));
    }

    @Then("^Similarity score should be greater than 0$")
    public void similarity_score_should_be_greather_than_cero() {

        softly.assertThat(result.getScore()).isGreaterThan(0.0);
    }


    private void instantiate_page() {
        driver = (AppiumDriver) context.getValue("driver");
    }
}
