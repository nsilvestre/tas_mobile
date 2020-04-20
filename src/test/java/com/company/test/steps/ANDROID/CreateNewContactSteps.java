package com.company.test.steps.ANDROID;

import com.company.framework.utils.Context;
import com.company.test.pages.ANDROID.CreateNewContactPage;
import com.company.test.pages.ANDROID.MainContactsPage;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;

public class CreateNewContactSteps {

    private AppiumDriver driver;
    private final Context context;
    private CreateNewContactPage createNewContactPage;

    public CreateNewContactSteps(Context context) {
        this.context = context;
        driver = (AppiumDriver) context.getValue("driver");
        createNewContactPage = new CreateNewContactPage(driver);
    }

    @And("^I enter the first name and last name$")
    public void iEnterTheFirstNameAndLastName()
    {
        createNewContactPage.enterNameAndLastName();
    }

    @And("^I enter the phone number$")
    public void iEnterThePhoneNumber()
    {
        createNewContactPage.enterPhoneNumber();
    }

    @And("^I tap save$")
    public void iTapSave(){
        createNewContactPage.save();
    }

}
