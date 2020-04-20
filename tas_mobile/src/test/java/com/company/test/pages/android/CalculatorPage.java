package com.company.test.pages.android;

import com.company.framework.pageobject.MobileBasePO;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CalculatorPage extends MobileBasePO {

    @AndroidFindBy(xpath = "//android.widget.Button[@text='0']")
    private AndroidElement btnZero;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='1']")
    private AndroidElement btnOne;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='2']")
    private MobileElement btnTwo;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='3']")
    private AndroidElement btnThree;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='4']")
    private AndroidElement btnFour;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='5']")
    private AndroidElement btnFive;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='6']")
    private AndroidElement btnSix;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='7']")
    private AndroidElement btnSeven;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='8']")
    private AndroidElement btnEight;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='9']")
    private AndroidElement btnNine;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='+']")
    private AndroidElement btnSum;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='-']")
    private AndroidElement btnSub;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='/']")
    private AndroidElement btnDiv;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='*']")
    private AndroidElement btnMul;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='=']")
    private AndroidElement btnEqual;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id='com.android.calculator2:id/del']")
    private AndroidElement btnDelete;

    @AndroidFindBy(xpath = "//android.widget.TextView[contains(@resource-id,'id/result')]")
    private AndroidElement txtResult;


    public CalculatorPage(AppiumDriver driver) {
        super(driver);
    }

    public AndroidElement getBtnZero() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnZero)));
    }

    public AndroidElement getBtnOne() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnOne)));
    }

    public AndroidElement getBtnTwo() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnTwo)));
    }

    public AndroidElement getBtnThree() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnThree)));
    }

    public AndroidElement getBtnFour() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnFour)));
    }

    public AndroidElement getBtnFive() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnFive)));
    }

    public AndroidElement getBtnSix() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnSix)));
    }

    public AndroidElement getBtnFSeven() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnSeven)));
    }

    public AndroidElement getBtnEight() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnEight)));
    }

    public AndroidElement getBtnNine() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnNine)));
    }

    public AndroidElement getBtnSum() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnSum)));
    }

    public AndroidElement getBtnSub() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnSub)));
    }

    public AndroidElement getBtnDiv() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnDiv)));
    }

    public AndroidElement getBtnMul() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnMul)));
    }

    public AndroidElement getBtnEqual() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnEqual)));
    }

    public AndroidElement getBtnDelete() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(btnDelete)));
    }

    public AndroidElement getTxtResult() {
        return (AndroidElement)(new WebDriverWait(getDriver(),60).until(ExpectedConditions.visibilityOf(txtResult)));
    }

    public int get_result() {
        return Integer.parseInt(getTxtResult().getText());
    }

    public void click_on_number(int number) {
        switch (number){
            case 0:
                getBtnZero().click();
                break;
            case 1:
                getBtnOne().click();
                break;
            case 2:
                getBtnTwo().click();
                break;
            case 3:
                getBtnThree().click();
                break;
            case 4:
                getBtnFour().click();
                break;
            case 5:
                getBtnFive().click();
                break;
            case 6:
                getBtnSix().click();
                break;
            case 7:
                getBtnFSeven().click();
                break;
            case 8:
                getBtnEight().click();
                break;
            case 9:
                getBtnNine().click();
                break;
        }
    }

    public void sum_two_values(String operation) {
        String[] parts = operation.split("\\+");
        int num1 = Integer.parseInt(parts[0]);
        int num2 = Integer.parseInt(parts[1]);
        click_on_calc_number(num1);
        getBtnSum().click();
        click_on_calc_number(num2);
        getBtnEqual().click();
    }

    public void click_on_calc_number(int num) {
        char[] digits = String.valueOf(num).toCharArray();
        for(int i=0; i < digits.length; i++) {
            click_on_number(Character.getNumericValue(digits[i]));
        }
    }


}
