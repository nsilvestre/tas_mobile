package com.company.test.runner;


import com.company.framework.base.BaseUtil;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

//all working
/*
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/java/com/company/test/features/EATSA/A_SetUp.feature", "src/test/java/com/company/test/features/EATSA/AccountManagement.feature"},
        glue = {"com/company/test/steps/common", "com/company/test/steps/EATSA"},
        format = {"html:target/Eatsa/cucumber-pretty"},
        plugin = {
        "pretty",
        "json:target/CucumberJson/cucumber.json",
        "junit:target/cucumber-results.xml"
})
*/

//set up + AddItemToCart
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/java/com/company/test/features/EATSA"},
        glue = {"com/company/test/steps/common", "com/company/test/steps/EATSA"},
        format = {"html:target/Eatsa/cucumber-pretty"},
        plugin = {
                "pretty",
                "json:target/CucumberJson/cucumber.json",
                "junit:target/cucumber-results.xml"
        })

public class TestRunner {
    @BeforeClass
    public static void setUp(){
    }

    @AfterClass
    public static void  tearDown() {
    }
}

