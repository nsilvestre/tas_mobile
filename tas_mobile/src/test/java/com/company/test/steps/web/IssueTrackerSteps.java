package com.company.test.steps.web;

import com.company.framework.utils.Context;
import com.company.test.pages.web.IssueTrackerLoginPage;
import com.company.test.pages.web.IssueTrackerMyPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueTrackerSteps {

    private final Context context;
    private WebDriver driver;
    private IssueTrackerLoginPage issueTrackerLoginPage;
    private IssueTrackerMyPage issueTrackerMyPage;

    public IssueTrackerSteps(Context context) {
        this.context = context;
        driver = null;
    }

    @When("^I enter username \"([^\"]*)\"$")
    public void fillUserNameByDriver(String user) {
        issueTrackerLoginPage.fillUsername(user);
    }

    @And("^I enter pass \"([^\"]*)\"$")
    public void fillPasswordByDriver(String password) {
        issueTrackerLoginPage.fillPassword(password);
    }

    @And("^I click on the login btn$")
    public void clickOnTheLoginButton() {
        issueTrackerMyPage = issueTrackerLoginPage.clickOnLoginBtn();
    }

    @And("^I validate My Page$")
    public void validate_issue_created__using_driver() {
        assertThat(issueTrackerMyPage.checkMyPage()).isTrue();
        driver.quit();
    }

    @And("^I open the browser and go to the \"([^\"]*)\"$")
    public void iOpenTheBrowserAndGoToTheByUsingDriver(String url) throws Throwable {
        driver = (WebDriver) context.getValue("driver");
        driver.get(url);
        //driver.manage().window().maximize();
        instantiate_page();
    }


    private void instantiate_page() {
        issueTrackerLoginPage = new IssueTrackerLoginPage(driver);
    }
}
