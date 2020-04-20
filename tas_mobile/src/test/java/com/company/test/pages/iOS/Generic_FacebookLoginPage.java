package com.company.test.pages.iOS;

import com.company.framework.pageobject.MobileBasePO;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.iOSFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Generic_FacebookLoginPage extends MobileBasePO{
    @FindBy(xpath = "//*[@id=\"header\"]/div/a/i/u")
    private WebElement fbLogo;

    @FindBy(xpath = "//*[@id=\"m_login_email\"]")
    private WebElement txtEmail;

    @FindBy(xpath = "//*[@id=\"m_login_password\"]")
    private WebElement txtPassword;

    @FindAll({@FindBy(name = "login"),
              @FindBy(xpath = "//*[@id=\"u_0_5\"]"),
              @FindBy(css = "#login_form > div._2pie > div:nth-child(1) > button")})
    private WebElement btnLogin;

    public Generic_FacebookLoginPage(AppiumDriver driver){
        super(driver);
    }

    public WebElement getFaceBookLogo() {
        return new WebDriverWait(getDriver(),30).until(ExpectedConditions.visibilityOf(fbLogo));
    }

    public WebElement getEmailField() {
        return new WebDriverWait(getDriver(),30).until(ExpectedConditions.visibilityOf(txtEmail));
    }

    public WebElement getPasswordField() {
        return new WebDriverWait(getDriver(),30).until(ExpectedConditions.visibilityOf(txtPassword));
    }

    public WebElement getLogintButton(){
        return new WebDriverWait(getDriver(),30).until(ExpectedConditions.visibilityOf(btnLogin));
    }

    public boolean isPageOpen(){
        return getFaceBookLogo().isDisplayed();
    }

    public void typeEmail(String email){
        getEmailField().sendKeys(email);
    }

    public void typePassword(String password){
        getPasswordField().sendKeys(password);
    }

    public void clickOnLogin(){
        getLogintButton().click();
    }

    public void clickOnUserName(){
        getEmailField().click();
    }
}
