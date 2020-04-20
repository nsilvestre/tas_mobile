package com.company.framework.base;

import com.company.framework.dtos.wsdl_calculator.Calculator;
import com.company.framework.utils.Context;
import com.company.framework.utils.JsonConfigReader;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import cucumber.api.DataTable;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.dbunit.JdbcDatabaseTester;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.company.framework.base.DeviceFactory.*;


public class Framework {
    private static Context context;
    private static WebDriver webdriver = null;
    private static AppiumDriver appiumDriver = null;
    private static RequestSpecBuilder builder = null;
    private static JdbcDatabaseTester databaseTester = null;
    private static final HashMap<String, Object> serverParameters = new HashMap<String, Object>();
    private static JsonConfigReader jsonConfigReader = new JsonConfigReader();
    private static MongoClient client;
    private static URL WS_ENDPOINT;
    private static Calculator service;
    private static Map<String, Object> capMap;
    private static DataTable table;

    public Framework(Context context){
        this.context = context;
    }

    /////////////////////////////// New Functions for Refactor ///////////////////////////////////////////////
    public static WebDriver setup_desktop_browser(Context context, String config) throws Exception {
        capMap = jsonConfigReader.getBrowserJsonConfig(config);
        String OS = System.getProperty("os.name");
        context.setValue("platform", OS);
        context.setValue("cap", capMap);

        //LOCAL BROWSER
        if (context.getValue("gridServerUrl").equals("")) {
            String browserName = capMap.get("browserName").toString();
            context.setValue("browserName", browserName);
            webdriver = BrowserFactoryNew.getBrowserDriver(context, browserName, capMap, OS);
        }

        //REMOTE BROWSER
        else {
            String hub_url = context.getValue("gridServerUrl").toString();
            webdriver = create_webdriver_remote(hub_url, new DesiredCapabilities(capMap));
        }

        return webdriver;
    }

    public static void updateCapabilities() {
        if (table != null) {
            List<List<String>> data = table.raw();
            for (int i = 0; i < data.size(); i++) {
                if (!data.get(i).get(0).equals("field")) {
                    if (data.get(i).get(0).equals("appWaitDuration")) {
                        capMap.put(data.get(i).get(0), Integer.parseInt(data.get(i).get(1)));
                    } else {
                        capMap.put(data.get(i).get(0), data.get(i).get(1));
                    }
                }
            }
        }
    }

    public static AppiumDriver setup_appium_driver(Context context, String config) throws Exception {
        capMap = jsonConfigReader.getAppiumJsonConfig(config);
        table = (DataTable) context.getValue("dataTable");

        updateCapabilities();

        String device = capMap.get("platformName").toString();
        String hub_url = context.getValue("gridServerUrl").toString();

        //LOCAL APPIUM
        if (hub_url.equals("")) {
            String address = BaseUtil.getAppiumServerAddress();
            String port = BaseUtil.getAppiumServerPort();
            serverParameters.put("address", address);
            serverParameters.put("port", port);

            String appium_server_url = "http://" + address + ":" + port + "/wd/hub";

            if (context.getValue("appium_subprocess") == null) {
                context.setValue("appium_subprocess", start_appium_server(serverParameters));
            }

            appiumDriver = create_appium_driver(device, appium_server_url, new DesiredCapabilities(capMap));
        }

        //REMOTE APPIUM
        else {
            appiumDriver = create_appium_driver(device, hub_url, new DesiredCapabilities(capMap));
        }

        return appiumDriver;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    public static AppiumDriverLocalService start_appium_server(HashMap<String, Object> serverPar) {

        return startMacAppiumServer(serverPar.get("address").toString(), Integer.parseInt(serverPar.get("port").toString()));
    }

    public static AppiumDriverLocalService stop_appium_server(){
        return stopAppiumServer();
    }

    public static WebDriver create_webdriver_remote(String url, DesiredCapabilities capability) throws
            MalformedURLException {
        webdriver = new RemoteWebDriver(new URL(url), capability);
        return webdriver;
    }

    public static AppiumDriver create_appium_driver(String device,String url, DesiredCapabilities capability) throws
            MalformedURLException {
        appiumDriver = getDevice(device,url,capability);
        return appiumDriver;
    }

    /*****************************************************************************************************************/

    public static RequestSpecBuilder process_setup_rest_handler(Context context, String config) throws Exception {
        Map<String, Object> jsonMap;
        jsonMap = jsonConfigReader.getRestJsonConfig(config);
        addKeysAndValuesToContext(context, jsonMap);
        builder = instantiate_rest_handler_setup(context);

        return builder;
    }

    public static JdbcDatabaseTester process_setup_db_handler(Context context, String config) throws Exception {
        Map<String, Object> jsonMap;
        jsonMap = jsonConfigReader.getDbJsonConfig(config);
        addKeysAndValuesToContext(context, jsonMap);
        databaseTester = instantiate_db_handler_setup(context);

        return databaseTester;
    }

    public static Calculator process_setup_soap_handler(Context context, String config) throws Exception {
        Map<String, Object> jsonMap;
        jsonMap = jsonConfigReader.getSoapJsonConfig(config);
        addKeysAndValuesToContext(context, jsonMap);
        service = instantiate_soap_handler_setup(context);

        return service;
    }

    public static MongoClient process_setup_dbnonsql_handler(Context context, String config) throws Exception {
        Map<String, Object> jsonMap;
        jsonMap = jsonConfigReader.getDbNonSqlJsonConfig(config);
        addKeysAndValuesToContext(context, jsonMap);

        String debug = context.getValue("DEBUG.MONGO").toString();
        String trace = context.getValue("DB.TRACE").toString();

        // Enable MongoDB logging in general
        System.setProperty("DEBUG.MONGO", debug);
        // Enable DB operation tracing
        System.setProperty("DB.TRACE", trace);

        client = instantiate_dbnonsql_handler_setup(context);

        return client;
    }

    public static void addKeysAndValuesToContext(Context context, Map<String, Object> map){
        for (Map.Entry<String, Object> pair : map.entrySet()) {
            context.setValue(pair.getKey(), pair.getValue().toString());
        }
    }

    public static RequestSpecBuilder instantiate_rest_handler_setup(Context context) throws Exception {
        String value = context.getValue("typeTest").toString().toLowerCase();

        if (value.equals("rest")) {
            builder = new RequestSpecBuilder();
        }
        else{
            //If no browser passed throw exception
            throw new Exception("Not Supported Capability.");
        }

        return builder;
    }

    public static JdbcDatabaseTester instantiate_db_handler_setup(Context context) throws Exception {
        String value = context.getValue("typeTest").toString().toLowerCase();
        String connectionUrl = context.getValue("connectionUrl").toString();
        String username = context.getValue("username").toString();
        String password = context.getValue("password").toString();
        String driverClass = context.getValue("driverClass").toString();

        if (value.equals("db")) {
            databaseTester = new JdbcDatabaseTester(driverClass, connectionUrl, username, password);
        }
        else{
            //If no browser passed throw exception
            throw new Exception("Not Supported Capability.");
        }

        return databaseTester;
    }

    public static MongoClient instantiate_dbnonsql_handler_setup(Context context) throws Exception {
        String value = context.getValue("typeTest").toString().toLowerCase();
        String host = context.getValue("host").toString();
        int port = Integer.parseInt(context.getValue("port").toString());

        if (value.equals("dbnonsql")) {
            try {
                client    = new MongoClient(new ServerAddress(host, port));
            } catch (UnknownHostException e) {
                System.out.println(e);
            }
        }
        else{
            //If no browser passed throw exception
            throw new Exception("Not Supported Capability.");
        }

        return client;
    }

    public static Calculator instantiate_soap_handler_setup(Context context) throws Exception {
        String value = context.getValue("typeTest").toString().toLowerCase();
        String wsEndpoint = context.getValue("wsEndpoint").toString().toLowerCase();

        if (value.equals("soap")) {
            WS_ENDPOINT = new URL(wsEndpoint);
            service = new Calculator(WS_ENDPOINT);
        }
        else{
            //If no browser passed throw exception
            throw new Exception("Not Supported Capability.");
        }

        return service;
    }
}
