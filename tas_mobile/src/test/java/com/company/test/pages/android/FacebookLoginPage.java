package com.company.test.pages.android;

import com.company.framework.pageobject.MobileBasePO;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FacebookLoginPage extends MobileBasePO {

    @AndroidFindBy(id = "com.facebook.katana:id/login_username")
    private AndroidElement txtEmail;

    @AndroidFindBy(id = "com.facebook.katana:id/login_password")
    private AndroidElement txtPassword;

    @AndroidFindBy(id = "com.facebook.katana:id/login_login")
    private AndroidElement btnLogin;

    @AndroidFindBy(id = "com.facebook.katana:id/alertTitle")
    private AndroidElement lblIncorrectPass;

    @AndroidFindBy(id = "com.facebook.katana:id/dbl_on")
    private AndroidElement btn_ok;

    @AndroidFindBy(id = "com.google.android.packageinstaller:id/button1")
    private AndroidElement uninstallOk;


    public FacebookLoginPage(AppiumDriver driver) {
        super(driver);
    }

    public AndroidElement getTxtEmail() {
        return (AndroidElement)(new WebDriverWait(getDriver(),20).until(ExpectedConditions.visibilityOf(txtEmail)));
    }

    public AndroidElement getTxtPassword() {
        return (AndroidElement)(new WebDriverWait(getDriver(),20).until(ExpectedConditions.visibilityOf(txtPassword)));
    }

    public AndroidElement getBtnLogin() {
        return (AndroidElement)(new WebDriverWait(getDriver(),20).until(ExpectedConditions.visibilityOf(btnLogin)));
    }

    public AndroidElement getLblIncorrectPass() {
        return (AndroidElement)(new WebDriverWait(getDriver(),20).until(ExpectedConditions.visibilityOf(lblIncorrectPass)));
    }

    public AndroidElement getBtn_ok() {
        return (AndroidElement)(new WebDriverWait(getDriver(),20).until(ExpectedConditions.visibilityOf(btn_ok)));
    }

    public AndroidElement getUninstallOk() {
        return (AndroidElement)(new WebDriverWait(getDriver(),20).until(ExpectedConditions.visibilityOf(uninstallOk)));
    }

    public void setEmail(String email) {
        getTxtEmail().sendKeys(email);
    }

    public void setTxtPassword(String password) {
        getTxtPassword().sendKeys(password);
    }

    public void fillLogin(String email, String password) {
        setEmail(email);
        setTxtPassword(password);
    }

    public void click_on_login_button() {
        getBtnLogin().click();
    }

    public String get_incorrect_pass_msg() {
        return getLblIncorrectPass().getText();
    }


    public boolean isBtnOkDisplayed(){
        return getBtn_ok().isDisplayed();
    }

    public boolean isIncorrectPassDisplayed(){
        return getLblIncorrectPass().isDisplayed();
    }

    public void clickOnUninstallOk(){
        getUninstallOk().click();
    }

    public void clickOnUserName(){
        getTxtEmail().click();
    }
}
