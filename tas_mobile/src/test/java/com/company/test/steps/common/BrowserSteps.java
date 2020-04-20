package com.company.test.steps.common;

import com.company.framework.base.BaseUtil;
import com.company.framework.utils.Context;
import com.company.framework.utils.Utility;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import net.jodah.failsafe.Failsafe;
import org.junit.AssumptionViolatedException;
import org.openqa.selenium.WebDriver;

import java.util.Collection;

import static com.company.framework.base.Framework.setup_desktop_browser;

public class BrowserSteps {
    private final Context context;
    private WebDriver driver;

    public BrowserSteps(Context context){
        this.context = context;
        driver = null;
    }

    @Before
    public void beforeScenario(Scenario sc) {
        Collection<String> tags = sc.getSourceTagNames();
        String tagName = null;
        for(Object tag : tags){
            tagName = tag.toString();
        }

        if (!tagName.equals("@working")) {
            throw new AssumptionViolatedException("Scenario not tagged as working, so skipping");
        }
    }

    // It will execute after scenario execution
    @After
    public void tearDown(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                //Screenshot
                Utility.captureScreenshot(driver, scenario);

                //Screenshot with scroll (full page)
                //Utility.captureScreenshotWithScroll(driver, scenario);

                //Screenshot embed in report
                Utility.embedScreenshot(driver, scenario);
                driver.quit();
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Given("([^\"]*) desktop browser$")
    public void setup_desktop(String configName) throws Exception {
        context.setValue("gridServerUrl", BaseUtil.getGridServerUrl());
        Failsafe.with(BaseUtil.getRetryPolicy()).run(()->driver= setup_desktop_browser(context, configName));
        context.setValue("driver", driver);
    }

}
