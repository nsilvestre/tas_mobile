package com.company.test.pages.iOS;

import com.company.framework.pageobject.MobileBasePO;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class iOS_native_ReminderPage extends MobileBasePO{

    @iOSFindBy(accessibility = "Add")
    private IOSElement btnAdd;

    @iOSFindBy(accessibility = "Scheduled")
    private IOSElement btnScheduled;

    @iOSFindBy(xpath = "//XCUIElementTypeButton[@name=\"Reminders\"]")
    private IOSElement btnReminders;

    public iOS_native_ReminderPage(AppiumDriver driver) {
        super(driver);
    }

    public IOSElement getBtnAdd() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnAdd));
    }

    public IOSElement getBtnScheduled() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnScheduled));
    }

    public IOSElement getBtnReminders() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnReminders));
    }

    public iOS_native_CreateNewPage clickOnAddBtn(){
        getBtnAdd().click();
        return new iOS_native_CreateNewPage(getDriver());
    }

    public boolean isPageOpen(){
        return getBtnReminders().isDisplayed();
    }

    public void swipeDown(){
        iOSElementSwipe(getBtnReminders(),"down");
    }

    public void clickOnScheduled(){
        getBtnScheduled().click();
    }

    public void clickOnReminders(){
        getBtnReminders().click();
    }

    @Override
    public void swipeJS(String direction, IOSElement element) {
        super.swipeJS(direction, element);
    }
}
