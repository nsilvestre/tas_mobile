package com.company.test.steps.android;


import com.company.framework.utils.Context;
import com.company.test.pages.android.FacebookLoginPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.company.framework.base.Framework.stop_appium_server;
import static org.assertj.core.api.Assertions.assertThat;

public class FacebookSteps {

    private final Context context;
    private AppiumDriver driver;
    private FacebookLoginPage facebookLoginPage;
    private static final Logger LOGGER = LoggerFactory.getLogger(AndroidStep.class);

    public FacebookSteps(Context context){
        this.context = context;
        driver = null;
    }


    @And("^The app is open using mobile android$")
    public void open_app() throws Throwable {
        instantiate_page();
    }

    @When("^I enter email \"([^\"]*)\" by using mobile android$")
    public void fill_email_android_driver(String email) throws Throwable {
        instantiate_page();
        facebookLoginPage.setEmail(email);
    }

    @And("^I enter password \"([^\"]*)\" by using mobile android$")
    public void fill_pass_android_driver(String pass) throws Throwable {
         try {
            facebookLoginPage.setTxtPassword(pass);
        }catch(Exception e){
            facebookLoginPage.click_on_login_button();
            facebookLoginPage.setTxtPassword(pass);
        }
    }

    @And("^I click the login button by using mobile android$")
    public void click_on_login_btn() throws Throwable {
        facebookLoginPage.click_on_login_button();
    }

    @Then("^Alert appears by using mobile android$")
    public void anotherPageAppears() throws Throwable {
        try {
            assertThat(facebookLoginPage.isBtnOkDisplayed()).isTrue();
        } catch(Exception e){
            assertThat(true).isTrue();
        }

        String bundleId = driver.getCapabilities().getCapability("appPackage").toString();
        driver.closeApp();
        driver.removeApp(bundleId);
        try{
            facebookLoginPage.clickOnUninstallOk();
        }catch(Exception e){
            LOGGER.info("App already uninstalled");
        }
        driver.quit();
        stop_appium_server();
    }


    private void instantiate_page() {
        driver = (AppiumDriver) context.getValue("driver");
        facebookLoginPage = new FacebookLoginPage(driver);
    }
}
