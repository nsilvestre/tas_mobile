package com.company.test.steps.iOS;

import com.company.framework.utils.Context;
import com.company.test.pages.iOS.iOS_App_MainPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.company.framework.base.Framework.stop_appium_server;
import static org.assertj.core.api.Assertions.assertThat;

public class YouTubeSteps {
    private final Context context;
    private AppiumDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStepsiOS.class);
    private iOS_App_MainPage mainPage;

    public YouTubeSteps(Context context){
        this.context = context;
        driver = null;
    }

    @And("^The app is open using mobile$")
    public void theAppIsOpenUsingMobileDriver() throws Throwable {
        instantiate_page();
        assertThat(mainPage.isPageOpen());
    }

    @When("^I click on all the options using mobile$")
    public void iClickOnAllTheOptionsUsingMobileDriver() throws Throwable {
        instantiate_page();
        mainPage.clickOnTrending();

        assertThat(mainPage.getImgTrending().getAttribute("name").compareToIgnoreCase("trendingselected")).isEqualTo(0);

        mainPage.clickOnSubscriptions();
        assertThat(mainPage.getImgSubscriptions().getAttribute("name").compareToIgnoreCase("subscriptionsselected")).isEqualTo(0);

        mainPage.clickOnAccount();
        assertThat(mainPage.getImgAccount().getAttribute("name").compareToIgnoreCase("accountselected")).isEqualTo(0);

        mainPage.clickOnHome();
        assertThat(mainPage.getImgHome().getAttribute("name").compareToIgnoreCase("homeselected")).isEqualTo(0);
    }

    @Then("^The app navigates all the options successfully using mobile$")
    public void theAppNavigatesAllTheOptionsSuccessfullyUsingMobileDriver() throws Throwable {
        instantiate_page();
        assertThat(mainPage.isPageOpen());
        driver.closeApp();
        driver.quit();
        stop_appium_server();

    }

    private void instantiate_page() {
        driver = (AppiumDriver) context.getValue("driver");
        mainPage = new iOS_App_MainPage(driver);
    }
}
