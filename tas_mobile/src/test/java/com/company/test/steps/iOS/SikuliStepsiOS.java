package com.company.test.steps.iOS;


import com.company.framework.utils.Context;
import com.company.framework.utils.SikuliX2014Helper;
import com.company.test.pages.iOS.*;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;
import org.assertj.core.api.SoftAssertionError;
import org.assertj.core.api.SoftAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.company.framework.utils.ImageFinder.getImageByDevice;
import static com.company.framework.base.Framework.stop_appium_server;
import static org.assertj.core.api.Assertions.assertThat;


public class SikuliStepsiOS {
    private final Context context;
    private AppiumDriver driver;
    private Generic_FacebookLoginPage faceBookLoginPage;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStepsiOS.class);

    public SikuliStepsiOS(Context context){
        this.context = context;
        driver = null;
    }

    @When("^I found the login button by using mobile driver$")
    public void iFoundTheLoginButton() throws Throwable {
        instantiate_page();

        String loginButton = getImageByDevice("button", context);

        Boolean isPresent = SikuliX2014Helper.isImagePresent(loginButton, 0, false, 0);

        assertThat(isPresent).isTrue();
    }

    @Then("^I click on the login button by using mobile driver (\\d+)$")
    public void iClickOnTheLoginButton(int driverId) throws Throwable {
        instantiate_page();

        String loginButton = getImageByDevice("button", context);

        SikuliX2014Helper.clickOnImage(loginButton,5,true,5);
    }

    @Then("^I write the username and password by using mobile$")
    public void iWriteTheUsernameAndPassword(DataTable dataTable) throws Throwable {
        instantiate_page();

        String mailTxt = getImageByDevice("mail", context);
        String passTxt = getImageByDevice("password", context);


        SikuliX2014Helper.clickOnImageAndType(mailTxt,10,dataTable.raw().get(0).get(0),true,5);
        SikuliX2014Helper.clickOnImageAndType(passTxt,10,dataTable.raw().get(0).get(1),true,5);
    }



    @Then("^I find the mobile facebook logo and is true highlighted for (\\d+) seconds$")
    public void iFindTheFacebookLogoAndIsTrueHighlightedForSeconds(int arg0) throws Throwable {
        instantiate_page();
        String faceLogo = getImageByDevice("logo", context);

        Boolean isPresent = SikuliX2014Helper.isImagePresent(faceLogo, 5000,true, arg0);

        assertThat(isPresent).isTrue();
    }

    @When("^Found the email and password fields by using mobile$")
    public void foundTheEmailAndPasswordFields() throws Throwable {
        instantiate_page();
        SoftAssertions softly = new SoftAssertions();

        String mailTxt = getImageByDevice("mail", context);
        String passTxt = getImageByDevice("password", context);

        Boolean isPresent = SikuliX2014Helper.isImagePresent(mailTxt,5000,false,0);
        Boolean isPresent2 = SikuliX2014Helper.isImagePresent(passTxt,5000,false,0);

        assertThat(isPresent).isTrue();
        assertThat(isPresent2).isTrue();
        try {
            softly.assertAll();
        } catch (SoftAssertionError e) {
            LOGGER.error(e.toString());
        }
    }

    @Then("^Another mobile page appears$")
    public void anotherPageAppears() throws Throwable {
        instantiate_page();
        String path = driver.getCurrentUrl();
        assertThat(path).contains("facebook");
        driver.quit();
        stop_appium_server();
    }

    private void instantiate_page() {
        driver = (AppiumDriver) context.getValue("driver");
        faceBookLoginPage = new Generic_FacebookLoginPage(driver);
    }

    @And("^I open the mobile browser and go to the page \"([^\"]*)\"$")
    public void iOpenTheMobileBrowserAndGoToThePageByUsingDriver(String url) throws Throwable {
        driver = (AppiumDriver) context.getValue("driver");
        driver.manage().deleteAllCookies();
        driver.get(url);

        faceBookLoginPage = new Generic_FacebookLoginPage(driver);
    }

    @Given("([^\"]*) platform")
    public void setup_device(String platform) throws Exception {
        context.setValue("device", "");
        context.setValue("platform", platform);
        context.setValue("browserName", "");
    }
}
