package com.company.test.pages.web;

import com.company.framework.pageobject.BasePO;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FaceBookLoginPage extends BasePO{
    @FindAll({@FindBy(css = ".fb_logo"),
              @FindBy(xpath = "//div[@id='header']/div/a/i"),
              @FindBy(xpath = "//div[@id='blueBarDOMInspector']/div/div/div/div/h1/a/i"),
              @FindBy(xpath = "//*[@id=\"header\"]/div[2]/div/div/a/i")})
    private WebElement faceBookLogo;

    @FindAll({@FindBy(id = "email"),
             @FindBy(id = "m_login_email"),
            @FindBy(xpath = "//*[@id=\"m_login_email\"]")
    })
    private WebElement emailField;

    @FindAll({@FindBy(id = "pass"),
              @FindBy(id = "m_login_password"),
              @FindBy(xpath = "//*[@id=\"m_login_password\"]")})
    private WebElement passwordField;

    @FindAll({@FindBy(id = "loginbutton"),
              @FindBy(name = "login"),
            @FindBy(xpath = "//*[@id=\"u_0_5\"]")})
    private WebElement logintButton;

    public FaceBookLoginPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getFaceBookLogo() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(faceBookLogo));
    }

    public WebElement getEmailField() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(emailField));
    }

    public WebElement getPasswordField() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(passwordField));
    }

    public WebElement getLogintButton(){
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(logintButton));
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

    public FaceBookHomePage clickOnLogin(){
        getLogintButton().click();
        return new FaceBookHomePage(super.getDriver());
    }
}