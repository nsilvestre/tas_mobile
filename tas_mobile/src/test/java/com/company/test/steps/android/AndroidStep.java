package com.company.test.steps.android;


import com.company.framework.utils.Context;
import com.company.test.pages.android.CalculatorPage;
import com.company.test.pages.android.androidTestPage;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.appium.java_client.AppiumDriver;
import static com.company.framework.base.Framework.stop_appium_server;
import static org.assertj.core.api.Assertions.assertThat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class AndroidStep {

    private final Context context;
    private androidTestPage androidTestPage;
    private AppiumDriver driver;
    private CalculatorPage calculatorPage;
    private static final Logger LOGGER = LoggerFactory.getLogger(AndroidStep.class);


    public AndroidStep(Context context){
        this.context = context;
        driver = null;
    }

    @Given("^I run the android framework$")
    public void iRunTheAndroidFramework() throws Throwable {
        androidTestPage = new androidTestPage(driver);
    }

    @When("^I check the operation \"([^\"]*)\" equal to (\\d+)$")
    public void check_operation(String operation, int result) throws Throwable {
        instantiate_page();
        calculatorPage.sum_two_values(operation);
        assertThat(result).isEqualTo(calculatorPage.get_result());
    }


    private void instantiate_page() {
        driver = (AppiumDriver) context.getValue("driver");
        calculatorPage = new CalculatorPage(driver);
    }
}
