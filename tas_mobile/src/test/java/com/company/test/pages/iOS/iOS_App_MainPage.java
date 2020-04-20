package com.company.test.pages.iOS;

import com.company.framework.pageobject.MobileBasePO;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.iOSBy;
import io.appium.java_client.pagefactory.iOSFindAll;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class iOS_App_MainPage extends MobileBasePO{

    @iOSFindBy(accessibility = "Home")
    private IOSElement pageTitle;

    @iOSFindBy(xpath = "//XCUIElementTypeApplication[@name=\"YouTube\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]" +
                       "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/" +
                       "XCUIElementTypeCollectionView/XCUIElementTypeCell[1]/XCUIElementTypeOther")
    private IOSElement btnHome;


    @iOSFindBy(xpath = "/XCUIElementTypeApplication[@name=\"YouTube\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]" +
                       "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/" +
                       "XCUIElementTypeCollectionView/XCUIElementTypeCell[2]/XCUIElementTypeOther")
    private IOSElement btnTrending;

    @iOSFindBy(xpath = "/XCUIElementTypeApplication[@name=\"YouTube\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]" +
                       "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/" +
                       "XCUIElementTypeCollectionView/XCUIElementTypeCell[3]/XCUIElementTypeOther")
    private IOSElement btnSubscriptions;

    @iOSFindBy(xpath = "/XCUIElementTypeApplication[@name=\"YouTube\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/" +
            "XCUIElementTypeCollectionView/XCUIElementTypeCell[4]/XCUIElementTypeOther")
    private IOSElement btnAccount;

    @iOSFindAll({@iOSBy(accessibility = "home"),@iOSBy(accessibility = "homeSelected")})
    private IOSElement imgHome;

    @iOSFindAll({@iOSBy(accessibility = "trending"),@iOSBy(accessibility = "trendingSelected")})
    private IOSElement imgTrending;

    @iOSFindAll({@iOSBy(accessibility = "subscriptions"),@iOSBy(accessibility = "subscriptionsSelected")})
    private IOSElement imgSubscriptions;

    @iOSFindAll({@iOSBy(accessibility = "account"),@iOSBy(accessibility = "accountSelected")})
    private IOSElement imgAccount;

    public iOS_App_MainPage(AppiumDriver driver) {
        super(driver);
    }

    public IOSElement getPageTitle() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(pageTitle));
    }

    public IOSElement getBtnHome() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnHome));
    }

    public IOSElement getBtnTrending() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnTrending));
    }

    public IOSElement getBtnSubscriptions() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnSubscriptions));
    }

    public IOSElement getBtnAccount() {
        return (IOSElement) new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btnAccount));
    }

    public IOSElement getImgHome() {
        return imgHome;
    }

    public IOSElement getImgTrending() {
        return imgTrending;
    }

    public IOSElement getImgSubscriptions() {
        return imgSubscriptions;
    }

    public IOSElement getImgAccount() {
        return imgAccount;
    }


    public boolean isPageOpen(){
        return getPageTitle().isDisplayed();
    }

    public void clickOnHome(){
        getBtnHome().click();
    }

    public void clickOnTrending(){
        getBtnTrending().click();
    }

    public void clickOnSubscriptions(){
        getBtnSubscriptions().click();
    }

    public void clickOnAccount(){
        getBtnAccount().click();
    }
}
