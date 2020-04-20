package com.company.test.pages.iOS;

import com.company.framework.pageobject.MobileBasePO;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class iOS_native_CreateNewReminderPage extends MobileBasePO{

    @iOSFindBy(xpath = "//XCUIElementTypeOther[@name=\"Create Reminder\"]")
    private IOSElement pageTitle;

    @iOSFindBy(accessibility = "Cancel")
    private IOSElement btnCancel;

    @iOSFindBy(accessibility = "Done")
    private IOSElement btnDone;

    @iOSFindBy(xpath = "(//XCUIElementTypeTextView[@name=\"AXReminderNotesIdentifier\"])[1]")
    private IOSElement txtTitle;

    @iOSFindBy(xpath = "//XCUIElementTypeSwitch[@name=\"Remind me on a day\"]")
    private IOSElement switchRemindOnDay;

    @iOSFindBy(accessibility = "None")
    private IOSElement btnNone;

    @iOSFindBy(accessibility = "Low")
    private IOSElement btnLow;

    @iOSFindBy(accessibility = "Medium")
    private IOSElement btnMedium;

    @iOSFindBy(accessibility = "High")
    private IOSElement btnHigh;

    @iOSFindBy(xpath = "(//XCUIElementTypeTextView[@name=\"AXReminderNotesIdentifier\"])[2]")
    private IOSElement txtNotes;

    public iOS_native_CreateNewReminderPage(AppiumDriver driver) {
        super(driver);
    }

    public IOSElement getPageTitle() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(pageTitle));
    }

    public IOSElement getBtnCancel() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnCancel));
    }

    public IOSElement getBtnDone() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnDone));
    }

    public IOSElement getTxtTitle() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(txtTitle));
    }

    public IOSElement getSwitchRemindOnDay() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(switchRemindOnDay));
    }

    public IOSElement getBtnNone() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnNone));
    }

    public IOSElement getBtnLow() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnLow));
    }

    public IOSElement getBtnMedium() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnMedium));
    }

    public IOSElement getBtnHigh() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnHigh));
    }

    public IOSElement getTxtNotes() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(txtNotes));
    }

    public iOS_native_ReminderPage clickOnCancel(){
        getBtnCancel().click();
        return new iOS_native_ReminderPage(getDriver());
    }

    public boolean isPageOpen(){
        return getPageTitle().isDisplayed();
    }

    public iOS_native_ReminderPage clickOnDone(){
        getBtnDone().click();
        return new iOS_native_ReminderPage(getDriver());
    }

    public void setReminderTitle(String title){
        getTxtTitle().sendKeys(title);
    }

    public void clickOnRemindOnDay(){
        getSwitchRemindOnDay().click();
    }

    public void clickOnNonePriority(){
        getBtnNone().click();
    }

    public void clickOnLowPriority(){
        getBtnLow().click();
    }

    public void clickOnMediumPriority(){
        getBtnMedium().click();
    }

    public void clickOnHighPriority(){
        getBtnHigh().click();
    }

    public void setReminderNotes(String notes){
        getTxtNotes().sendKeys(notes);
    }
}
