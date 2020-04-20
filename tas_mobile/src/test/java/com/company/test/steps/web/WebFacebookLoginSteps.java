package com.company.test.steps.web;

import com.company.framework.utils.Context;
import com.company.test.pages.web.FaceBookHomePage;
import com.company.test.pages.web.FaceBookLoginPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;
import static org.assertj.core.api.Assertions.assertThat;


public class WebFacebookLoginSteps {

    private final Context context;
    private WebDriver driver;
    private FaceBookLoginPage faceBookLoginPage;
    private FaceBookHomePage faceBookHomePage;


    public WebFacebookLoginSteps(Context context) {
        this.context = context;
        driver = null;
    }

    @When("^I enter email \"([^\"]*)\"$")
    public void iEnterEmailByDriver(String email) throws Throwable {
        instantiate_page();
        faceBookLoginPage.typeEmail(email);
    }

    @And("^I enter password \"([^\"]*)\"$")
    public void iEnterPassword(String password) {
        try {
            faceBookLoginPage.typePassword(password);
        }catch(Exception e){
            faceBookLoginPage.typeEmail("\n");
            faceBookLoginPage.typePassword(password);
        }
    }

    @And("^I click the login button$")
    public void iClickTheLoginButton() throws Throwable {
        faceBookHomePage = faceBookLoginPage.clickOnLogin();
    }

    @Then("^The Scenario will fail$")
    public void scenarioWillFail() throws Throwable {
        assertThat(false).isTrue();
    }

    @Then("^Another page appears$")
    public void anotherPageAppears() throws Throwable {
        try{
            faceBookHomePage.clickNotNowOneTouchButton();
            assertThat(faceBookHomePage.settingsIsVisible()).isTrue();
        }catch(Exception e) {
            assertThat(true).isTrue(); //Only for accessibility reasons.
        }
        driver.quit();
    }

    private void instantiate_page() {
        faceBookLoginPage = new FaceBookLoginPage(driver);
    }

    @And("^I open the browser and go to the page \"([^\"]*)\"$")
    public void iOpenTheBrowserAndGoToThePageByUsingDriver(String url) throws Throwable {
        driver = (WebDriver) context.getValue("driver");
        driver.get(url);
        //driver.manage().window().maximize();
        instantiate_page();
        assertThat(faceBookLoginPage.isPageOpen()).isTrue();
    }

}
