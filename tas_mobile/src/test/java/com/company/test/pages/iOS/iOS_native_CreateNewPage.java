package com.company.test.pages.iOS;

import com.company.framework.pageobject.MobileBasePO;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class iOS_native_CreateNewPage extends MobileBasePO{

    @iOSFindBy(xpath = "//XCUIElementTypeSheet[@name=\"Create new…\"]")
    private IOSElement pageTitle;

    @iOSFindBy(accessibility = "Reminder")
    private IOSElement btnReminder;

    @iOSFindBy(accessibility = "List")
    private IOSElement btnList;

    @iOSFindBy(accessibility = "Cancel")
    private IOSElement btnCancel;

    public iOS_native_CreateNewPage(AppiumDriver driver) {
        super(driver);
    }

    public IOSElement getPageTitle() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(pageTitle));
    }

    public IOSElement getBtnReminder() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnReminder));
    }

    public IOSElement getBtnList() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnList));
    }

    public IOSElement getBtnCancel() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnCancel));
    }

    public iOS_native_CreateNewReminderPage clickOnReminder(){
        getBtnReminder().click();
        return new iOS_native_CreateNewReminderPage(getDriver());
    }

    public void clickOnList(){
        getBtnList().click();
    }

    public iOS_native_CreateNewPage clickOnCancel(){
        getBtnCancel().click();
        return this;
    }

    public boolean isPageOpen(){
        return getPageTitle().isDisplayed();
    }
}
