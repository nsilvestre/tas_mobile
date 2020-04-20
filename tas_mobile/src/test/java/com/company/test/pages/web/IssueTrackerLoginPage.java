package com.company.test.pages.web;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.company.framework.pageobject.BasePO;


public class IssueTrackerLoginPage extends BasePO{

    @FindBy(id = "username")
    private WebElement txt_username;

    @FindBy(id = "password")
    private WebElement txt_password;

    @FindBy(name = "login")
    private WebElement btn_login;

    public IssueTrackerLoginPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getUsername() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(txt_username));
    }

    public WebElement getPassword() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(txt_password));
    }

    public WebElement getBtnLogin() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(btn_login));
    }

    public void fillUsername(String user) {
        getUsername().sendKeys(user);
    }

    public void fillPassword(String pass) {
        getPassword().sendKeys(pass);
    }

    public void fillLogin(String user, String pass) {
        fillUsername(user);
        fillPassword(pass);
    }

    public IssueTrackerMyPage clickOnLoginBtn() {
        //getBtnLogin().click();
        JavascriptExecutor executor = (JavascriptExecutor)super.getDriver();
        executor.executeScript("arguments[0].click();", getBtnLogin());
        return new IssueTrackerMyPage(super.getDriver());
    }

}
