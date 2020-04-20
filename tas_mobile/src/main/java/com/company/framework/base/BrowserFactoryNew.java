package com.company.framework.base;

import com.company.framework.constants.Constants;
import com.company.framework.utils.Context;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BrowserFactoryNew {

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    private static WebDriver driver = null;
    private static ChromeDriverService serviceChrome;
    private static GeckoDriverService serviceFirefox;
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public static WebDriver getBrowser(String browser, boolean isHeadless, boolean mobileEmulated, String device)
    {
        WebDriver driver = null;
        String OS = System.getProperty("os.name");

        if (browser.equals("firefox"))
        {
            System.setProperty(Constants.SYSTEM_PROP_FIREFOX,getFirefoxPlatformPath(OS));
            FirefoxOptions firefoxOptions = new FirefoxOptions();

            if(isHeadless) {
                firefoxOptions.setHeadless(true);
            }
            firefoxOptions.addPreference("dom.webnotifications.enabled", false);
            driver = new FirefoxDriver(firefoxOptions);
        }
        else if (browser.equals("chrome"))
        {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("----disable-notifications");
            if(isHeadless){
                chromeOptions.addArguments("--headless");
            }
            if(mobileEmulated){
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", device);
                chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
            }
            System.setProperty(Constants.SYSTEM_PROP_CHROME, getChromePlatformPath(OS));
            driver = new ChromeDriver(chromeOptions);
        }
        return driver;
    }

    private static String getFirefoxPlatformPath(String OS){
        String path;
        String arch = System.getProperty("os.arch");
        if(OS.toLowerCase().contains("windows")){
            if(arch.toLowerCase().contains("64")) {
                path = BaseUtil.getWin64firePath();
            }
            else{
                path = BaseUtil.getWin32firePath();
            }
        }
        else if(OS.toLowerCase().contains("mac")){
            path = BaseUtil.getMacFirePath();
        }
        else{
            path = BaseUtil.getLinuxFirePath();
        }
        return path;
    }

    private static String getChromePlatformPath(String OS){
        String path;
        if(OS.toLowerCase().contains("windows")){
            path = BaseUtil.getWinChromePath();
        }
        else if(OS.toLowerCase().contains("mac")){
            path = BaseUtil.getMacChromePath();
        }
        else{
            path = BaseUtil.getLinuxChromePath();
        }
        return path;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public static WebDriver getBrowserDriver(Context context, String browserName, Map<String, Object> capMap, String OS) throws Exception {
        String pathDriver;
        String pathCurrent = System.getProperty("user.dir");

        if (browserName.equals("firefox")) {
            String firefox = getFirefoxPlatformPath(OS);
            pathDriver = pathCurrent + firefox;
            serviceFirefox = new GeckoDriverService.Builder()
                    .usingDriverExecutable(new File(pathDriver))
                    .usingAnyFreePort()
                    .build();
            serviceFirefox.start();

            driver = new RemoteWebDriver(serviceFirefox.getUrl(), new DesiredCapabilities(capMap));

        }
        else if (browserName.equals("chrome"))
            {
                String chrome = getChromePlatformPath(OS);
                pathDriver = pathCurrent + chrome;
                serviceChrome = new ChromeDriverService.Builder()
                        .usingDriverExecutable(new File(pathDriver))
                        .usingAnyFreePort()
                        .build();
                serviceChrome.start();

                driver = new RemoteWebDriver(serviceChrome.getUrl(), new DesiredCapabilities(capMap));
            }
            else{
            //If no browser passed throw exception
            throw new Exception("Not Supported Browser.");
        }
        return driver;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

}
