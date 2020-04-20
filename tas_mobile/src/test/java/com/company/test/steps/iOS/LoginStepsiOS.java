package com.company.test.steps.iOS;

import com.company.framework.utils.Context;
import com.company.test.pages.iOS.*;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.company.framework.base.Framework.stop_appium_server;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginStepsiOS {
    private final Context context;
    private AppiumDriver driver;
    private Generic_FacebookLoginPage faceBookLoginPage;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStepsiOS.class);

    public LoginStepsiOS(Context context){
        this.context = context;
        driver = null;
    }

    @When("^I enter email \"([^\"]*)\" by using mobile$")
    public void iEnterEmailByDriver(String email) throws Throwable {
        instantiate_page();
        faceBookLoginPage.typeEmail(email);
    }

    @And("^I enter password \"([^\"]*)\" by using mobile$")
    public void iEnterPassword(String password){
        instantiate_page();
        try {
            faceBookLoginPage.typePassword(password);
        }catch(Exception e){
            faceBookLoginPage.clickOnUserName();
            faceBookLoginPage.getEmailField().sendKeys("\n");
            faceBookLoginPage.typePassword(password);
        }
    }

    @And("^I click the login button by using mobile$")
    public void iClickTheLoginButton() throws Throwable {
        instantiate_page();
        faceBookLoginPage.clickOnLogin();
    }

    @Then("^Another page appears by using mobile$")
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
}
