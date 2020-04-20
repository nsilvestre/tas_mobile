package com.company.test.pages.web;

import com.company.framework.pageobject.BasePO;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FaceBookHomePage extends BasePO{
    @FindAll({@FindBy(id="userNavigationLabel"),
            @FindBy(id = "bookmarks_jewel")})
    private WebElement settingsButton;

    @FindAll({@FindBy(css="._1k67"),
              @FindBy(css="#u_0_r")})
    private WebElement profileButton;

    @FindAll({@FindBy(xpath="//*[@id=\"u_a_1\"]/div/div/div/div[2]/div/div[9]/a/div[2]"),
              @FindBy(css="li._54ni:nth-child(12) > a:nth-child(1) > span:nth-child(1) > span:nth-child(1)")})
    //*[@id="js_d"]/div/div/ul/li[12]/a/span/span
    private WebElement logoutButton;

    @FindAll({@FindBy(xpath = "//*[@id=\"root\"]/div[1]/div/div/div[3]/div[1]/div/div/a")})
    private WebElement notNowOneTouchButton;

    @FindBy(id = "checkpointSubmitButton")
    private WebElement security;


    public FaceBookHomePage(WebDriver driver) {
        super(driver);
    }

    public WebElement getSettingsButton() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(settingsButton));
    }

    public WebElement getProfileButton() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(profileButton));
    }

    public WebElement getLogoutButton() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(logoutButton));
    }

    public WebElement getNotNowOneTouchButton() {
        return new WebDriverWait(getDriver(),2).until(ExpectedConditions.visibilityOf(notNowOneTouchButton));
    }

    public WebElement getSecurity() {
        return new WebDriverWait(getDriver(),2).until(ExpectedConditions.visibilityOf(security));
    }

    public void clickOnSettingsButton(){
        getSettingsButton().click();
    }

    public boolean settingsIsVisible(){
        return getProfileButton().isDisplayed();
    }

    public boolean securityIsVisible(){
        return getSecurity().isDisplayed();
    }

    public FaceBookLoginPage clickOnLogout(){
        getLogoutButton().click();
        return new FaceBookLoginPage(super.getDriver());
    }

    public boolean isLogoutVisible(){
        return getLogoutButton().isDisplayed();
    }

    public void clickNotNowOneTouchButton(){
        getNotNowOneTouchButton().click();
    }

    public void scrollUntilLogoutIsVisible(){
        scrollUntilVisible(getLogoutButton());
    }
}
