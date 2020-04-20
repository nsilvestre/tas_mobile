package com.company.test.steps.iOS;

import com.company.framework.utils.Context;
import com.company.test.pages.iOS.iOS_native_CreateNewPage;
import com.company.test.pages.iOS.iOS_native_CreateNewReminderPage;
import com.company.test.pages.iOS.iOS_native_ReminderPage;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.company.framework.base.Framework.stop_appium_server;
import static org.assertj.core.api.Assertions.assertThat;

public class ReminderSteps {
    private final Context context;
    private AppiumDriver driver;
    private iOS_native_ReminderPage remainderPage;
    private iOS_native_CreateNewPage createNewPage;
    private iOS_native_CreateNewReminderPage createNewReminderPage;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginStepsiOS.class);

    public ReminderSteps(Context context){
        this.context = context;
        driver = null;
    }

    @And("^The Reminder App is open$")
    public void theReminderAppIsOpenByUsingDriver() throws Throwable {
        instantiate_page();
        assertThat(remainderPage.isPageOpen());
    }

    @When("^I click on the Add Reminder button$")
    public void iClickOnTheAddReminderButton() throws Throwable {
        createNewPage = remainderPage.clickOnAddBtn();
        assertThat(createNewPage.isPageOpen());
    }

    @And("^Select create New reminder$")
    public void selectCreateNewReminderByUsingDriver() throws Throwable {
        createNewReminderPage = createNewPage.clickOnReminder();
        assertThat(createNewReminderPage.isPageOpen());
    }

    @And("^I fill the reminder data and click on the Done button$")
    public void iFillTheReminderDataAndClickOnTheDoneButtonByUsingDriver(DataTable dataTable) throws Throwable {
        createNewReminderPage.setReminderTitle(dataTable.raw().get(0).get(0));
        this.setPriority(dataTable.raw().get(0).get(1));
        createNewReminderPage.setReminderNotes(dataTable.raw().get(0).get(2));
        remainderPage = createNewReminderPage.clickOnDone();
    }

    @Then("^The reminder is successfully created$")
    public void theReminderIsSuccessfullyCreatedByUsingDriver() throws Throwable {
        assertThat(remainderPage.isPageOpen());
        driver.closeApp();
        driver.quit();
        stop_appium_server();

    }

    private void instantiate_page() {
        driver = (AppiumDriver) context.getValue("driver");
        remainderPage = new iOS_native_ReminderPage(driver);
    }

    private void setPriority(String priority){
        switch (priority){
            case "none":
                createNewReminderPage.clickOnNonePriority();
                break;
            case "low":
                createNewReminderPage.clickOnLowPriority();
                break;
            case "medium":
                createNewReminderPage.clickOnMediumPriority();
                break;
            case "high":
                createNewReminderPage.clickOnHighPriority();
                break;
        }
    }
}
