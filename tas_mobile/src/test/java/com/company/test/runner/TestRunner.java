package com.company.test.runner;


import com.company.framework.base.BaseUtil;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

//@RunWith(Cucumber.class)
//@CucumberOptions(
////      features = { "src/test/java/com/company/test/features/web/local_sikullix.feature" },
////      glue = "com/company/test/steps/web",
//        plugin = {
//        "pretty",
//        "json:target/cucumber.json",
//        "junit:target/cucumber-results.xml" })

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {
        "pretty",
        "json:target/cucumber.json",
        "junit:target/cucumber-results.xml",
        "com.company.framework.reportPortalFiles.ScenarioReporter"
})

public class TestRunner {
    @BeforeClass
    public static void setUp(){

        String logPath = BaseUtil.getLogPath();
    }

    @AfterClass
    public static void tearDown() {
        /*Still working on Send reports to email*/
        //ZipFiles.zipDirectory(new File(BaseUtil.getLogPath()),BaseUtil.getLogPath()+".zip");
        //EMail.sendEmail("./log",BaseUtil.getParamDate()+".zip");
    }
}

