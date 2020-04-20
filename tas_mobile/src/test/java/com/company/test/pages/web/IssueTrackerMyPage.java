package com.company.test.pages.web;

import com.company.framework.pageobject.BasePO;
import com.company.framework.utils.RestHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class IssueTrackerMyPage extends BasePO {

    @FindBy(linkText = "Issues assigned to me")
    private WebElement lnk_issues_assigned_to_me;

    @FindBy(linkText = "Reported issues")
    private WebElement lnk_reported_issues;

    @FindBy(xpath = "//h2[contains(text(),'My page')]")
    private WebElement txt_my_page;


    private final RestHelper restHelper = new RestHelper();

    public IssueTrackerMyPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getIssuesAssignedtoMe() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(lnk_issues_assigned_to_me));
    }

    public WebElement getReportedIssues() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(lnk_reported_issues));
    }

    public WebElement getMyPage() {
        return new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(txt_my_page));
    }

    public void clickOnIssuesAssignedToMe() {
        getIssuesAssignedtoMe().click();
    }

    public void clickOnReportedIssues() {
        getReportedIssues().click();
    }

    public Boolean checkMyPage() {

        try {
            new WebDriverWait(getDriver(),5).until(ExpectedConditions.visibilityOf(txt_my_page));
            return true;
        }
        catch (NoSuchElementException e){
            return false;
        }



    }

}
