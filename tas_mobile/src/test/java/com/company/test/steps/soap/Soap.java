package com.company.test.steps.soap;


import com.company.framework.dtos.wsdl_calculator.Calculator;
import com.company.framework.dtos.wsdl_calculator.CalculatorSoap;
import com.company.framework.utils.Context;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static com.company.framework.base.Framework.process_setup_soap_handler;
import static org.assertj.core.api.Assertions.assertThat;
import javax.xml.namespace.QName;
import java.util.Iterator;


public class Soap {
    private static Calculator service;
    private static CalculatorSoap soapEndpoint;
    private static int response;
    private static int counterPorts = 0;
    private final Context context;
    private static int numberA;
    private static int numberB;

    public Soap(Context context) {
        this.context = context;
    }


    @After
    public void tearDown() throws Exception {

    }

    @Given("([^\"]*) Handler for \"([^\"]*)\"$")
    public void setup_handler(String configName, String typeTest) throws Exception {
        context.setValue("typeTest", typeTest);
        service = process_setup_soap_handler(context, configName);

        String clientlog1 = context.getValue("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump")
                .toString();
        String serverlog1 = context.getValue("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump")
                .toString();
        String clientlog2 = context.getValue("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump")
                .toString();
        String serverlog2 = context.getValue("com.sun.xml.ws.transport.http.HttpAdapter.dump")
                .toString();

        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", clientlog1);
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", serverlog1);
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", clientlog2);
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", serverlog2);
        soapEndpoint = service.getCalculatorSoap();
    }

    @When("I use \"([^\"]*)\" webEndpoint$")
    public void use_web_endpoint(String webEndpoint) throws Exception {
        if (webEndpoint.equals("getCalculatorSoap12")) {
            soapEndpoint = service.getCalculatorSoap12();
        } else if (webEndpoint.equals("getCalculatorSoap")) {
            soapEndpoint = service.getCalculatorSoap12();
        } else  {
            System.out.println("The webElement does not exist.");
        }
    }

    @When("^I sum the number (\\d+) and the number (\\d+) in the calculator$")
    public void get_result_of_sum(int  intA, int intB) throws Exception {
        numberA = intA;
        numberB = intB;
        response = soapEndpoint.add(intA, intB);

        System.out.println("Response : " + response);
    }

    @Then("^I should validate the result of the sum")
    public void validate_result_of_the_sum() throws Exception {
        assertThat(numberA + numberB).isEqualTo(response);
    }

    @Then("^I should validate the result of the multiplication")
    public void validate_result_of_the_multiplication() throws Exception {
        assertThat(5 * 5).isEqualTo(response);
    }

    @When("^I call web service using \"([^\"]*)\" method$")
    public void use_method_in_web_service(String methodName) throws Exception {
        if (methodName.equals("multiply")) {
            response = soapEndpoint.multiply(5,5);
        } else {
            System.out.println("The method does not exist.");
        }
    }

    @When("^I get all ports$")
    public void get_ports() throws Exception {
        Iterator<QName> itr = service.getPorts();

        while(itr.hasNext()) {
            itr.next();
            System.out.println("Port: " + itr.next().toString());
            counterPorts++;
        }
    }

    @Then("^I should validate the ports$")
    public void validate_ports() throws Exception {
        assertThat(1).isEqualTo(counterPorts);
    }
}
