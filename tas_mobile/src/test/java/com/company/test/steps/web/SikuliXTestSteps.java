package com.company.test.steps.web;


import com.company.framework.utils.Context;
import com.company.framework.utils.SikuliX2014Helper;
import com.company.test.pages.web.FaceBookLoginPage;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

import static com.company.framework.base.Framework.stop_appium_server;
import static com.company.framework.utils.ImageFinder.getImageByDevice;
import static org.assertj.core.api.Assertions.assertThat;

public class SikuliXTestSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(SikuliXTestSteps.class);
    private final Context context;
    private WebDriver driver;
    private FaceBookLoginPage faceBookLoginPage;


    public SikuliXTestSteps(Context context){
        this.context = context;
        driver = null;
    }


    @Then("^I find the facebook logo and is true highlighted for (\\d+) seconds$")
    public void iFindTheFacebookLogoAndIsTrueHighlightedForSeconds(int arg0) throws Throwable {
        instantiate_page();
        String faceLogo = getImageByDevice("logo", context);
        driver.switchTo().window(driver.getWindowHandle());
        Boolean isPresent = SikuliX2014Helper.isImagePresent(faceLogo, 5000,true, arg0);

        assertThat(isPresent).isTrue();
    }

    @When("^Found the email and password fields$")
    public void foundTheEmailAndPasswordFields() throws Throwable {
        instantiate_page();
        SoftAssertions softly = new SoftAssertions();

        String mailTxt = getImageByDevice("mail", context);
        String passTxt = getImageByDevice("password", context);

        driver.switchTo().window(driver.getWindowHandle());
        Boolean isPresent = SikuliX2014Helper.isImagePresent(mailTxt,5000,false,0);
        driver.switchTo().window(driver.getWindowHandle());
        Boolean isPresent2 = SikuliX2014Helper.isImagePresent(passTxt,5000,false,0);

        softly.assertThat(isPresent).isTrue();
        softly.assertThat(isPresent2).isTrue();
        try {
            softly.assertAll();
        } catch (SoftAssertionError e) {
            LOGGER.error(e.toString());
        }
    }

    @Then("^I write the username and password$")
    public void iWriteTheUsernameAndPassword(DataTable dataTable) throws Throwable {
        instantiate_page();

        String mailTxt = getImageByDevice("mail", context);
        String passTxt = getImageByDevice("password", context);

        driver.switchTo().window(driver.getWindowHandle());
        SikuliX2014Helper.clickOnImageAndType(mailTxt,10,dataTable.raw().get(0).get(0),true,5);
        driver.switchTo().window(driver.getWindowHandle());
        SikuliX2014Helper.clickOnImageAndType(passTxt,10,dataTable.raw().get(0).get(1),true,5);
    }

    @When("^I found the login button$")
    public void iFoundTheLoginButton() throws Throwable {
        instantiate_page();

        String loginButton = getImageByDevice("button", context);
        driver.switchTo().window(driver.getWindowHandle());
        Boolean isPresent = SikuliX2014Helper.isImagePresent(loginButton, 0, false, 0);

        assertThat(isPresent).isTrue();
    }

    @And("^The facebook logo is present on page$")
    public void theFacebookLogoIsPresentOnPage() throws IOException {
        instantiate_page();
        String facebookLogo = getImageByDevice("logo", context);
        Boolean isPresent = SikuliX2014Helper.isImagePresent(facebookLogo,0, false, 0);
        assertThat(isPresent).isTrue();
        driver.quit();
    }

    @Then("^I click on the login button$")
    public void iClickOnTheLoginButton() throws Throwable {
        instantiate_page();

        String loginButton = getImageByDevice("button", context);
        driver.switchTo().window(driver.getWindowHandle());
        SikuliX2014Helper.clickOnImage(loginButton,5,true,5);
    }

    @Given("([^\"]*) device$")
    public void setup_device(String device) throws Exception {
        context.setValue("device", device);
    }

    private void instantiate_page() {
        driver = (WebDriver) context.getValue("driver");
        faceBookLoginPage = new FaceBookLoginPage(driver);
    }
}
